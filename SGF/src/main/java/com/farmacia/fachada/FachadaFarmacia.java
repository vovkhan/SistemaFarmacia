package com.farmacia.fachada;

import com.farmacia.negocio.entidade.*;
import com.farmacia.negocio.excecao.AcessoNegadoException;
import com.farmacia.dados.repositorio.*;
import com.farmacia.negocio.servico.*;
import com.farmacia.dados.persistencia.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * A Fachada é responsável por instanciar e orquestrar os serviços e repositórios.
 */
public class FachadaFarmacia {
    private final IRepositorioClientes clienteRepository;
    private final IRepositorioFuncionarios funcionarioRepository;
    private final IRepositorioProdutos produtoRepository;
    private final IRepositorioEstoque loteRepository;
    private final IRepositorioVendas vendaRepository;

    private final ClienteService clienteService;
    private final FuncionarioService funcionarioService;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;
    private final VendaService vendaService;
    private final ReciboService reciboService;
    private final RelatorioService relatorioService;

    public FachadaFarmacia() {
        this.clienteRepository = new RepositorioClientesArquivo();
        this.funcionarioRepository = new RepositorioFuncionariosArquivo();
        this.produtoRepository = new RepositorioProdutosArquivo();
        this.loteRepository = new RepositorioEstoqueArquivo();
        this.vendaRepository = new RepositorioVendasArquivo();

        this.clienteService = new ClienteService(clienteRepository, funcionarioRepository);
        this.funcionarioService = new FuncionarioService(funcionarioRepository);
        this.produtoService = new ProdutoService(produtoRepository, loteRepository);
        this.estoqueService = new EstoqueService(loteRepository, produtoRepository);
        this.vendaService = new VendaService(vendaRepository, loteRepository, clienteRepository, funcionarioRepository);
        this.relatorioService = new RelatorioService(vendaRepository, funcionarioRepository);
        this.reciboService = new ReciboService();
    }

    public Funcionario login(String login, String senha) {
        return funcionarioService.autenticar(login, senha);
    }

    //Atendente

    public void cadastrarNovoCliente(Cliente novoCliente, Funcionario funcionarioLogado) {
        if (!(funcionarioLogado instanceof Atendente)) {
            throw new AcessoNegadoException("Atendentes");
        }
        clienteService.cadastrarNovoCliente(novoCliente, (Atendente) funcionarioLogado);
    }

    public String processarVenda(Funcionario funcionarioLogado, Cliente cliente, ArrayList<ItemVenda> carrinho, int pontosParaUsar) {
        if (!(funcionarioLogado instanceof Atendente)) {
            throw new AcessoNegadoException("Atendentes");
        }
        Venda vendaConcluida = vendaService.processarVenda((Atendente) funcionarioLogado, cliente, carrinho, pontosParaUsar);
        return reciboService.gerarRecibo(vendaConcluida);
    }

    public String processarReembolso(Funcionario funcionarioLogado, Venda vendaOriginal, ArrayList<ItemVenda> itensAReembolsar) {
        if (!(funcionarioLogado instanceof Atendente)) {
            throw new AcessoNegadoException("Atendentes");
        }
        Venda transacaoReembolso = vendaService.processarReembolso(vendaOriginal, itensAReembolsar, (Atendente) funcionarioLogado);
        return reciboService.gerarRecibo(transacaoReembolso);
    }

    public void atualizarDadosCliente(int idCliente, String nome, String cpf, String email, String telefone, Funcionario funcionarioLogado) {
        if (!(funcionarioLogado instanceof Atendente)) {
            throw new AcessoNegadoException("Atendentes");
        }
        clienteService.atualizarDadosCliente(idCliente, nome, cpf, email, telefone);
    }

    public void removerCliente(int idCliente, Funcionario funcionarioLogado) {
        if (!(funcionarioLogado instanceof Atendente)) {
            throw new AcessoNegadoException("Atendentes");
        }
        clienteService.removerCliente(idCliente);
    }

    public List<Cliente> listarClientes() {
        return clienteService.listarTodosClientesAtivos();
    }

    public Cliente buscarClientePorCpf(String cpf) {
        return clienteService.buscarClientePorCpf(cpf);
    }

    public Venda buscarVendaPorCodigo(String codigo) {
        return vendaRepository.buscarPorCodigo(codigo);
    }

    public List<Venda> consultarVendas(LocalDate dataInicio, LocalDate dataFim, Cliente cliente) {
        return vendaService.consultarVendas(dataInicio, dataFim, cliente);
    }

    //Supervisor

    public void adicionarNovoProduto(Produto produto, Funcionario funcionarioLogado) {
        if (!(funcionarioLogado instanceof Supervisor)) {
            throw new AcessoNegadoException("Supervisores");
        }
        produtoService.adicionarNovoProduto(produto);
    }

    public void removerProduto(int idProduto, Funcionario funcionarioLogado) {
        if (!(funcionarioLogado instanceof Supervisor)) {
            throw new AcessoNegadoException("Supervisores");
        }
        produtoService.removerProduto(idProduto);
    }

    public void atualizarDadosProduto(int idProduto, String novoNome, String novoFabricante, double preco, int estMin, Funcionario funcionarioLogado) {
        if (!(funcionarioLogado instanceof Supervisor)) {
            throw new AcessoNegadoException("Supervisores");
        }
        produtoService.atualizarDadosProduto(idProduto, novoNome, novoFabricante, preco, estMin);
    }

    public void adicionarLote(int idProduto, int quantidade, LocalDate dataValidade, Funcionario funcionarioLogado) {
        if (!(funcionarioLogado instanceof Supervisor)) {
            throw new AcessoNegadoException("Supervisores ou Gerentes");
        }
        estoqueService.adicionarLote(idProduto, quantidade, dataValidade);
    }

    public String getStatusDetalhadoProduto(int idProduto) {
        return estoqueService.consultarStatusDetalhadoProduto(idProduto);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.listarTodos();
    }

    public void ajustarQuantidadeLote(int idLote, int novaQuantidade, Funcionario funcionarioLogado) {
        if (!(funcionarioLogado instanceof Supervisor)) {
            throw new AcessoNegadoException("Supervisores");
        }
        estoqueService.ajustarQuantidadeLote(idLote, novaQuantidade);
    }

    public List<String> verificarAlertasDeEstoque(Funcionario funcionarioLogado) {
        if (!(funcionarioLogado instanceof Supervisor)) {
            throw new AcessoNegadoException("Supervisores");
        }
        return estoqueService.verificarAlertasDeEstoque();
    }

    //Gerentes

    public List<String> gerarRelatorioMaisVendidos(int topN, Funcionario funcionarioLogado) {
        if (!(funcionarioLogado instanceof Gerente)) {
            throw new AcessoNegadoException("Gerentes");
        }
        return relatorioService.gerarRelatorioProdutosMaisVendidos(topN);
    }

    public List<String> gerarRelatorioMenosVendidos(int topN, Funcionario funcionarioLogado) {
        if (!(funcionarioLogado instanceof Gerente)) {
            throw new AcessoNegadoException("Gerentes");
        }
        return relatorioService.gerarRelatorioProdutosMenosVendidos(topN);
    }

    public List<String> gerarRelatorioDesempenho(Funcionario funcionarioLogado) {
        if (!(funcionarioLogado instanceof Gerente)) {
            throw new AcessoNegadoException("Gerentes");
        }
        return relatorioService.gerarRelatorioDesempenhoFuncionarios();
    }

    public String gerarRelatorioFaturamento(LocalDate dataInicio, LocalDate dataFim, Funcionario funcionarioLogado) {
        if (!(funcionarioLogado instanceof Gerente)) {
            throw new AcessoNegadoException("Gerentes");
        }
        return relatorioService.gerarRelatorioFaturamento(dataInicio, dataFim);
    }

    public Produto buscarProdutoPorCodigo(String codigo) {
        return produtoRepository.buscarPorCodigo(codigo);
    }

    public Produto buscarProdutoPorId(int id) {
        return produtoRepository.buscarPorId(id);
    }

}