package com.farmacia.negocio.excecao.produto;

public class ProdutoComEstoqueException extends ProdutoException {
  public ProdutoComEstoqueException(String nomeProduto) {
    super("Não é possível remover o produto '" + nomeProduto + "' pois ele ainda possui lotes em estoque.");
  }
}
