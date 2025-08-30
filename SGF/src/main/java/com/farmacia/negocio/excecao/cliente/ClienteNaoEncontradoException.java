package com.farmacia.negocio.excecao.cliente;

public class ClienteNaoEncontradoException extends ClienteException {
  public ClienteNaoEncontradoException(int id) {
    super("Cliente com ID " + id + " não encontrado.");
  }
  public ClienteNaoEncontradoException(String cpf) {
    super("Cliente com CPF " + cpf + " não encontrado.");
  }
}
