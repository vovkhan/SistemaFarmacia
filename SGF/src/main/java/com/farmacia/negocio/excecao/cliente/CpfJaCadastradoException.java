package com.farmacia.negocio.excecao.cliente;

public class CpfJaCadastradoException extends ClienteException {
  public CpfJaCadastradoException() {
    super("Já existe um cliente cadastrado com o CPF informado.");
  }
}
