package com.farmacia.ui;

import com.farmacia.fachada.FachadaFarmacia;
import com.farmacia.negocio.entidade.*;
import com.farmacia.negocio.excecao.AcessoNegadoException;
import com.farmacia.negocio.excecao.funcionario.AutenticacaoException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaPrincipal {
    private final FachadaFarmacia fachada;
    private final Scanner sc;

    private final TelaVenda telaVenda;
    private final TelaCadastroCliente telaCadastroCliente;
    private final TelaGestaoDeProdutos telaGestaoDeProdutos;
    private final TelaCadastroProduto telaCadastroProduto;
    private final TelaRelatorios telaRelatorios;
    private final TelaReembolso telaReembolso;

    public TelaPrincipal() {
        this.fachada = new FachadaFarmacia();
        this.sc = new Scanner(System.in);

        this.telaCadastroCliente = new TelaCadastroCliente(sc);
        this.telaVenda = new TelaVenda(sc, telaCadastroCliente);
        this.telaCadastroProduto = new TelaCadastroProduto(sc);
        this.telaGestaoDeProdutos = new TelaGestaoDeProdutos(sc, telaCadastroProduto);
        this.telaRelatorios = new TelaRelatorios(sc);
        this.telaReembolso = new TelaReembolso(sc);
    }

    public void iniciar() {
        System.out.println("--- BEM-VINDO AO SISTEMA DE GESTÃO DE FARMÁCIA ---");

        // 1. LÓGICA DE LOGIN
        Funcionario funcionarioLogado = null;
        while (funcionarioLogado == null) {
            try {
                System.out.print("Login: ");
                String login = sc.nextLine();
                System.out.print("Senha: ");
                String senha = sc.nextLine();
                funcionarioLogado = fachada.login(login, senha);
                System.out.println("\nLogin bem-sucedido! Bem-vindo(a), " + funcionarioLogado.getNome());
            } catch (AutenticacaoException e) {
                System.err.println("ERRO: " + e.getMessage() + "\n");
            }
        }

        if (funcionarioLogado instanceof Atendente) {
            menuAtendente((Atendente) funcionarioLogado);
        } else if (funcionarioLogado instanceof Supervisor) {
            menuSupervisor((Supervisor) funcionarioLogado);
        } else if (funcionarioLogado instanceof Gerente) {
            menuGerente((Gerente) funcionarioLogado);
        }

        System.out.println("\n--- FIM DA SESSÃO ---");
    }

    private void menuAtendente(Atendente atendente) {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n--- Menu do Atendente ---");
            System.out.println("1. Processar Nova Venda");
            System.out.println("2. Gestão de Clientes");
            System.out.println("3. Processar Reembolso");
            System.out.println("0. Sair (Logout)");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = sc.nextInt();
                sc.nextLine();

                switch (opcao) {
                    case 1:
                        telaVenda.executar(fachada, atendente);
                        break;
                    case 2:
                        telaCadastroCliente.executar(fachada, atendente);
                        break;
                    case 3:
                        telaReembolso.executar(fachada, atendente);
                        break;
                    case 0:
                        sair = true;
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (InputMismatchException e) {
                System.err.println("Erro: Por favor, digite um número.");
                sc.nextLine();
            }
        }
    }

    private void menuSupervisor(Supervisor supervisor) {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n--- Menu do Supervisor ---");
            System.out.println("--- (Acesso de Estoque) ---");
            System.out.println("1. Gestão de Produtos e Estoque");
            System.out.println("0. Sair (Logout)");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = sc.nextInt();
                sc.nextLine();

                switch (opcao) {
                    case 1:
                        telaGestaoDeProdutos.executar(fachada, supervisor);
                        break;
                    case 0:
                        sair = true;
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (InputMismatchException e) {
                System.err.println("Erro: Por favor, digite um número.");
                sc.nextLine();
            }
        }
    }

    private void menuGerente(Gerente gerente) {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n--- Menu do Gerente ---");
            System.out.println("--- (Acesso Gerencial) ---");
            System.out.println("1. Ver Relatórios");
            System.out.println("0. Sair (Logout)");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = sc.nextInt();
                sc.nextLine();

                switch (opcao) {
                    case 1:
                        telaRelatorios.executar(fachada, gerente);
                        break;
                    case 0:
                        sair = true;
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (InputMismatchException | AcessoNegadoException e) {
                System.err.println("Erro: " + e.getMessage());
                if (sc.hasNextLine()) sc.nextLine();
            }
        }
    }
}