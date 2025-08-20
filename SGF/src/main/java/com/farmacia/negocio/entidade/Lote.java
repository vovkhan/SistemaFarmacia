package com.farmacia.negocio.entidade;

import java.time.LocalDate;

public class Lote {
    private int id;
    private final Produto produto;
    private int quantidade;
    private final LocalDate dataValidade;
    private final LocalDate dataDeEntrada;

    public Lote(Produto produto, int quantidade, LocalDate dataValidade) {
        if (produto == null) {
            //Lote tem que ter produto
        }
        if (quantidade <= 0) {
            //Quantidade do produto no lote não pode ser 0
        }
        if (dataValidade == null || dataValidade.isBefore(LocalDate.now())) {
            //Data de validade não informada ou anterior a de agora
        }

        this.produto = produto;
        this.quantidade = quantidade;
        this.dataValidade = dataValidade;
        this.dataDeEntrada = LocalDate.now();
    }

    public void darBaixa(int qtdParaRemover) {
        if (qtdParaRemover <= 0) {
            //Erro Quandidade pra dar baixa negativa ou nula
        }
        if (qtdParaRemover > this.quantidade) {
            //Erro não há estoque o suficiente no lote pra realizar essa operação
        }
        this.quantidade -= qtdParaRemover;
    }

    public void adicionar(int qtdParaAdicionar) {
        if (qtdParaAdicionar <= 0) {
            //Erro Quandidade pra adicionar negativa ou nula
        }
        this.quantidade += qtdParaAdicionar;
    }

    public boolean estaVencido() {
        return LocalDate.now().isAfter(this.dataValidade);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public LocalDate getDataDeEntrada() {
        return dataDeEntrada;
    }

    @Override
    public String toString() {
        return "Lote ID: " + id +
                " | Produto: '" + produto.getNome() + '\'' +
                " | Quantidade: " + quantidade +
                " | Validade: " + dataValidade;
    }
}