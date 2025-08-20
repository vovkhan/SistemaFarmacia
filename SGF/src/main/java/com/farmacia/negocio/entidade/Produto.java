package com.farmacia.negocio.entidade;

abstract class Produto {
    protected String nome;
    protected double valor;
    protected String id;
    protected String fabricante;
    protected static int contador = 0;

    public Produto(String nome,double valor,String fabricante){
        this.nome = nome;
        this.valor = valor;
        //this.id = "P-101" + (++contador);
        //Acho que isso só vai ser necessário isso nas classes que extendem dessa(mas ainda vai ter)
        this.fabricante = fabricante;
    }

    // Rafa: adicionei o setId, mas o Id pode ser incializado no construtor da subclasse se acharem melhor
    public abstract void setId();

    public String getId() {
        return id;
    }

    public double getValor() {
        return valor;
    }

    public String getNome() {
        return nome;
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

}
