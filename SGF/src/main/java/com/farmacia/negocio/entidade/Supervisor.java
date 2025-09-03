package com.farmacia.negocio.entidade;

public class Supervisor extends Funcionario {
    private int produtosCadastrados;
    private int lotesRecebidos;
    private int ajustesDeEstoqueRealizados;

    public Supervisor(String nome, String cpf, String login, String senha) {
        super(nome, cpf, login, senha);
        this.produtosCadastrados = 0;
        this.lotesRecebidos = 0;
        this.ajustesDeEstoqueRealizados = 0;
    }

    @Override
    public String getNomeCargo() {
        return "Supervisor";
    }

    @Override
    public String getResumoDeDesempenho() {
        return String.format(
                "Produtos Novos Cadastrados: %d | Lotes Recebidos: %d | Ajustes de Estoque: %d",
                this.produtosCadastrados,
                this.lotesRecebidos,
                this.ajustesDeEstoqueRealizados
        );
    }

    public void registrarNovoProdutoCadastrado() {
        this.produtosCadastrados++;
    }

    public void registrarNovoLoteRecebido() {
        this.lotesRecebidos++;
    }

    public void registrarAjusteDeEstoque() {
        this.ajustesDeEstoqueRealizados++;
    }

    public int getProdutosCadastrados() {
        return produtosCadastrados;
    }
    public int getLotesRecebidos() {
        return lotesRecebidos;
    }
    public int getAjustesDeEstoqueRealizados() {
        return ajustesDeEstoqueRealizados;
    }

}