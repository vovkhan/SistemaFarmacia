package com.farmacia.negocio.entidade;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Venda {
    private int id;
    private String codigo;
    private final LocalDateTime dataHora;
    private final ArrayList<ItemVenda> itens;
    private final Cliente cliente;
    private final Atendente vendedor;
    private double valorTotal;
    private int pontosUsadosParaDesconto;
    private boolean ehReembolso;

    public Venda(Atendente vendedor, Cliente cliente) {
        if (vendedor == null) {
            //Tem que ter um vendedor vendendo
        }
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.dataHora = LocalDateTime.now();
        this.itens = new ArrayList<>();
        this.ehReembolso = false;
        this.pontosUsadosParaDesconto = 0;
        this.valorTotal = 0.0;
    }

    public void adicionarItem(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) {
            //Tem que ter um produto e uma quantidade valida pra adicionar
        }

        ItemVenda novoItem = new ItemVenda(produto, quantidade, produto.getPreco());
        this.itens.add(novoItem);
    }

    public void calcularTotais() {
        double valorBruto = 0.0;
        for (ItemVenda item : this.itens) {
            valorBruto += item.calcularSubtotal();
        }

        double valorDoDesconto = this.pontosUsadosParaDesconto / 10.0;

        this.valorTotal = Math.max(0, valorBruto - valorDoDesconto);
    }

    public int calcularTotalPontosGerados() {
        int totalPontos = 0;
        for (ItemVenda item : this.itens) {
            totalPontos += item.getProduto().calcularPontosGerados(item.getQuantidade());
        }
        return totalPontos;
    }

    public void aplicarDescontoFidelidade(int pontos) {
        if (pontos < 0) {
            //Pontos não podem ser negativos
        }
        this.pontosUsadosParaDesconto = pontos;
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

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public ArrayList<ItemVenda> getItens() {
        return itens;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Atendente getVendedor() {
        return vendedor;
    }

    public double getValorTotal() {
        calcularTotais();
        return valorTotal;
    }

    public int getPontosUsadosParaDesconto() {
        return pontosUsadosParaDesconto;
    }

    public boolean isEhReembolso() {
        return ehReembolso;
    }

    public void setEhReembolso(boolean ehReembolso) {
        this.ehReembolso = ehReembolso;
    }
}