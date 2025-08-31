package com.farmacia.negocio.entidade;

import com.farmacia.negocio.excecao.DadosInvalidosException;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe abstrata que serve como base para todos os tipos de funcionários da farmácia.
 * Define os atributos e comportamentos comuns a todos.
 */
public abstract class Funcionario implements Serializable {
    private int id;
    private String codigo;
    private String nome;
    private String cpf;
    private String login;
    private String senha;
    private boolean ativo;

    public Funcionario(String nome, String cpf, String login, String senha) {
        if (nome == null || nome.trim().isEmpty() || cpf == null || cpf.trim().isEmpty()) {
            throw new DadosInvalidosException("Nome e CPF são obrigatórios para o cadastro de funcionario.");
        }
        if (!cpf.matches("^\\d{11}$")) {
            throw new DadosInvalidosException("O campo 'CPF' deve conter exatamente 11 dígitos numéricos, sem pontos ou traços.");
        }
        if (nome.matches(".*\\d.*")) {
            throw new DadosInvalidosException("O campo 'Nome' não pode conter números.");
        }
        if (login == null || login.trim().isEmpty()) {
            throw new DadosInvalidosException("Login não pode ser vazio.");
        }
        this.nome = nome;
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
        this.ativo = true;
    }

    public boolean autenticar(String senhaFornecida) {
        return this.senha != null && this.senha.equals(senhaFornecida);
    }

    public void desativar() {
        this.ativo = false;
    }

    /**
     * Contrato que obriga as subclasses a retornarem o nome do seu cargo.
     */
    public abstract String getNomeCargo();

    /**
     * Contrato que obriga as subclasses a fornecerem um resumo de seu desempenho.
     */
    public abstract String getResumoDeDesempenho();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public String toString() {
        return getNomeCargo() + " ID = " + id + "| Nome = " + nome + "| Codigo = " + codigo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario that = (Funcionario) o;
        return Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

}