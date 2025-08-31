package com.farmacia.negocio.entidade;

public class Gerente extends Funcionario {

    public Gerente(String nome, String cpf, String login, String senha) {
        super(nome, cpf, login, senha);
    }

    @Override
    public String getNomeCargo() {
        return "Gerente Administrativo";
    }

    @Override
    public String getResumoDeDesempenho() {
        return "Métricas de desempenho gerencial não são aplicáveis neste relatório.";
    }

    public String contratarFuncionario(Funcionario f){
        return "Funcionário"+ f.getNome()+ "Contratado!";
        //Talvez tenha?
    }


}
