package com.farmacia.negocio.entidade;

public class Medicamento extends Produto {
    private String tarja;


    public Medicamento(String nome, double valor, String fabricante,String tarja) {
        super(nome, valor, fabricante);
        this.tarja = tarja;
    }

    @Override
    public void setId(){
        this.id = "P-101-" + (++contador);
    }

    public String getTarja() {
        return tarja;
    }


}
