package com.farmacia.negocio.servico;

import com.farmacia.negocio.entidade.ItemVenda;
import com.farmacia.negocio.entidade.Venda;
import java.time.format.DateTimeFormatter;

/**
 * Serviço responsável por gerar representações textuais de transações.
 */
public class ReciboService {
    private static final DateTimeFormatter FORMATADOR_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final String NOME_FARMACIA = "Sistema de gerenciamento de farmacia";

    public String gerarRecibo(Venda venda) {
        StringBuilder recibo = new StringBuilder();

        String titulo = venda.isEhReembolso() ? "COMPROVANTE DE REEMBOLSO" : "RECIBO DE VENDA";

        recibo.append("========================================\n");
        recibo.append(NOME_FARMACIA).append("\n");
        recibo.append("========================================\n");
        recibo.append(titulo).append("\n");
        recibo.append("----------------------------------------\n");

        // --- 2. Detalhes da Transação ---
        recibo.append("Código: ").append(venda.getCodigo()).append("\n");
        recibo.append("Data/Hora: ").append(venda.getDataHora().format(FORMATADOR_DATA_HORA)).append("\n");
        recibo.append("Vendedor: ").append(venda.getVendedor().getNome()).append("\n");

        if (venda.getCliente().isCadastrado()) {
            recibo.append("Cliente: ").append(venda.getCliente().getNome()).append("\n");
        } else {
            recibo.append("Cliente: ").append(venda.getCliente().getNome()).append("\n");
        }
        recibo.append("----------------------------------------\n");

        recibo.append("ITENS:\n");
        double subtotal = 0;
        for (ItemVenda item : venda.getItens()) {
            subtotal += item.calcularSubtotal();

            recibo.append("- ").append(item.getProduto().getNome()).append("\n");

            recibo.append(String.format("  %d un. x R$ %.2f ............ R$ %.2f\n",
                    item.getQuantidade(),
                    item.getPrecoUnitarioGravado(),
                    item.calcularSubtotal()
            ));
        }
        recibo.append("----------------------------------------\n");

        double valorFinal = venda.getValorTotal();
        double desconto = subtotal - valorFinal;
        String labelTotal = venda.isEhReembolso() ? "VALOR REEMBOLSADO:" : "VALOR TOTAL:";

        recibo.append(String.format("Subtotal: R$ %.2f\n", subtotal));
        if (desconto > 0) {
            recibo.append(String.format("Desconto (Pontos): R$ %.2f\n", desconto));
        }
        recibo.append(String.format("%s R$ %.2f\n", labelTotal, Math.abs(valorFinal)));
        recibo.append("----------------------------------------\n");

        if (!venda.isEhReembolso()) {
            recibo.append("Pontos usados nesta compra: ").append(venda.getPontosUsadosParaDesconto()).append("\n");
            recibo.append("Pontos gerados nesta compra: ").append(venda.calcularTotalPontosGerados()).append("\n");
            recibo.append("----------------------------------------\n");
        }

        recibo.append("OBRIGADO E VOLTE SEMPRE!\n");
        recibo.append("========================================\n");

        return recibo.toString();
    }
}