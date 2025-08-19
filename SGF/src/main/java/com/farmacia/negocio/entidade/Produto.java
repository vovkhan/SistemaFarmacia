package com.farmacia.negocio.entidade;
import java.time.LocalDate;

abstract class Produto {
    protected String nome;
    protected double valor;
    protected LocalDate dataValidade ;
    protected String id;
    protected String fabricante;
    protected int quantidade;
    private static int contador = 0;

    public Produto(String nome,double valor,LocalDate data,String fabricante,int quantidade){
        this.nome = nome;
        this.valor = valor;
        this.dataValidade = data;
        this.id = "P-101" + (++contador);
        this.fabricante = fabricante;
        this.quantidade = quantidade;
    }

    public String getId() {
        return id;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public double getValor() {
        return valor;
    }

    public String getNome() {
        return nome;
    }


    public int getQuantidade() {
        return quantidade;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

}
