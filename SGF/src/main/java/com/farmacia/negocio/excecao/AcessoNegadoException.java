package com.farmacia.negocio.excecao;

public class AcessoNegadoException extends RuntimeException {
  public AcessoNegadoException(String cargoNecessario) {
    super("Acesso negado. Apenas " + cargoNecessario + " podem realizar esta operação.");
  }
}
