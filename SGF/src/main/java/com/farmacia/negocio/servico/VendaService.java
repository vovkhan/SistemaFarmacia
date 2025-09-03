package com.farmacia.negocio.servico;

import com.farmacia.negocio.entidade.*;
import com.farmacia.dados.repositorio.*;
import com.farmacia.negocio.excecao.venda.*;
import com.farmacia.negocio.excecao.estoque.EstoqueInsuficienteException;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class VendaService {
    private final IRepositorioVendas vendaRepository;
    private final IRepositorioEstoque loteRepository;
    private final IRepositorioClientes clienteRepository;
    private final IRepositorioFuncionarios funcionarioRepository;

    public VendaService(IRepositorioVendas vendaRepo, IRepositorioEstoque loteRepo, IRepositorioClientes clienteRepo, IRepositorioFuncionarios funcRepo) {
        this.vendaRepository = vendaRepo;
        this.loteRepository = loteRepo;
        this.clienteRepository = clienteRepo;
        this.funcionarioRepository = funcRepo;
    }

    /**
     * Realiza o processo de uma nova venda.
     */
    public Venda processarVenda(Atendente vendedor, Cliente cliente, ArrayList<ItemVenda> carrinho, int pontosParaUsar) {
        if (carrinho == null || carrinho.isEmpty()) {
            throw new CarrinhoVazioException();
        }
        if (pontosParaUsar > 0 && !cliente.isCadastrado()) {
            throw new UsoDePontosNaoPermitidoException();
        }

        Venda novaVenda = new Venda(vendedor, cliente);

        for (ItemVenda itemDoCarrinho : carrinho) {
            Produto produtoDesejado = itemDoCarrinho.getProduto();
            int quantidadeDesejada = itemDoCarrinho.getQuantidade();

            List<Lote> lotesDisponiveis = loteRepository.listarPorProduto(produtoDesejado);
            lotesDisponiveis.sort(Comparator.comparing(Lote::getDataValidade));

            int estoqueTotal = 0;
            for (Lote lote : lotesDisponiveis) {
                estoqueTotal += lote.getQuantidade();
            }
            if (estoqueTotal < quantidadeDesejada) {
                throw new EstoqueInsuficienteException(produtoDesejado.getNome());
            }

            int quantidadeAtendida = 0;
            for (Lote lote : lotesDisponiveis) {
                int qtdParaRetirarDoLote = Math.min(lote.getQuantidade(), quantidadeDesejada - quantidadeAtendida);
                try {
                    lote.darBaixa(qtdParaRetirarDoLote);
                } catch (IllegalStateException e) {
                    throw new EstoqueInsuficienteException(produtoDesejado.getNome());
                }
                loteRepository.atualizar(lote);
                quantidadeAtendida += qtdParaRetirarDoLote;
                if (quantidadeAtendida == quantidadeDesejada) break;
            }
            novaVenda.adicionarItem(produtoDesejado, quantidadeDesejada);
        }

        if (pontosParaUsar > 0) {
            try {
                cliente.resgatarPontos(pontosParaUsar);
            } catch (IllegalStateException e) {
                throw new PontosInsuficientesException(); // Traduzindo a exceção
            }
            novaVenda.aplicarDescontoFidelidade(pontosParaUsar);
        }

        novaVenda.calcularTotais();

        int pontosGanhos = novaVenda.calcularTotalPontosGerados();
        cliente.adicionarPontos(pontosGanhos);
        clienteRepository.atualizar(cliente);

        vendedor.registrarNovaVenda(novaVenda.getValorTotal());
        funcionarioRepository.atualizar(vendedor);

        vendaRepository.salvar(novaVenda);
        return novaVenda;
    }

    /**
     * Realiza o processo de reembolso de itens de uma venda.
     */
    public Venda processarReembolso(Venda vendaOriginal, ArrayList<ItemVenda> itensAReembolsar, Atendente atendenteDoReembolso) {
        if (vendaOriginal.isEhReembolso()) {
            throw new ReembolsoInvalidoException("Não é possível reembolsar uma transação que já é um reembolso.");
        }
        validarItensDoReembolso(vendaOriginal, itensAReembolsar);

        double valorTotalAReembolsar = 0;
        int pontosADeduzir = 0;
        Cliente cliente = vendaOriginal.getCliente();

        for (ItemVenda itemDevolvido : itensAReembolsar) {
            Produto produto = itemDevolvido.getProduto();
            int quantidadeDevolvida = itemDevolvido.getQuantidade();

            Lote loteReembolsado = new Lote(produto, quantidadeDevolvida, LocalDate.now().plusYears(1));
            loteRepository.salvar(loteReembolsado);

            double precoOriginal = encontrarPrecoOriginal(vendaOriginal, produto);
            valorTotalAReembolsar += quantidadeDevolvida * precoOriginal;

            pontosADeduzir += produto.calcularPontosGerados(quantidadeDevolvida);
        }

        if (cliente.isCadastrado() && pontosADeduzir > 0) {
            try {
                cliente.resgatarPontos(pontosADeduzir);
                clienteRepository.atualizar(cliente);
            } catch (IllegalStateException e) {
                System.out.println("[AVISO] Cliente não possuía saldo de pontos suficiente para a dedução do reembolso.");
            }
        }

        Venda transacaoReembolso = new Venda(atendenteDoReembolso, cliente);
        transacaoReembolso.setEhReembolso(true);

        for (ItemVenda itemDevolvido : itensAReembolsar) {
            transacaoReembolso.adicionarItem(itemDevolvido.getProduto(), itemDevolvido.getQuantidade());
        }

        transacaoReembolso.setValorTotal(-valorTotalAReembolsar);
        vendaRepository.salvar(transacaoReembolso);

        atendenteDoReembolso.registrarNovaVenda(-valorTotalAReembolsar);
        funcionarioRepository.atualizar(atendenteDoReembolso);

        return transacaoReembolso;
    }

    /**
     * Valida se os itens e quantidades a serem reembolsados são consistentes com a venda original.
     */
    private void validarItensDoReembolso(Venda vendaOriginal, ArrayList<ItemVenda> itensAReembolsar) {
        for (ItemVenda itemDevolvido : itensAReembolsar) {
            Produto produto = itemDevolvido.getProduto();
            int quantidadeDevolvida = itemDevolvido.getQuantidade();

            int quantidadeComprada = 0;

            for (ItemVenda itemOriginal : vendaOriginal.getItens()) {

                if (itemOriginal.getProduto().equals(produto)) {
                    quantidadeComprada += itemOriginal.getQuantidade();
                }
            }

            if (quantidadeComprada == 0) {
                throw new ReembolsoInvalidoException("O produto '" + produto.getNome() + "' não consta na venda original.");
            }
            if (quantidadeDevolvida > quantidadeComprada) {
                throw new ReembolsoInvalidoException("A quantidade a devolver de '" + produto.getNome() + "' é maior que a comprada.");
            }
        }
    }

    /**
     * Encontra o preço unitário que foi pago por um produto em uma venda específica.
     */
    private double encontrarPrecoOriginal(Venda vendaOriginal, Produto produto) {
        for (ItemVenda item : vendaOriginal.getItens()) {
            if (item.getProduto().equals(produto)) {
                return item.getPrecoUnitarioGravado();
            }
        }

        return 0.0;
    }

    /**
     * Busca vendas no repositório e as filtra com base nos critérios fornecidos.
     */
    public List<Venda> consultarVendas(LocalDate dataInicio, LocalDate dataFim, Cliente cliente) {
        List<Venda> todasAsVendas = vendaRepository.listarTodos();
        ArrayList<Venda> vendasFiltradas = new ArrayList<>();

        for (Venda venda : todasAsVendas) {
            if ((dataInicio == null || !venda.getDataHora().toLocalDate().isBefore(dataInicio)) &&
                    (dataFim == null || !venda.getDataHora().toLocalDate().isAfter(dataFim)) &&
                    (cliente == null || venda.getCliente().equals(cliente)))
            {
                vendasFiltradas.add(venda);
            }
        }

        return vendasFiltradas;
    }

    public Venda buscarVendaPorCodigo(String codigo) {
        Venda venda = vendaRepository.buscarPorCodigo(codigo);
        if(venda == null){
            throw new VendaNaoEncontradaException(codigo);
        }
        return venda;
    }
}