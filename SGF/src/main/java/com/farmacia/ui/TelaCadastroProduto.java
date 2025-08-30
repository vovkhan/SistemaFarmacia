// Localização: src/com/farmacia/ui/TelaCadastroProduto.java
package com.farmacia.ui;

import com.farmacia.fachada.FachadaFarmacia;
import com.farmacia.negocio.entidade.*;
import com.farmacia.negocio.excecao.DadosInvalidosException;
import com.farmacia.negocio.excecao.produto.ProdutoDuplicadoException;
import java.util.Scanner;

public class TelaCadastroProduto {

    private final Scanner sc;

    public TelaCadastroProduto(Scanner sc) {
        this.sc = sc;
    }

    /**
     * Executa o cadastro de um novo produto
     */
    public void executar(FachadaFarmacia fachada, Supervisor supervisorLogado) {
        System.out.println("\n--- Adicionar Novo Produto ao Catálogo ---");
        try {
            System.out.print("Nome: "); String nome = sc.nextLine();
            System.out.print("Preço (ex: 25,50): "); double preco = sc.nextDouble(); sc.nextLine();
            System.out.print("Fabricante: "); String fabricante = sc.nextLine();
            System.out.print("Estoque Mínimo: "); int estMin = sc.nextInt(); sc.nextLine();

            System.out.println("Qual o tipo de produto?");
            System.out.println("1. Medicamento");
            System.out.println("2. Higiene");
            System.out.println("3. Cosmético");
            System.out.println("4. Conveniência");
            System.out.print("Opção: ");
            int tipo = sc.nextInt(); sc.nextLine();

            Produto novoProduto = null;

            switch (tipo) {
                case 1:
                    System.out.print("Necessita Receita? (true/false): "); boolean receita = sc.nextBoolean(); sc.nextLine();
                    System.out.print("Tarja (Sem Tarja, Amarela, Vermelha, Preta): "); String tarja = sc.nextLine();
                    novoProduto = new Medicamento(nome, preco, fabricante, estMin, receita, tarja);
                    break;
                case 2:
                    System.out.print("É infantil? (true/false): "); boolean infantil = sc.nextBoolean(); sc.nextLine();
                    novoProduto = new Higiene(nome, preco, fabricante, estMin, infantil);
                    break;
                case 3:
                    System.out.print("Tipo de Uso (Facial, Corporal, Capilar, Unhas, Maquiagem): "); String tipoUso = sc.nextLine();
                    novoProduto = new Cosmetico(nome, preco, fabricante, estMin, tipoUso);
                    break;
                case 4:
                    System.out.print("É um alimento? (true/false): "); boolean alimento = sc.nextBoolean(); sc.nextLine();
                    novoProduto = new Conveniencia(nome, preco, fabricante, estMin, alimento);
                    break;
                default:
                    System.out.println("Tipo de produto inválido. Operação cancelada.");
                    return;
            }
            fachada.adicionarNovoProduto(novoProduto, supervisorLogado);
            System.out.println("\nPRODUTO ADICIONADO COM SUCESSO! CÓDIGO GERADO: " + novoProduto.getCodigo());

        } catch (DadosInvalidosException | ProdutoDuplicadoException e) {
            System.err.println("\nERRO AO ADICIONAR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\nERRO: Entrada de dados inválida. Tente novamente.");
            if (sc.hasNextLine()) sc.nextLine();
        }
    }
}