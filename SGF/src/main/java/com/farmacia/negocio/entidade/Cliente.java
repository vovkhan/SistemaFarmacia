package com.farmacia.negocio.entidade;

import java.util.Objects;

public class Cliente {
    private int id;
    private String nome;
    private String cpf;
    private int pontosFidelidade;
    private boolean ativo;

    public static final Cliente CLIENTE_NAO_CADASTRADO = new Cliente(0, "Cliente não cadastrado", "00000000000", 0, true);

    public Cliente(String nome, String cpf) {
        if (nome == null || nome.trim().isEmpty() || cpf == null || cpf.trim().isEmpty()) {
            //Nome ou cpf não pode ser vazio
        }
        this.nome = nome;
        this.cpf = cpf;
        this.pontosFidelidade = 0;
        this.ativo = true;
    }

    private Cliente(int id, String nome, String cpf, int pontos, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.pontosFidelidade = pontos;
        this.ativo = ativo;
    }

    public void adicionarPontos(int pontosGanhos) {
        if (this.isCadastrado() && pontosGanhos > 0) {
            this.pontosFidelidade += pontosGanhos;
        }
    }

    public void resgatarPontos(int pontosParaResgatar) {
        if (!this.isCadastrado()) {
            //Cliente não é cadastrado
        }
        if (pontosParaResgatar <= 0) {
            //Tentando resgatar uma quantidade invalida de pontos
        }
        if (pontosParaResgatar > this.pontosFidelidade) {
            //Pontos de Fidelidade insuficientes
        }
        this.pontosFidelidade -= pontosParaResgatar;
    }

    public void desativar() {
        this.ativo = false;
    }

    public boolean isCadastrado() {
        return this.id > 0;
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
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getPontosFidelidade() { return pontosFidelidade; }
    public boolean isAtivo() { return ativo; }

    @Override
    public String toString() {
        return "Cliente [ID=" + id + ", Nome='" + nome + "', CPF='" + cpf + "', Pontos=" + pontosFidelidade + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(cpf, cliente.cpf);
    }

}