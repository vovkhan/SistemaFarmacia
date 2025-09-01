package com.farmacia.ui;

import com.farmacia.fachada.FachadaFarmacia;
import com.farmacia.negocio.entidade.Produto;
import com.farmacia.negocio.excecao.produto.ProdutoNaoEncontradoException;

import java.util.Scanner;

public class TelaConsultaProduto {

    private final Scanner sc;

    public TelaConsultaProduto(Scanner sc) {
        this.sc = sc;
    }

    /**
     * Executa a consulta de produto.
     */
    public void executar(FachadaFarmacia fachada) {
        System.out.println("\n--- Consultar Produto Detalhado ---");
        try {
            System.out.print("Digite o código ou ID do produto: ");
            String entrada = sc.nextLine();

            Produto produtoEncontrado = null;

            try {
                int idProduto = Integer.parseInt(entrada);
                produtoEncontrado = fachada.buscarProdutoPorId(idProduto);
            } catch (NumberFormatException e) {
                produtoEncontrado = fachada.buscarProdutoPorCodigo(entrada);
            }

            if (produtoEncontrado == null) {
                throw new ProdutoNaoEncontradoException("Produto com ID ou Código '" + entrada + "' não foi encontrado.");
            }
            String detalhes = fachada.getStatusDetalhadoProduto(produtoEncontrado.getId());
            System.out.println(detalhes);

        } catch (ProdutoNaoEncontradoException e) {
            System.err.println("\nERRO: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\nERRO INESPERADO: " + e.getMessage());
        }
    }
}