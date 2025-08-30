package com.farmacia.negocio.excecao.funcionario;

public class AutenticacaoException extends FuncionarioException {
  public AutenticacaoException() {
    super("Login ou senha inválidos.");
  }
}
