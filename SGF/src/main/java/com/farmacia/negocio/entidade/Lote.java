package com.farmacia.negocio.entidade;

import com.farmacia.negocio.excecao.DadosInvalidosException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa um lote específico de um produto no estoque.
 * É a unidade fundamental para o controle de inventário, quantidade e validade.
 */
public class Lote implements Serializable {

    private int id;
    private final Produto produto;
    private int quantidade;
    private final LocalDate dataValidade;
    private final LocalDate dataDeEntrada;

    public Lote(Produto produto, int quantidade, LocalDate dataValidade) {
        if (produto == null) {
            throw new DadosInvalidosException("O produto de um lote não pode ser nulo.");
        }
        if (quantidade <= 0) {
            throw new DadosInvalidosException("A quantidade inicial de um lote deve ser positiva.");
        }
        if (dataValidade == null || dataValidade.isBefore(LocalDate.now())) {
            throw new DadosInvalidosException("A data de validade não pode ser nula ou anterior a de hoje.");
        }

        this.produto = produto;
        this.quantidade = quantidade;
        this.dataValidade = dataValidade;
        this.dataDeEntrada = LocalDate.now();
    }

    public void darBaixa(int qtdParaRemover) {
        if (qtdParaRemover <= 0) {
            throw new IllegalArgumentException("A quantidade para dar baixa deve ser positiva.");
        }
        if (qtdParaRemover > this.quantidade) {
            throw new IllegalStateException("Não há estoque suficiente neste lote para dar baixa.");
        }
        this.quantidade -= qtdParaRemover;
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

    public void setQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa.");
        }
        this.quantidade = quantidade;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public LocalDate getDataDeEntrada() {
        return dataDeEntrada;
    }

    @Override
    public String toString() {
        return "Lote [ID=" + id + ", Produto='" + produto.getNome() + "', Qtd=" + quantidade + ", Val=" + dataValidade + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lote lote = (Lote) o;
        return id == lote.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}