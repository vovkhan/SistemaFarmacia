package com.farmacia.ui;

import com.farmacia.fachada.FachadaFarmacia;
import com.farmacia.negocio.entidade.*;
import com.farmacia.negocio.excecao.cliente.ClienteNaoEncontradoException;
import com.farmacia.negocio.excecao.estoque.EstoqueInsuficienteException;
import com.farmacia.negocio.excecao.produto.ProdutoNaoEncontradoException;
import com.farmacia.negocio.excecao.venda.CarrinhoVazioException;
import com.farmacia.negocio.excecao.venda.PontosInsuficientesException;

import java.util.ArrayList;
import java.util.Scanner;

public class TelaVenda {
    private final Scanner sc;
    private final TelaCadastroCliente telaCadastroCliente;

    public TelaVenda(Scanner sc, TelaCadastroCliente telaCadastroCliente) {
        this.sc = sc;
        this.telaCadastroCliente = telaCadastroCliente;
    }

    /**
     * Executa uma nova venda.
     */
    public void executar(FachadaFarmacia fachada, Atendente atendenteLogado) {
        System.out.println("\n--- NOVA VENDA ---");

        System.out.print("Digite o CPF do cliente (ou deixe em branco para venda anônima): ");
        String cpf = sc.nextLine();
        Cliente cliente;

        if (cpf.trim().isEmpty()) {
            cliente = Cliente.CLIENTE_NAO_CADASTRADO;
        } else {
            try {
                cliente = fachada.buscarClientePorCpf(cpf);
            } catch (ClienteNaoEncontradoException e) {
                System.out.println("Cliente com CPF " + cpf + " não encontrado.");
                System.out.print("Deseja cadastrar um novo cliente agora? (S/N): ");
                String resposta = sc.nextLine();

                if ("S".equalsIgnoreCase(resposta)) {
                    Cliente novoCliente = telaCadastroCliente.cadastrar(fachada, atendenteLogado);

                    if (novoCliente != null) {
                        cliente = novoCliente;
                    } else {
                        System.out.println("Cadastro falhou. Continuando a venda como 'Cliente Não Identificado'.");
                        cliente = Cliente.CLIENTE_NAO_CADASTRADO;
                    }
                } else {
                    cliente = Cliente.CLIENTE_NAO_CADASTRADO;
                }
            }
        }
        System.out.println("Iniciando venda para o cliente: " + cliente.getNome());

        //Carrinho
        ArrayList<ItemVenda> carrinho = new ArrayList<>();
        while (true) {
            System.out.print("Digite o código do produto (ou 'fim' para finalizar): ");
            String codigo = sc.nextLine();
            if ("fim".equalsIgnoreCase(codigo)) break;

            try {
                Produto produto = fachada.buscarProdutoPorCodigo(codigo);
                System.out.print("Digite a quantidade: ");
                int qtd = sc.nextInt();
                sc.nextLine();

                carrinho.add(new ItemVenda(produto, qtd, produto.getPreco()));
                System.out.println("'" + produto.getNome() + "' adicionado ao carrinho.");
            } catch (ProdutoNaoEncontradoException e) {
                System.err.println("ERRO: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Entrada inválida.");
            }
        }

        int pontosParaUsar = 0;
        if (cliente.isCadastrado() && cliente.getPontosFidelidade() > 0) {
            System.out.println("O cliente possui " + cliente.getPontosFidelidade() + " pontos.");
            System.out.print("Quantos pontos deseja usar nesta compra? (0 para nenhum): ");
            pontosParaUsar = sc.nextInt();
            sc.nextLine();
        }

        try {
            String recibo = fachada.processarVenda(atendenteLogado, cliente, carrinho, pontosParaUsar);
            System.out.println("\n--- Venda Finalizada com Sucesso! ---");
            System.out.println(recibo);
        } catch (EstoqueInsuficienteException | PontosInsuficientesException | CarrinhoVazioException e) {
            System.err.println("\nERRO AO PROCESSAR VENDA: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("\nERRO INESPERADO: " + e.getMessage());
        }
    }
}