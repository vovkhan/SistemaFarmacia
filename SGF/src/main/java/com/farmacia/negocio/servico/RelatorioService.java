package com.farmacia.negocio.servico;

import com.farmacia.negocio.entidade.*;
import com.farmacia.dados.repositorio.IRepositorioFuncionarios;
import com.farmacia.dados.repositorio.IRepositorioVendas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelatorioService {

    private final IRepositorioVendas vendaRepository;
    private final IRepositorioFuncionarios funcionarioRepository;

    public RelatorioService(IRepositorioVendas vendaRepo, IRepositorioFuncionarios funcionarioRepo) {
        this.vendaRepository = vendaRepo;
        this.funcionarioRepository = funcionarioRepo;
    }

    /**
     * Gera um relatório com os N produtos mais vendidos.
     */
    public List<String> gerarRelatorioProdutosMaisVendidos(int topN) {
        List<Venda> todasAsVendas = vendaRepository.listarTodos();
        ArrayList<ContadorProduto> contagem = new ArrayList<>();

        for (Venda venda : todasAsVendas) {
            for (ItemVenda item : venda.getItens()) {
                Produto produtoDoItem = item.getProduto();
                int quantidadeDoItem = item.getQuantidade();
                boolean produtoJaContado = false;

                for (ContadorProduto contador : contagem) {
                    if (contador.getProduto().equals(produtoDoItem)) {
                        contador.adicionarQuantidade(quantidadeDoItem);
                        produtoJaContado = true;
                        break;
                    }
                }

                if (!produtoJaContado) {
                    contagem.add(new ContadorProduto(produtoDoItem, quantidadeDoItem));
                }
            }
        }
        contagem.sort((c1, c2) -> Integer.compare(c2.getQuantidadeTotal(), c1.getQuantidadeTotal()));

        List<String> relatorio = new ArrayList<>();
        relatorio.add("--- RELATÓRIO DE PRODUTOS MAIS VENDIDOS ---");
        int limite = Math.min(topN, contagem.size());
        for (int i = 0; i < limite; i++) {
            ContadorProduto contador = contagem.get(i);
            Produto p = contador.getProduto();
            relatorio.add(String.format("%d. [%s] %s - %d unidades vendidas",
                    (i + 1), p.getCodigo(), p.getNome(), contador.getQuantidadeTotal()));
        }
        return relatorio;
    }

    /**
     * Gera um relatório com os N produtos menos vendidos.
     */
    public List<String> gerarRelatorioProdutosMenosVendidos(int topN) {
        List<Venda> todasAsVendas = vendaRepository.listarTodos();
        ArrayList<ContadorProduto> contagem = new ArrayList<>();
        for (Venda venda : todasAsVendas) {
            for (ItemVenda item : venda.getItens()) {
                Produto produtoDoItem = item.getProduto();
                int quantidadeDoItem = item.getQuantidade();
                boolean produtoJaContado = false;
                for (ContadorProduto contador : contagem) {
                    if (contador.getProduto().equals(produtoDoItem)) {
                        contador.adicionarQuantidade(quantidadeDoItem);
                        produtoJaContado = true;
                        break;
                    }
                }
                if (!produtoJaContado) {
                    contagem.add(new ContadorProduto(produtoDoItem, quantidadeDoItem));
                }
            }
        }

        contagem.sort((c1, c2) -> Integer.compare(c1.getQuantidadeTotal(), c2.getQuantidadeTotal()));

        List<String> relatorio = new ArrayList<>();
        relatorio.add("--- RELATÓRIO DE PRODUTOS MENOS VENDIDOS ---");
        int limite = Math.min(topN, contagem.size());
        for (int i = 0; i < limite; i++) {
            ContadorProduto contador = contagem.get(i);
            Produto p = contador.getProduto();
            relatorio.add(String.format("%d. [%s] %s - %d unidades vendidas",
                    (i + 1), p.getCodigo(), p.getNome(), contador.getQuantidadeTotal()));
        }
        return relatorio;
    }

    /**
     * Gera um relatório de desempenho para todos os funcionários.
     */
    public List<String> gerarRelatorioDesempenhoFuncionarios() {
        List<Funcionario> todosOsFuncionarios = funcionarioRepository.listarTodosAtivos();
        List<String> relatorio = new ArrayList<>();
        relatorio.add("--- RELATÓRIO DE DESEMPENHO DE FUNCIONÁRIOS ---");

        for (Funcionario func : todosOsFuncionarios) {
            String resumo = func.getResumoDeDesempenho();
            relatorio.add(String.format("[%s] %s (Cód: %s)",
                    func.getNomeCargo().toUpperCase(), func.getNome(), func.getCodigo()));
            relatorio.add("   => Desempenho: " + resumo);
            relatorio.add("");
        }
        return relatorio;
    }

    public String gerarRelatorioFaturamento(LocalDate dataInicio, LocalDate dataFim) {
        List<Venda> todasAsVendas = vendaRepository.listarTodos();
        ArrayList<Venda> vendasNoPeriodo = new ArrayList<>();

        for (Venda venda : todasAsVendas) {
            LocalDate dataDaVenda = venda.getDataHora().toLocalDate();
            boolean ehVendaValida = !venda.isEhReembolso();
            boolean depoisDoInicio = !dataDaVenda.isBefore(dataInicio);
            boolean antesDoFim = !dataDaVenda.isAfter(dataFim);

            if (ehVendaValida && depoisDoInicio && antesDoFim) {
                vendasNoPeriodo.add(venda);
            }
        }

        double faturamentoTotal = 0.0;
        for (Venda venda : vendasNoPeriodo) {
            faturamentoTotal += venda.getValorTotal();
        }

        int numeroDeVendas = vendasNoPeriodo.size();
        double ticketMedio = (numeroDeVendas > 0) ? faturamentoTotal / numeroDeVendas : 0;

        String relatorio = String.format(
                "--- RELATÓRIO DE FATURAMENTO (%s a %s) ---\n" +
                        "Número de Vendas: %d\n" +
                        "Ticket Médio: R$ %.2f\n" +
                        "FATURAMENTO TOTAL: R$ %.2f\n" +
                        "--------------------------------------------------",
                dataInicio, dataFim, numeroDeVendas, ticketMedio, faturamentoTotal
        );
        return relatorio;
    }
}
