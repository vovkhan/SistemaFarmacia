package com.farmacia.negocio.servico;

import com.farmacia.negocio.entidade.Lote;
import com.farmacia.negocio.entidade.Produto;
import com.farmacia.dados.repositorio.IRepositorioEstoque;
import com.farmacia.dados.repositorio.IRepositorioProdutos;
import com.farmacia.negocio.excecao.produto.ProdutoComEstoqueException;
import com.farmacia.negocio.excecao.produto.ProdutoDuplicadoException;
import com.farmacia.negocio.excecao.produto.ProdutoNaoEncontradoException;

import java.util.List;

public class ProdutoService {

    private final IRepositorioProdutos produtoRepository;
    private final IRepositorioEstoque loteRepository;

    public ProdutoService(IRepositorioProdutos produtoRepo, IRepositorioEstoque loteRepo) {
        this.produtoRepository = produtoRepo;
        this.loteRepository = loteRepo;
    }

    public void adicionarNovoProduto(Produto produto) {
        if (produtoRepository.buscarPorNomeEFabricante(produto.getNome(), produto.getFabricante()) != null) {
            throw new ProdutoDuplicadoException();
        }
        produtoRepository.salvar(produto);
    }

    public void removerProduto(int idProduto) {
        Produto produtoParaRemover = produtoRepository.buscarPorId(idProduto);
        if (produtoParaRemover == null) {
            throw new ProdutoNaoEncontradoException(idProduto);
        }
        List<Lote> lotesDoProduto = loteRepository.listarPorProduto(produtoParaRemover);
        if (!lotesDoProduto.isEmpty()) {
            throw new ProdutoComEstoqueException(produtoParaRemover.getNome());
        }
        produtoRepository.remover(produtoParaRemover);
    }

    public void atualizarPreco(int idProduto, double novoPreco) throws Exception {
        Produto produto = produtoRepository.buscarPorId(idProduto);
        if (produto == null) {
            throw new Exception("Produto com ID " + idProduto + " não encontrado.");
        }

        produto.setPreco(novoPreco);
        produtoRepository.atualizar(produto);
    }

    public void atualizarDadosProduto(int idProduto, String novoNome, String novoFabricante, double novoPreco, int novoEstoqueMinimo) {
        Produto produtoParaAtualizar = produtoRepository.buscarPorId(idProduto);
        if (produtoParaAtualizar == null) {
            throw new ProdutoNaoEncontradoException(idProduto);
        }

        Produto produtoExistente = produtoRepository.buscarPorNomeEFabricante(novoNome, novoFabricante);
        if (produtoExistente != null && produtoExistente.getId() != produtoParaAtualizar.getId()) {
            throw new ProdutoDuplicadoException();
        }

        produtoParaAtualizar.setNome(novoNome);
        produtoParaAtualizar.setFabricante(novoFabricante);
        produtoParaAtualizar.setPreco(novoPreco);
        produtoParaAtualizar.setEstoqueMinimo(novoEstoqueMinimo);

        produtoRepository.atualizar(produtoParaAtualizar);
    }

}