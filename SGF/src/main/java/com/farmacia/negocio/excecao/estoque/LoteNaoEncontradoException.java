package com.farmacia.negocio.excecao.estoque;

public class LoteNaoEncontradoException extends EstoqueException {
  public LoteNaoEncontradoException(int id) {
    super("Lote com ID " + id + " não encontrado no estoque.");
  }
}
