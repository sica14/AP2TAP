package br.edu.ibmec.strategy;

import br.edu.ibmec.entity.Aluno;
import org.springframework.stereotype.Component;

/**
 * Strategy Pattern: Cálculo de mensalidade para alunos com bolsa de estudos.
 * Aplica desconto sobre valor base.
 */
@Component
public class MensalidadeBolsistaStrategy implements MensalidadeStrategy {

    private static final double VALOR_BASE = 800.0;
    private static final double VALOR_POR_DISCIPLINA = 200.0;
    private static final double PERCENTUAL_DESCONTO = 0.50; // 50% de desconto

    @Override
    public double calcularMensalidade(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo");
        }

        int quantidadeInscricoes = aluno.obterQuantidadeInscricoes();
        double valorTotal = VALOR_BASE + (quantidadeInscricoes * VALOR_POR_DISCIPLINA);

        // Aplica desconto de bolsa
        return valorTotal * (1 - PERCENTUAL_DESCONTO);
    }

    @Override
    public String getDescricao() {
        return "Mensalidade Bolsista - " + (PERCENTUAL_DESCONTO * 100) +
               "% de desconto sobre valor integral";
    }
}

