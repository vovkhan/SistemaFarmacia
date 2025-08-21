package com.farmacia.negocio.entidade;

public class Atendente extends Funcionario {

    public Atendente(String nome, String cpf, String matricula, String login, String senha) {
        super(nome, cpf, matricula, login, senha);
    }

    @Override
    public String getNomeCargo() {
        return "Atendente de Vendas";
    }

    //O resto dos métodos de atendente de vendas

}
