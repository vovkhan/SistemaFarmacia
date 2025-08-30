package com.farmacia.ui;

import com.farmacia.fachada.FachadaFarmacia;
import com.farmacia.negocio.entidade.Atendente;
import com.farmacia.negocio.entidade.Cliente;
import com.farmacia.negocio.excecao.*;
import com.farmacia.negocio.excecao.cliente.ClienteNaoEncontradoException;
import com.farmacia.negocio.excecao.cliente.CpfJaCadastradoException;

import java.util.List;
import java.util.Scanner;

public class TelaCadastroCliente {

    private final Scanner sc;

    public TelaCadastroCliente(Scanner sc) {
        this.sc = sc;
    }

    public void executar(FachadaFarmacia fachada, Atendente atendenteLogado) {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n--- Gestão de Clientes ---");
            System.out.println("1. Cadastrar Novo Cliente");
            System.out.println("2. Atualizar Dados de Cliente");
            System.out.println("3. Listar Clientes Ativos");
            System.out.println("4. Remover Cliente");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    cadastrar(fachada, atendenteLogado);
                    break;
                case 2:
                    atualizar(fachada, atendenteLogado);
                    break;
                case 3:
                    listar(fachada);
                    break;
                case 4:
                    remover(fachada, atendenteLogado);
                    break;
                case 0:
                    sair = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    public Cliente cadastrar(FachadaFarmacia fachada, Atendente atendenteLogado) {
        System.out.println("--- Cadastro de Novo Cliente ---");
        try {
            System.out.print("Nome: ");
            String nome = sc.nextLine();
            System.out.print("CPF (apenas números): ");
            String cpf = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Telefone: ");
            String telefone = sc.nextLine();

            Cliente novoCliente = new Cliente(nome, cpf, email, telefone);
            fachada.cadastrarNovoCliente(novoCliente, atendenteLogado);
            System.out.println("\nCLIENTE CADASTRADO COM SUCESSO!");

            // Retorna o objeto do cliente recém-criado
            return novoCliente;

        } catch (DadosInvalidosException | CpfJaCadastradoException e) {
            System.err.println("\nERRO DE CADASTRO: " + e.getMessage());
            return null;
        }
    }

    private void atualizar(FachadaFarmacia fachada, Atendente atendenteLogado) {
        System.out.println("--- Atualizar Dados de Cliente ---");
        try {
            System.out.print("Digite o CPF do cliente que deseja atualizar: ");
            String cpfBusca = sc.nextLine();
            Cliente cliente = fachada.buscarClientePorCpf(cpfBusca);

            System.out.println("Editando cliente: " + cliente.getNome());
            System.out.print("Novo Nome (" + cliente.getNome() + "): ");
            String novoNome = sc.nextLine();
            System.out.print("Novo CPF (" + cliente.getCpf() + "): ");
            String novoCpf = sc.nextLine();
            System.out.print("Novo Email (" + cliente.getEmail() + "): ");
            String novoEmail = sc.nextLine();
            System.out.print("Novo Telefone (" + cliente.getTelefone() + "): ");
            String novoTelefone = sc.nextLine();

            fachada.atualizarDadosCliente(cliente.getId(), novoNome, novoCpf, novoEmail, novoTelefone, atendenteLogado);
            System.out.println("\nCLIENTE ATUALIZADO COM SUCESSO!");
        } catch (ClienteNaoEncontradoException | CpfJaCadastradoException | DadosInvalidosException e) {
            System.err.println("\nERRO AO ATUALIZAR: " + e.getMessage());
        }
    }

    private void listar(FachadaFarmacia fachada) {
        System.out.println("\n--- Lista de Clientes Ativos ---");
        List<Cliente> clientes = fachada.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            for (Cliente cliente : clientes) {
                System.out.println(cliente.toString());
            }
        }
    }

    private void remover(FachadaFarmacia fachada, Atendente atendenteLogado) {
        System.out.println("--- Remover Cliente ---");
        try {
            System.out.print("Digite o CPF do cliente que deseja remover: ");
            String cpfBusca = sc.nextLine();
            Cliente cliente = fachada.buscarClientePorCpf(cpfBusca);

            System.out.print("Tem certeza que deseja remover o cliente '" + cliente.getNome() + "'? (S/N): ");
            String confirmacao = sc.nextLine();

            if ("S".equalsIgnoreCase(confirmacao)) {
                fachada.removerCliente(cliente.getId(), atendenteLogado);
                System.out.println("\nCLIENTE REMOVIDO COM SUCESSO!");
            } else {
                System.out.println("\nOperação cancelada.");
            }
        } catch (ClienteNaoEncontradoException e) {
            System.err.println("\nERRO AO REMOVER: " + e.getMessage());
        }
    }
}