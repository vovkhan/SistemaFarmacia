package com.farmacia.negocio.excecao.produto;

public class ProdutoDuplicadoException extends ProdutoException {
  public ProdutoDuplicadoException() {
    super("Já existe um produto cadastrado com o mesmo nome e fabricante.");
  }
}
