package com.farmacia.negocio.entidade;

import com.farmacia.negocio.excecao.DadosInvalidosException;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe abstrata que representa um item de catálogo da farmácia.
 * Contém informações comuns a todos os produtos, independentemente do lote.
 */
public abstract class Produto implements Serializable {

    private int id;
    private String codigo;
    private String nome;
    private double preco;
    private String fabricante;
    private int estoqueMinimo;

    public Produto(String nome, double preco, String fabricante, int estoqueMinimo) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DadosInvalidosException("Nome do produto não pode ser vazio.");
        }
        if (preco < 0) {
            throw new DadosInvalidosException("Preço do produto não pode ser negativo.");
        }
        this.nome = nome;
        this.preco = preco;
        this.fabricante = fabricante;
        this.estoqueMinimo = estoqueMinimo;
    }

    /**
     * Contrato que obriga as subclasses a calcularem os pontos gerados.
     */
    public abstract int calcularPontosGerados(int quantidadeVendida);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DadosInvalidosException("O nome do produto não pode ser vazio.");
        }
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new DadosInvalidosException("O preço do produto не pode ser negativo.");
        }
        this.preco = preco;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        if (fabricante == null || fabricante.trim().isEmpty()) {
            throw new DadosInvalidosException("O fabricante do produto não pode ser vazio.");
        }
        this.fabricante = fabricante;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        if (estoqueMinimo < 0) {
            throw new DadosInvalidosException("O estoque mínimo não pode ser negativo.");
        }
        this.estoqueMinimo = estoqueMinimo;
    }

    @Override
    public String toString() {
        return "Produto ID = " + id + " | Codigo = " + codigo + " | Nome = " + nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        if (id == 0 || produto.id == 0) return this == o;
        return id == produto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}