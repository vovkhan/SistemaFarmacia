package com.farmacia.negocio.entidade;

public class Supervisor extends Funcionario {

    public Supervisor(String nome, String cpf, String login, String senha) {
        super(nome, cpf, login, senha);
    }


    @Override
    public String getNomeCargo() {
        return "Supervisor";
    }

    @Override
    public String getResumoDeDesempenho() {
        return "Métricas de desempenho de supervisão não são aplicáveis neste relatório.";
    }
}