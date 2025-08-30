package com.farmacia.negocio.servico;

import com.farmacia.negocio.entidade.Lote;
import com.farmacia.negocio.entidade.Produto;
import com.farmacia.dados.repositorio.IRepositorioEstoque;
import com.farmacia.dados.repositorio.IRepositorioProdutos;
import com.farmacia.negocio.excecao.DadosInvalidosException;
import com.farmacia.negocio.excecao.estoque.LoteNaoEncontradoException;
import com.farmacia.negocio.excecao.produto.ProdutoNaoEncontradoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EstoqueService {

    private final IRepositorioEstoque loteRepository;
    private final IRepositorioProdutos produtoRepository;
    private static final int DIAS_PARA_ALERTA_VENCIMENTO = 30;

    public EstoqueService(IRepositorioEstoque loteRepo, IRepositorioProdutos produtoRepo) {
        this.loteRepository = loteRepo;
        this.produtoRepository = produtoRepo;
    }

    /**
     * Adiciona um novo lote de um produto existente ao estoque.
     */
    public void adicionarLote(int idProduto, int quantidade, LocalDate dataValidade) {
        Produto produto = produtoRepository.buscarPorId(idProduto);
        if (produto == null) {
            throw new ProdutoNaoEncontradoException(idProduto);
        }

        Lote novoLote = new Lote(produto, quantidade, dataValidade);

        loteRepository.salvar(novoLote);
    }

    public String consultarStatusDetalhadoProduto(int idProduto) {
        Produto produto = produtoRepository.buscarPorId(idProduto);
        if (produto == null) {
            throw new ProdutoNaoEncontradoException(idProduto);
        }

        List<Lote> lotesDoProduto = loteRepository.listarPorProduto(produto);

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("--- DETALHES DO PRODUTO ---\n");
        relatorio.append("ID: ").append(produto.getId()).append("\n");
        relatorio.append("Código: ").append(produto.getCodigo()).append("\n");
        relatorio.append("Nome: ").append(produto.getNome()).append("\n");
        relatorio.append("Fabricante: ").append(produto.getFabricante()).append("\n");
        relatorio.append(String.format("Preço: R$ %.2f\n", produto.getPreco()));
        relatorio.append("Estoque Mínimo Definido: ").append(produto.getEstoqueMinimo()).append(" unidades\n");

        int estoqueTotal = 0;
        for (Lote lote : lotesDoProduto) {
            estoqueTotal += lote.getQuantidade();
        }

        relatorio.append("ESTOQUE TOTAL ATUAL: ").append(estoqueTotal).append(" unidades\n");
        relatorio.append("---------------------------\n");

        if (lotesDoProduto.isEmpty()) {
            relatorio.append("Nenhum lote em estoque.\n");
        } else {
            relatorio.append("LOTES EM ESTOQUE:\n");
            for (Lote lote : lotesDoProduto) {
                relatorio.append(String.format(" - Lote ID: %d | Qtd: %d | Validade: %s\n",
                        lote.getId(), lote.getQuantidade(), lote.getDataValidade()));
            }
        }
        relatorio.append("---------------------------\n");

        return relatorio.toString();
    }

    /**
     * Permite que um Supervisor ajuste manualmente a quantidade de um lote específico.
     */
    public void ajustarQuantidadeLote(int idLote, int novaQuantidade) {
        if (novaQuantidade < 0) {
            throw new DadosInvalidosException("A quantidade do lote não pode ser negativa.");
        }

        Lote lote = loteRepository.buscarPorId(idLote);

        if (lote == null) {
            throw new LoteNaoEncontradoException(idLote);
        }

        loteRepository.atualizar(lote);
        System.out.println("Estoque do lote " + idLote + " ajustado para " + novaQuantidade);
    }

    /**
     * Gera uma lista de notificações sobre problemas no estoque.
     */
    public List<String> verificarAlertasDeEstoque() {
        List<String> alertas = new ArrayList<>();
        List<Lote> todosOsLotes = loteRepository.listarTodos();
        LocalDate dataLimite = LocalDate.now().plusDays(DIAS_PARA_ALERTA_VENCIMENTO);

        for (Lote lote : todosOsLotes) {
            if (!lote.estaVencido() && lote.getQuantidade() > 0 && lote.getDataValidade().isBefore(dataLimite)) {
                alertas.add(
                        String.format("[VENCIMENTO PRÓXIMO] %d un. de '%s' (Lote %d) vencem em %s.",
                                lote.getQuantidade(), lote.getProduto().getNome(), lote.getId(), lote.getDataValidade())
                );
            }
        }

        List<Produto> todosOsProdutos = produtoRepository.listarTodos();
        for (Produto produto : todosOsProdutos) {
            int estoqueAtual = 0;
            for (Lote lote : todosOsLotes) {
                if (lote.getProduto().getId() == produto.getId() && !lote.estaVencido()) {
                    estoqueAtual += lote.getQuantidade();
                }
            }
            if (estoqueAtual > 0 && estoqueAtual <= produto.getEstoqueMinimo()) {
                alertas.add(
                        String.format("[ESTOQUE BAIXO] '%s' (Cód: %s) está com apenas %d unidades.",
                                produto.getNome(), produto.getCodigo(), estoqueAtual)
                );
            }
        }

        return alertas;
    }
}