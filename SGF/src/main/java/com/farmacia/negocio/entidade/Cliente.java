package com.farmacia.negocio.entidade;

import com.farmacia.negocio.excecao.DadosInvalidosException;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa um cliente da farmácia.
 * Esta classe armazena os dados cadastrais e gerencia o saldo de pontos de fidelidade.
 * Inclui uma implementação do padrão Null Object para representar um cliente não identificado.
 */
public class Cliente implements Serializable {
    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private int pontosFidelidade;
    private boolean ativo;

    /**
     * Representa um cliente não identificado (anônimo).
     * É usado em vendas onde o cliente não se cadastra para evitar NullPointerExceptions.
     */
    public static final Cliente CLIENTE_NAO_CADASTRADO = new Cliente(0, "Cliente Não Identificado", "00000000000", 0, true, "N/A", "N/A");

    public Cliente(String nome, String cpf, String email, String telefone) {
        if (nome == null || nome.trim().isEmpty() || cpf == null || cpf.trim().isEmpty()) {
            throw new DadosInvalidosException("Nome e CPF são obrigatórios para o cadastro de cliente.");
        }
        if (!cpf.matches("^\\d{11}$")) {
            throw new DadosInvalidosException("O campo 'CPF' deve conter exatamente 11 dígitos numéricos, sem pontos ou traços.");
        }
        if (nome.matches(".*\\d.*")) {
            throw new DadosInvalidosException("O campo 'Nome' não pode conter números.");
        }
        if (email != null && !email.contains("@")) {
            throw new DadosInvalidosException("O formato do email parece inválido.");
        }
        if (telefone != null && !telefone.matches("^\\d{9}$")) {
           throw new DadosInvalidosException("O campo 'Telefone' deve conter exatamente 9 dígitos numéricos, sem DDD.");
        }

        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.pontosFidelidade = 0;
        this.ativo = true;
    }

    /**
     * Construtor privado para uso interno (pelo Null Object e pela camada de persistência).
     */
    private Cliente(int id, String nome, String cpf, int pontos, boolean ativo, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.pontosFidelidade = pontos;
        this.ativo = ativo;
        this.email = email;
        this.telefone = telefone;
    }

    /**
     * Adiciona pontos de fidelidade ao saldo do cliente.
     */
    public void adicionarPontos(int pontosGanhos) {
        if (this.isCadastrado() && pontosGanhos > 0) {
            this.pontosFidelidade += pontosGanhos;
        }
    }

    /**
     * Resgata (subtrai) pontos do saldo do cliente para usar como desconto.
     */
    public void resgatarPontos(int pontosParaResgatar) {
        if (!this.isCadastrado()) {
            throw new UnsupportedOperationException("Não é possível resgatar pontos de um cliente não identificado.");
        }
        if (pontosParaResgatar <= 0) {
            throw new IllegalArgumentException("A quantidade de pontos para resgate deve ser positiva.");
        }
        if (pontosParaResgatar > this.pontosFidelidade) {
            throw new IllegalStateException("Saldo de pontos insuficiente para o resgate.");
        }
        this.pontosFidelidade -= pontosParaResgatar;
    }

    public void desativar() {
        this.ativo = false;
    }

    /**
     * Verifica se este é um cliente real (cadastrado) ou o cliente padrão anônimo.
     * Cliente cadastrado tem um ID > 0.
     */
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getPontosFidelidade() {
        return pontosFidelidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public String toString() {
        return "Cliente [ID=" + id + ", Nome='" + nome + "', CPF='" + cpf + "', Email='" + email + "']";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(cpf, cliente.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }
}