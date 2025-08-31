package com.farmacia.ui;

import com.farmacia.fachada.FachadaFarmacia;
import com.farmacia.negocio.entidade.Atendente;
import com.farmacia.negocio.entidade.Cliente;
import com.farmacia.negocio.entidade.Venda;
import com.farmacia.negocio.excecao.cliente.ClienteNaoEncontradoException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TelaConsultaVendas {

    private final Scanner sc;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaConsultaVendas(Scanner sc) {
        this.sc = sc;
    }

    public void executar(FachadaFarmacia fachada, Atendente atendenteLogado) {
        System.out.println("\n--- Consultar Histórico de Vendas ---");

        try {
            LocalDate dataInicio = null;
            LocalDate dataFim = null;
            Cliente cliente = null;

            System.out.print("Deseja filtrar por um período de data? (S/N): ");
            if ("S".equalsIgnoreCase(sc.nextLine())) {
                dataInicio = lerDataDoUsuario("Digite a data de início (dd/mm/aaaa): ");
                dataFim = lerDataDoUsuario("Digite a data de fim (dd/mm/aaaa): ");
                if (dataFim.isBefore(dataInicio)) {
                    System.err.println("A data final não pode ser anterior à data de início. Abortando consulta.");
                    return;
                }
            }

            System.out.print("Deseja filtrar por um cliente específico? (S/N): ");
            if ("S".equalsIgnoreCase(sc.nextLine())) {
                System.out.print("Digite o CPF do cliente: ");
                String cpf = sc.nextLine();
                try {
                    cliente = fachada.buscarClientePorCpf(cpf);
                    System.out.println("Filtrando por cliente: " + cliente.getNome());
                } catch (ClienteNaoEncontradoException e) {
                    System.err.println("AVISO: " + e.getMessage() + ". A busca continuará sem filtro de cliente.");
                }
            }
            System.out.println("\nBuscando vendas...");
            List<Venda> vendasEncontradas = fachada.consultarVendas(dataInicio, dataFim, cliente);

            if (vendasEncontradas.isEmpty()) {
                System.out.println("Nenhuma venda encontrada com os filtros especificados.");
            } else {
                System.out.println("\n--- Vendas Encontradas (" + vendasEncontradas.size() + ") ---");
                for (Venda venda : vendasEncontradas) {
                    System.out.println("----------------------------------------");
                    String tipo = venda.isEhReembolso() ? "REEMBOLSO" : "VENDA";
                    System.out.printf("[%s] Código: %s | Data: %s\n",
                            tipo, venda.getCodigo(), venda.getDataHora().format(formatter));
                    System.out.printf("Cliente: %s | Vendedor: %s\n",
                            venda.getCliente().getNome(), venda.getVendedor().getNome());
                    System.out.printf("Valor Total: R$ %.2f\n", Math.abs(venda.getValorTotal()));
                }
                System.out.println("----------------------------------------");
            }

        } catch (Exception e) {
            System.err.println("Ocorreu um erro durante a consulta: " + e.getMessage());
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
