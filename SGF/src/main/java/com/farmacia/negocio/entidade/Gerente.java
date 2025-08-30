package com.farmacia.negocio.entidade;

public class Gerente extends Funcionario {

    public Gerente(String nome, String cpf, String matricula, String login, String senha) {
        super(nome, cpf, matricula, login, senha);
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
