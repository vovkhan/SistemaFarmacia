package com.farmacia.dados.persistencia;

import com.farmacia.dados.repositorio.IRepositorioFuncionarios;
import com.farmacia.negocio.entidade.Atendente;
import com.farmacia.negocio.entidade.Funcionario;
import com.farmacia.negocio.entidade.Gerente;
import com.farmacia.negocio.entidade.Supervisor;
import com.farmacia.negocio.servico.GeradorDeCodigoService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioFuncionariosArquivo implements IRepositorioFuncionarios {

    private ArrayList<Funcionario> funcionarios;
    private int proximoId = 1;
    private final GeradorDeCodigoService geradorDeCodigo = new GeradorDeCodigoService();
    private static final String NOME_ARQUIVO = "funcionarios.dat";

    public RepositorioFuncionariosArquivo() {
        this.funcionarios = new ArrayList<>();
        carregarDoArquivo();

        if (this.funcionarios.isEmpty()) {
            criarUsuariosPadrao();
        }
    }

    @Override
    public void salvar(Funcionario funcionario) {
        funcionario.setId(this.proximoId);
        String codigo = geradorDeCodigo.gerarCodigoParaFuncionario(funcionario, this.proximoId);
        funcionario.setCodigo(codigo);

        this.funcionarios.add(funcionario);
        this.proximoId++;
        salvarNoArquivo();
    }

    @Override
    public void atualizar(Funcionario funcionario) {
        salvarNoArquivo();
    }

    @Override
    public Funcionario buscarPorId(int id) {
        return this.funcionarios.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Funcionario buscarPorLogin(String login) {
        return this.funcionarios.stream()
                .filter(f -> f.getLogin().equalsIgnoreCase(login) && f.isAtivo())
                .findFirst().orElse(null);
    }

    @Override
    public List<Funcionario> listarTodosAtivos() {
        return this.funcionarios.stream().filter(Funcionario::isAtivo).collect(Collectors.toList());
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO))) {
            oos.writeObject(this.funcionarios);
        } catch (IOException e) {
            System.err.println("Erro ao salvar funcionarios: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOME_ARQUIVO))) {
            this.funcionarios = (ArrayList<Funcionario>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar funcionarios: " + e.getMessage());
            this.funcionarios = new ArrayList<>();
        }
    }

    private void atualizarProximoId() {
        if (!this.funcionarios.isEmpty()) {
            this.proximoId = this.funcionarios.stream().mapToInt(Funcionario::getId).max().orElse(0) + 1;
        }
    }

    private void criarUsuariosPadrao() {
        System.out.println("[DEBUG] Repositório de funcionários vazio. Criando usuários padrão...");

        Funcionario atendente = new Atendente("Zero Uno Atendente", "11111111111", "AT01", "atendente", "123");
        Funcionario supervisor = new Supervisor("Gui Supervisor", "22222222222", "SUP01", "supervisor", "123");
        Funcionario gerente = new Gerente("Guilherme Gerente", "33333333333", "GER01", "gerente", "123");
        this.salvar(atendente);
        this.salvar(supervisor);
        this.salvar(gerente);

        System.out.println("[DEBUG] Usuários padrão criados e salvos com sucesso.");
    }
}