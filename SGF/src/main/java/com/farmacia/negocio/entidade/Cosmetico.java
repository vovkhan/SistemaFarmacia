package com.farmacia.negocio.entidade;

public class Cosmetico extends  Produto{

    public Cosmetico(String nome, double valor, String fabricante) {
        super(nome, valor, fabricante);
    }

    @Override
    public void setId() {
        this.id = "P-401-" + (++contador);

    }

}
