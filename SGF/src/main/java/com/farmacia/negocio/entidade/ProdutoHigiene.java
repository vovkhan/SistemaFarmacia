package com.farmacia.negocio.entidade;

public class ProdutoHigiene extends  Produto{


    public ProdutoHigiene(String nome, double valor, String fabricante) {
        super(nome, valor, fabricante);

    }

    @Override
    public void setId() {
        this.id = "P-301-" + (++contador);
    }
}
