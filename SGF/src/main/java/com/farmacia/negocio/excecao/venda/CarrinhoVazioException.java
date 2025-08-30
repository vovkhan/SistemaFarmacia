package com.farmacia.negocio.excecao.venda;

public class CarrinhoVazioException extends VendaException {
  public CarrinhoVazioException() {
    super("O carrinho de compras não pode estar vazio.");
  }
}
