package com.farmacia.ui;

import com.farmacia.fachada.FachadaFarmacia;
import com.farmacia.negocio.entidade.*;
import com.farmacia.negocio.excecao.*;
import com.farmacia.negocio.excecao.produto.ProdutoComEstoqueException;
import com.farmacia.negocio.excecao.produto.ProdutoNaoEncontradoException;

import java.util.List;
import java.util.Scanner;

public class TelaGestaoDeProdutos {
    private final Scanner sc;
    private final TelaCadastroProduto telaCadastroProduto;

    public TelaGestaoDeProdutos(Scanner sc, TelaCadastroProduto telaCadastroProduto) {
        this.sc = sc;
        this.telaCadastroProduto = telaCadastroProduto;
    }

    public void executar(FachadaFarmacia fachada, Supervisor supervisorLogado) {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n--- Gestão de Produtos e Estoque ---");
            System.out.println("1. Adicionar Novo Produto ao Catálogo");
            System.out.println("2. Remover Produto do Catálogo");
            System.out.println("3. Consultar Detalhes de um Produto");
            System.out.println("4. Ver Alertas de Estoque e Vencimento");
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
                    consultarProduto(fachada);
                    break;
                case 4:
                    verAlertas(fachada, supervisorLogado);
                    break;
                case 0:
                    sair = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
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

    private void removerProduto(FachadaFarmacia facade, Supervisor supervisorLogado) {
        System.out.println("--- Remover Produto ---");
        try {
            System.out.print("Digite o ID do produto a ser removido: ");
            int id = sc.nextInt();
            sc.nextLine();
            facade.removerProduto(id, supervisorLogado);
            System.out.println("\nPRODUTO REMOVIDO COM SUCESSO!");
        } catch (ProdutoNaoEncontradoException | ProdutoComEstoqueException e) {
            System.err.println("\nERRO AO REMOVER: " + e.getMessage());
        }
    }

    private void consultarProduto(FachadaFarmacia fachada) {
        System.out.println("--- Consultar Produto Detalhado ---");
        try {
            System.out.print("Digite o ID do produto: ");
            int id = sc.nextInt();
            sc.nextLine();
            String detalhes = fachada.getStatusDetalhadoProduto(id);
            System.out.println(detalhes);
        } catch (ProdutoNaoEncontradoException e) {
            System.err.println("\nERRO: " + e.getMessage());
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