package com.farmacia.negocio.entidade;

public class Atendente extends Funcionario {

    private int totalVendasRealizadas;
    private double valorTotalVendido;
    private int clientesCadastrados;

    public Atendente(String nome, String cpf, String matricula, String login, String senha) {
        super(nome, cpf, matricula, login, senha);
        this.totalVendasRealizadas = 0;
        this.valorTotalVendido = 0.0;
        this.clientesCadastrados = 0;
    }

    @Override
    public String getNomeCargo() {
        return "Atendente de Vendas";
    }

    @Override
    public String getResumoDeDesempenho() {
        return String.format(
                "Vendas Realizadas: %d | Valor Total Vendido: R$ %.2f | Clientes Novos: %d",
                this.totalVendasRealizadas,
                this.valorTotalVendido,
                this.clientesCadastrados
        );
    }

    /**
     * Atualiza as métricas de desempenho do atendente após uma venda.
     */
    public void registrarNovaVenda(double valorDaVenda) {
        this.totalVendasRealizadas++;
        this.valorTotalVendido += valorDaVenda;
    }

    public void registrarNovoCliente() {
        this.clientesCadastrados++;
    }

    public int getTotalVendasRealizadas() {
        return totalVendasRealizadas;
    }
    public double getValorTotalVendido() {
        return valorTotalVendido;
    }
    public int getClientesCadastrados() {
        return clientesCadastrados;
    }
}