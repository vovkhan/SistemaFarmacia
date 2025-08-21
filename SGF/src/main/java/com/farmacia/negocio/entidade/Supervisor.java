package com.farmacia.negocio.entidade;

public class Supervisor extends Funcionario{

    public Supervisor(String nome, String cpf, String matricula, String login, String senha) {
        super(nome, cpf, matricula, login, senha);
    }

    @Override
    public String getNomeCargo() {
        return "Supervisor";
    }

    //O resto dos métodos de supervisor
}
