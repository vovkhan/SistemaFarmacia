package com.farmacia.negocio.entidade;

public class Gerente extends Funcionario{

    public Gerente(String nome, String cpf, String matricula, String login, String senha) {
        super(nome, cpf, matricula, login, senha);
    }

    @Override
    public String getNomeCargo() {
        return "Gerente Administrativo";
    }

    //O resto dos métodos do Gerente
}
