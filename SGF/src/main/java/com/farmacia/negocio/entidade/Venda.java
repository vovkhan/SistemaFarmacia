package com.farmacia.negocio.entidade;

import com.farmacia.negocio.excecao.DadosInvalidosException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa uma transação de venda na farmácia.
 * Agrupa todos os dados relevantes de uma venda, como cliente, vendedor, itens e valores.
 */
public class Venda implements Serializable {

    private int id;
    private String codigo;
    private final LocalDateTime dataHora;
    private final List<ItemVenda> itens;
    private final Cliente cliente;
    private final Atendente vendedor;
    private double valorTotal;
    private int pontosUsadosParaDesconto;
    private boolean ehReembolso;

    public Venda(Atendente vendedor, Cliente cliente) {
        if (vendedor == null || cliente == null) {
            throw new DadosInvalidosException("Vendedor e Cliente são obrigatórios para iniciar uma venda.");
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
            throw new IllegalArgumentException("Produto ou quantidade inválida para adicionar ao item.");
        }
        ItemVenda novoItem = new ItemVenda(produto, quantidade, produto.getPreco());
        this.itens.add(novoItem);
    }

    /**
     * Calcula o valor total da venda.
     * Cada 10 pontos acumulados equivale a R$ 1,00.
     */
    public void calcularTotais() {
        double valorBruto = this.itens.stream().mapToDouble(ItemVenda::calcularSubtotal).sum();
        double valorDoDesconto = this.pontosUsadosParaDesconto / 10.0;
        this.valorTotal = Math.max(0, valorBruto - valorDoDesconto);
    }

    public int calcularTotalPontosGerados() {
        return this.itens.stream()
                .mapToInt(item -> item.getProduto().calcularPontosGerados(item.getQuantidade()))
                .sum();
    }

    public void aplicarDescontoFidelidade(int pontos) {
        if (pontos < 0) {
            throw new IllegalArgumentException("Pontos para desconto não podem ser negativos.");
        }
        this.pontosUsadosParaDesconto = pontos;
    }

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

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public List<ItemVenda> getItens() {
        return new ArrayList<>(itens);
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

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
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

    @Override
    public String toString() {
        return "Venda [ID=" + id + ", Codigo='" + codigo + "', Cliente='" + cliente.getNome() + "', Itens=" + itens.size() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        if (id == 0 || venda.id == 0) return this == o;
        return id == venda.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}