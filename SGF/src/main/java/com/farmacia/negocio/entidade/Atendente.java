package com.farmacia.negocio.entidade;

public class Atendente extends Funcionario {
    private int totalVendasRealizadas;
    private double valorTotalVendido;
    private int clientesCadastrados;

    public Atendente(String nome, String cpf, String matricula, String login, String senha) {
        super(nome, cpf, matricula, login, senha);
        this.totalVendasRealizadas = 0;
        this.valorTotalVendido = 0;
        this.clientesCadastrados = 0;
    }

    @Override
    public String getNomeCargo() {
        return "Atendente de Vendas";
    }

    //O resto dos métodos de atendente de vendas

}
