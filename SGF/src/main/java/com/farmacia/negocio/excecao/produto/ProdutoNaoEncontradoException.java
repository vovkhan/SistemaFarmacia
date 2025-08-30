package com.farmacia.negocio.excecao.produto;

public class ProdutoNaoEncontradoException extends ProdutoException {
  public ProdutoNaoEncontradoException(int id) {
    super("Produto com ID " + id + " não encontrado.");
  }
  public ProdutoNaoEncontradoException(String codigo) {
    super("Produto com código '" + codigo + "' não encontrado.");
  }
}
