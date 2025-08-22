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
    public String contratarFuncionario(Funcionario f){
        return "Funcionário"+ f.getNome()+ "Contratado!";
    }


    public String definirMetaVendas(Produto produto, int numeroVendas){
        return "Meta de vendas definida: " + numeroVendas + " unidades do produto " + produto.getNome();
    }

    public String aprovarCompraProdutos(Produto produto, int quantidade, int valorCompra){
        return "Compra aprovada: " + quantidade + " unidades de " + produto.getNome() + " por R$ " + valorCompra;
    }
}
