package com.farmacia.dados.repositorio;

import com.farmacia.negocio.entidade.Lote;
import com.farmacia.negocio.entidade.Produto;
import java.util.List;

public interface IRepositorioEstoque {
    void salvar(Lote lote);
    void atualizar(Lote lote);
    Lote buscarPorId(int id);
    List<Lote> listarTodos();
    List<Lote> listarPorProduto(Produto produto);
}