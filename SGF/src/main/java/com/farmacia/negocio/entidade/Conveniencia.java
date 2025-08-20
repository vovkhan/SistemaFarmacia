package com.farmacia.negocio.entidade;

public class Conveniencia extends  Produto{

    public Conveniencia(String nome, double valor, String fabricante) {
        super(nome, valor, fabricante);

    }

    @Override
    public void setId() {
        this.id = "P-201-" + (++contador);

    }

}
