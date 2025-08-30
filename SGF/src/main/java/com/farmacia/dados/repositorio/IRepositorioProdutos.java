package com.farmacia.dados.repositorio;

import com.farmacia.negocio.entidade.Produto;
import java.util.List;

public interface IRepositorioProdutos {
    void salvar(Produto produto);
    void remover(Produto produto);
    void atualizar(Produto produto);
    Produto buscarPorId(int id);
    Produto buscarPorCodigo(String codigo);
    Produto buscarPorNomeEFabricante(String nome, String fabricante);
    List<Produto> listarTodos();
}
