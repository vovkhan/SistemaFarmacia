package com.farmacia.dados.persistencia;

import java.io.*;
import com.farmacia.negocio.entidade.Cliente;
import com.farmacia.dados.ConfiguracaoPersistencia;
import com.farmacia.dados.repositorio.IRepositorioClientes;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioClientesArquivo implements IRepositorioClientes {

    private ArrayList<Cliente> clientes;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = ConfiguracaoPersistencia.getCaminhoCompleto("clientes.dat");

    public RepositorioClientesArquivo() {
        this.clientes = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void salvar(Cliente cliente) {
        if (cliente.getId() == 0) {
            cliente.setId(this.proximoId++);
        }
        this.clientes.removeIf(c -> c.getId() == cliente.getId());
        this.clientes.add(cliente);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(Cliente cliente) {
        salvarNoArquivo();
    }

    @Override
    public Cliente buscarPorId(int id) {
        for (Cliente c : this.clientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Cliente buscarPorCpf(String cpf) {
        for (Cliente c : this.clientes) {
            if (c.getCpf().equals(cpf) && c.isAtivo()) {
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Cliente> listarTodosAtivos() {
        ArrayList<Cliente> ativos = new ArrayList<>();
        for (Cliente c : this.clientes) {
            if (c.isAtivo() && c.isCadastrado()) {
                ativos.add(c);
            }
        }
        return ativos;
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(NOME_ARQUIVO)))) {
            oos.writeObject(this.clientes);
        } catch (IOException e) {
            System.err.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            this.clientes.add(Cliente.CLIENTE_NAO_CADASTRADO);
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(NOME_ARQUIVO)))) {
            this.clientes = (ArrayList<Cliente>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar clientes: " + e.getMessage());
            this.clientes = new ArrayList<>();
            this.clientes.add(Cliente.CLIENTE_NAO_CADASTRADO);
        }
    }

    private void atualizarProximoId() {
        if (this.clientes.isEmpty()) {
            this.proximoId = 1;
        } else {
            int maiorId = this.clientes.stream().mapToInt(Cliente::getId).max().orElse(0);
            this.proximoId = maiorId + 1;
        }
    }
}