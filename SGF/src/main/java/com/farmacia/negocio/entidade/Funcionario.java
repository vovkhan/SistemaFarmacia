package com.farmacia.negocio.entidade;

public abstract class Funcionario {

    protected int id;
    protected String codigo;
    protected String nome;
    protected String cpf;
    protected String matricula;
    protected String login;
    protected String senha;
    protected boolean ativo;

    public Funcionario(String nome, String cpf, String matricula, String login, String senha) {
        if (nome == null || nome.trim().isEmpty()) {
            //Nome não pode ser vazio
        }


        this.nome = nome;
        this.cpf = cpf;
        this.matricula = matricula;
        this.login = login;
        this.senha = senha;
        this.ativo = true;
    }

    public boolean autenticar(String senhaFornecida) {
        if (senhaFornecida == null) {
            return false;
        }
        return this.senha.equals(senhaFornecida);
    }

    public void desativar() {
        this.ativo = false;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getLogin() {
        return login;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public abstract String getNomeCargo();

}