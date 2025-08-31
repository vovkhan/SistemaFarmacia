package com.farmacia.ui;

import com.farmacia.fachada.FachadaFarmacia;
import com.farmacia.negocio.entidade.Produto;
import com.farmacia.negocio.entidade.Supervisor;
import com.farmacia.negocio.excecao.DadosInvalidosException;
import com.farmacia.negocio.excecao.produto.ProdutoNaoEncontradoException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class TelaCadastroLote {

    private final Scanner sc;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaCadastroLote(Scanner sc) {
        this.sc = sc;
    }

    public void executar(FachadaFarmacia fachada, Supervisor supervisorLogado) {
        System.out.println("\n--- Adicionar Lote ao Estoque (Entrada de Mercadoria) ---");
        try {
            System.out.print("Digite o código ou ID do produto para adicionar um lote: ");
            String entrada = sc.nextLine();

            Produto produtoEncontrado = null;
            try {
                int idProduto = Integer.parseInt(entrada);
                produtoEncontrado = fachada.buscarProdutoPorId(idProduto);
            } catch (NumberFormatException e) {
                produtoEncontrado = fachada.buscarProdutoPorCodigo(entrada);
            }
            if (produtoEncontrado == null) {
                throw new ProdutoNaoEncontradoException(entrada);
            }
            System.out.println("Adicionando lote para o produto: " + produtoEncontrado.getNome());

            System.out.print("Digite a quantidade de itens no lote: ");
            int quantidade = sc.nextInt();
            sc.nextLine();

            LocalDate dataValidade = lerDataDoUsuario("Digite a data de validade do lote (dd/mm/aaaa): ");
            fachada.adicionarLote(produtoEncontrado.getId(), quantidade, dataValidade, supervisorLogado);

            System.out.println("\nLOTE ADICIONADO COM SUCESSO AO ESTOQUE!");

        } catch (ProdutoNaoEncontradoException | DadosInvalidosException e) {
            System.err.println("\nERRO: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\nERRO INESPERADO: " + e.getMessage());
            if (sc.hasNextLine()) sc.nextLine();
        }
    }

    private LocalDate lerDataDoUsuario(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return LocalDate.parse(sc.nextLine(), formatter);
            } catch (java.time.format.DateTimeParseException e) {
                System.err.println("Formato de data inválido! Use dd/mm/aaaa e tente novamente.");
            }
        }
    }
}