package com.farmacia.negocio.entidade;

public abstract class Produto {
    private int id;
    private String codigo;
    private String nome;
    private double preco;
    private String fabricante;

    public Produto(String nome, double preco, String fabricante) {
        if (nome == null || nome.trim().isEmpty()) {
            //O Produto tem que ter um nome
        }
        if (preco < 0) {
            //Preço do produto não pode ser negativo
        }
        this.nome = nome;
        this.preco = preco;
        this.fabricante = fabricante;
    }

    public abstract int calcularPontosGerados(int quantidadeVendida);

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            //Preço do produto não pode ser negativo
        }
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFabricante() {
        return fabricante;
    }

    @Override
    public String toString() {
        return " | Produto ID: " + id +
                "| Nome: " + nome +
                "| Preço: R$" + String.format("%.2f", preco);

    }
}