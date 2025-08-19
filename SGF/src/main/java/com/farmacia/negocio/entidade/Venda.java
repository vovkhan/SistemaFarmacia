package com.farmacia.negocio.entidade;

import java.time.LocalDateTime;
import java.util.*;

public class Venda {

    private int id;

    private LocalDateTime dataHora;

    private ArrayList<ItemVenda> itens;

    private Cliente cliente;

    private Atendente vendedor;

    private double valorTotal;

    private int pontosUsadosParaDesconto;

    private boolean ehReembolso;

    public void adicionarItem(Produto produto, int quantidade){

    }

    public void calcularTotais() {

    }

    public int calcularTotalPontosGerados(){
        int totalPontos = 0;
        for (ItemVenda item : itens) {
            //totalPontos += item.getProduto().calcularPontosGerados(item.getQuantidade());
        }
        return totalPontos;
    }
}
