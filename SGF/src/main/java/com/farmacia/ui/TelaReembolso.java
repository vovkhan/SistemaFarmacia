package com.farmacia.ui;


import com.farmacia.fachada.FachadaFarmacia;
import com.farmacia.negocio.entidade.*;
import com.farmacia.negocio.excecao.produto.ProdutoNaoEncontradoException;
import com.farmacia.negocio.excecao.venda.VendaException;

import java.util.ArrayList;
import java.util.Scanner;

public class TelaReembolso {
    private final Scanner scanner;
    public TelaReembolso(Scanner scanner) { this.scanner = scanner; }

    /**
     * Executa o processo de reembolso.
     */
    public void executar(FachadaFarmacia fachada, Atendente atendenteLogado) {
        System.out.println("\n--- PROCESSAR REEMBOLSO ---");
        try {
            System.out.print("Digite o código da venda original (ex: V-2025-1): ");
            String codigoVenda = scanner.nextLine();
            Venda vendaOriginal = fachada.buscarVendaPorCodigo(codigoVenda);

            System.out.println("--- Itens da Venda Original ---");
            for (ItemVenda item : vendaOriginal.getItens()) {
                System.out.println(item.toString());
            }
            System.out.println("-----------------------------");

            ArrayList<ItemVenda> itensAReembolsar = new ArrayList<>();
            while (true) {
                System.out.print("Digite o código do produto a reembolsar (ou 'fim'): ");
                String codigoProduto = scanner.nextLine();
                if ("fim".equalsIgnoreCase(codigoProduto)) break;

                Produto produto = fachada.buscarProdutoPorCodigo(codigoProduto);
                System.out.print("Digite a quantidade a reembolsar: ");
                int qtd = scanner.nextInt(); scanner.nextLine();

                itensAReembolsar.add(new ItemVenda(produto, qtd, 0)); // Preço não importa aqui, o serviço buscará o original
            }

            if (itensAReembolsar.isEmpty()) {
                System.out.println("Nenhum item selecionado. Operação cancelada.");
                return;
            }

            String reciboReembolso = fachada.processarReembolso(atendenteLogado, vendaOriginal, itensAReembolsar);
            System.out.println("\n--- Reembolso Realizado com Sucesso! ---");
            System.out.println(reciboReembolso);

        } catch (VendaException | ProdutoNaoEncontradoException e) {
            System.err.println("\nERRO: " + e.getMessage());
        }
    }
}