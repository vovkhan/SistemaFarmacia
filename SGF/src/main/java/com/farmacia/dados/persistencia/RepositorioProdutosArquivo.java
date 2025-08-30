package com.farmacia.dados.persistencia;

import com.farmacia.negocio.entidade.Produto;
import com.farmacia.dados.repositorio.IRepositorioProdutos;
import com.farmacia.negocio.servico.GeradorDeCodigoService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioProdutosArquivo implements IRepositorioProdutos {

    private ArrayList<Produto> produtos;
    private int proximoId = 1;
    private final GeradorDeCodigoService geradorDeCodigo = new GeradorDeCodigoService();
    private static final String NOME_ARQUIVO = "produtos.dat";

    public RepositorioProdutosArquivo() {
        this.produtos = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void salvar(Produto produto) {
        produto.setId(this.proximoId);
        String codigo = geradorDeCodigo.gerarCodigoParaProduto(produto, this.proximoId);
        produto.setCodigo(codigo);

        this.produtos.add(produto);
        this.proximoId++;
        salvarNoArquivo();
    }

    @Override
    public void atualizar(Produto produto) {
        salvarNoArquivo();
    }

    @Override
    public Produto buscarPorId(int id) {
        return this.produtos.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Produto buscarPorCodigo(String codigo) {
        return this.produtos.stream().filter(p -> p.getCodigo().equalsIgnoreCase(codigo)).findFirst().orElse(null);
    }

    @Override
    public List<Produto> listarTodos() {
        return new ArrayList<>(this.produtos);
    }

    @Override
    public Produto buscarPorNomeEFabricante(String nome, String fabricante) {
        for (Produto p : this.produtos) {
            if (p.getNome().equalsIgnoreCase(nome) && p.getFabricante().equalsIgnoreCase(fabricante)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void remover(Produto produto) {
        this.produtos.removeIf(p -> p.getId() == produto.getId());
        salvarNoArquivo();
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO))) {
            oos.writeObject(this.produtos);
        } catch (IOException e) {
            System.err.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOME_ARQUIVO))) {
            this.produtos = (ArrayList<Produto>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar produtos: " + e.getMessage());
            this.produtos = new ArrayList<>();
        }
    }

    private void atualizarProximoId() {
        if (!this.produtos.isEmpty()) {
            this.proximoId = this.produtos.stream().mapToInt(Produto::getId).max().orElse(0) + 1;
        }
    }
}
