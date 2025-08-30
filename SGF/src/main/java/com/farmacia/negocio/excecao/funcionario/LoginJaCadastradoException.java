package com.farmacia.negocio.excecao.funcionario;

public class LoginJaCadastradoException extends FuncionarioException {
  public LoginJaCadastradoException(String login) {
    super("O login '" + login + "' já está em uso.");
  }
}