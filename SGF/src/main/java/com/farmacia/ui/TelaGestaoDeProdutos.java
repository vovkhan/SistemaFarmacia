package com.farmacia.ui;

import com.farmacia.fachada.FachadaFarmacia;
import com.farmacia.negocio.entidade.*;
import com.farmacia.negocio.excecao.*;
import com.farmacia.negocio.excecao.estoque.LoteNaoEncontradoException;
import com.farmacia.negocio.excecao.produto.ProdutoComEstoqueException;
import com.farmacia.negocio.excecao.produto.ProdutoNaoEncontradoException;

import java.util.List;
import java.util.Scanner;

public class TelaGestaoDeProdutos {
    private final Scanner sc;
    private final TelaCadastroProduto telaCadastroProduto;
    private final TelaCadastroLote telaCadastroLote;
    private final TelaConsultaProduto telaConsultaProduto;

    public TelaGestaoDeProdutos(Scanner sc, TelaCadastroProduto telaCadastroProduto, TelaCadastroLote telaCadastroLote, TelaConsultaProduto telaConsultaProduto) {
        this.sc = sc;
        this.telaCadastroProduto = telaCadastroProduto;
        this.telaCadastroLote = telaCadastroLote;
        this.telaConsultaProduto = telaConsultaProduto;
    }

    /**
     * Executa a tela de gestão de produtos.
     */
    public void executar(FachadaFarmacia fachada, Supervisor supervisorLogado) {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n--- Gestão de Produtos e Estoque ---");
            System.out.println("1. Adicionar Novo Produto ao Catálogo");
            System.out.println("2. Remover Produto do Catálogo");
            System.out.println("3. Consultar Detalhes de um Produto");
            System.out.println("4. Atualizar Dados de Produto");
            System.out.println("5. Ver Alertas de Estoque e Vencimento");
            System.out.println("6. Adicionar Lote no Estoque");
            System.out.println("7. Ajustar Estoque de um Lote Específico");
            System.out.println("8. Listar Produtos");

            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    telaCadastroProduto.executar(fachada, supervisorLogado);
                    break;
                case 2:
                    removerProduto(fachada, supervisorLogado);
                    break;
                case 3:
                    telaConsultaProduto.executar(fachada);
                    break;
                case 4:
                    atualizarProduto(fachada, supervisorLogado);
                    break;
                case 5:
                    verAlertas(fachada, supervisorLogado);
                    break;
                case 6:
                    telaCadastroLote.executar(fachada, supervisorLogado);
                    break;
                case 7:
                    ajustarEstoqueLote(fachada, supervisorLogado);
                    break;
                case 8:
                    listar(fachada);
                case 0:
                    sair = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void listar(FachadaFarmacia fachada) {
        System.out.println("\n--- Lista de Produtos ---");
        List<Produto> produtos = fachada.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            for (Produto produto : produtos) {
                System.out.println(produto.toString());
            }
        }
    }

    private void atualizarProduto(FachadaFarmacia fachada, Supervisor supervisorLogado) {
        System.out.println("--- Atualizar Dados de Produto ---");
        try {
            System.out.print("Digite o código do produto a ser atualizado: ");
            String codigo = sc.nextLine();
            Produto p = fachada.buscarProdutoPorCodigo(codigo);

            System.out.print("Novo Nome (Atual: " + p.getNome() + "): "); String nome = sc.nextLine();
            System.out.print("Novo Fabricante (Atual: " + p.getFabricante() + "): "); String fabricante = sc.nextLine();
            System.out.print("Novo Preço (Atual: " + p.getPreco() + "): "); double preco = sc.nextDouble(); sc.nextLine();
            System.out.print("Novo Estoque Mínimo (Atual: " + p.getEstoqueMinimo() + "): "); int estMin = sc.nextInt(); sc.nextLine();

            fachada.atualizarDadosProduto(p.getId(), nome, fabricante, preco, estMin, supervisorLogado);
            System.out.println("\nPRODUTO ATUALIZADO COM SUCESSO!");
        } catch (ProdutoNaoEncontradoException | DadosInvalidosException e) {
            System.err.println("\nERRO AO ATUALIZAR: " + e.getMessage());
        }
    }

    private void removerProduto(FachadaFarmacia fachada, Supervisor supervisorLogado) {
        System.out.println("--- Remover Produto ---");
        try {
            System.out.print("Digite o código do produto a ser removido: ");
            String codigo = sc.nextLine();
            sc.nextLine();
            Produto p = fachada.buscarProdutoPorCodigo(codigo);

            fachada.removerProduto(p.getId(), supervisorLogado);
            System.out.println("\nPRODUTO REMOVIDO COM SUCESSO!");
        } catch (ProdutoNaoEncontradoException | ProdutoComEstoqueException e) {
            System.err.println("\nERRO AO REMOVER: " + e.getMessage());
        }
    }

    private void ajustarEstoqueLote(FachadaFarmacia fachada, Supervisor supervisorLogado) {
        System.out.println("\n--- Ajuste Manual de Estoque de Lote ---");
        try {
            System.out.print("Primeiro, digite o código do produto para ver seus lotes: ");
            String codigo = sc.nextLine();
            sc.nextLine();
            Produto p = fachada.buscarProdutoPorCodigo(codigo);

            String detalhes = fachada.getStatusDetalhadoProduto(p.getId());
            System.out.println(detalhes);

            System.out.print("Agora, digite o ID do Lote que deseja ajustar: ");
            int idLote = sc.nextInt();
            sc.nextLine();

            System.out.print("Digite a NOVA quantidade de itens para este lote: ");
            int novaQuantidade = sc.nextInt();
            sc.nextLine();

            System.out.print("Tem certeza que deseja alterar a quantidade do Lote " + idLote +
                    " para " + novaQuantidade + " unidades? (S/N): ");
            String confirmacao = sc.nextLine();

            if ("S".equalsIgnoreCase(confirmacao)) {
                fachada.ajustarQuantidadeLote(idLote, novaQuantidade, supervisorLogado);
                System.out.println("\nEstoque do lote ajustado com sucesso!");
            } else {
                System.out.println("\nOperação cancelada.");
            }

        } catch (ProdutoNaoEncontradoException | LoteNaoEncontradoException | DadosInvalidosException e) {
            System.err.println("\nERRO: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\nERRO: Entrada de dados inválida. Tente novamente.");
            if (sc.hasNextLine()) sc.nextLine();
        }
    }

    private void verAlertas(FachadaFarmacia fachada, Supervisor supervisorLogado) {
        System.out.println("\n--- Alertas de Estoque e Vencimento ---");
        List<String> alertas = fachada.verificarAlertasDeEstoque(supervisorLogado);
        if (alertas.isEmpty()) {
            System.out.println("Nenhum alerta no momento.");
        } else {
            for (String alerta : alertas) {
                System.out.println("-> " + alerta);
            }
        }
    }
}