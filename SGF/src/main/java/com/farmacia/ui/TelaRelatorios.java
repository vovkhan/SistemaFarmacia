package com.farmacia.ui;

import com.farmacia.fachada.FachadaFarmacia;
import com.farmacia.negocio.entidade.Gerente;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TelaRelatorios {
    private final Scanner sc;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaRelatorios(Scanner sc) {
        this.sc = sc;
    }

    public void executar(FachadaFarmacia fachada, Gerente gerenteLogado) {
        boolean sair = false;
        while(!sair) {
            System.out.println("\n--- Menu de Relatórios ---");
            System.out.println("1. Relatório de Produtos Mais Vendidos");
            System.out.println("2. Relatório de Produtos Menos Vendidos");
            System.out.println("3. Relatório de Desempenho de Funcionários");
            System.out.println("3. Relatório de Faturamento por Período");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = sc.nextInt();
            sc.nextLine();

            switch(opcao) {
                case 1:
                    relatorioMaisVendidos(fachada, gerenteLogado);
                    break;

                case 2:
                    relatorioMenosVendidos(fachada, gerenteLogado);
                    break;
                case 3:
                    relatorioDesempenho(fachada, gerenteLogado);
                    break;
                case 4:
                    relatorioFaturamento(fachada, gerenteLogado);
                    break;
                case 0:
                    sair = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void relatorioMaisVendidos(FachadaFarmacia fachada, Gerente gerenteLogado) {
        System.out.print("Deseja listar quantos produtos? ");
        int topN = sc.nextInt();
        sc.nextLine();

        List<String> relatorio = fachada.gerarRelatorioMaisVendidos(topN, gerenteLogado);
        System.out.println("\n==============================================");
        for(String linha : relatorio) {
            System.out.println(linha);
        }
        System.out.println("==============================================");
    }

    private void relatorioMenosVendidos(FachadaFarmacia fachada, Gerente gerenteLogado) {
        System.out.print("Deseja listar quantos produtos? ");
        int topN = sc.nextInt();
        sc.nextLine();

        List<String> relatorio = fachada.gerarRelatorioMenosVendidos(topN, gerenteLogado);
        System.out.println("\n==============================================");
        for(String linha : relatorio) {
            System.out.println(linha);
        }
        System.out.println("==============================================");
    }

    private void relatorioFaturamento(FachadaFarmacia fachada, Gerente gerenteLogado) {
        try {
            LocalDate inicio = lerDataDoUsuario("Digite a data de início (dd/mm/aaaa): ");
            LocalDate fim = lerDataDoUsuario("Digite a data de fim (dd/mm/aaaa): ");

            String relatorio = fachada.gerarRelatorioFaturamento(inicio, fim, gerenteLogado);
            System.out.println("\n" + relatorio);
        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    private void relatorioDesempenho(FachadaFarmacia fachada, Gerente gerenteLogado) {
        List<String> relatorio = fachada.gerarRelatorioDesempenho(gerenteLogado);
        System.out.println("\n==============================================");
        for(String linha : relatorio) {
            System.out.println(linha);
        }
        System.out.println("==============================================");
    }

    private LocalDate lerDataDoUsuario(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return LocalDate.parse(sc.nextLine(), formatter);
            } catch (java.time.format.DateTimeParseException e) {
                System.err.println("Formato de data inválido! Por favor, use o formato dd/mm/aaaa e tente novamente.");
            }
        }
    }
}