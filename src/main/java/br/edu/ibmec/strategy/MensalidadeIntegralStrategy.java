package br.edu.ibmec.strategy;

import br.edu.ibmec.entity.Aluno;
import org.springframework.stereotype.Component;

/**
 * Strategy Pattern: Cálculo de mensalidade para alunos de período integral.
 * Valor base por disciplina inscrita.
 */
@Component
public class MensalidadeIntegralStrategy implements MensalidadeStrategy {

    private static final double VALOR_BASE = 800.0;
    private static final double VALOR_POR_DISCIPLINA = 200.0;

    @Override
    public double calcularMensalidade(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo");
        }

        int quantidadeInscricoes = aluno.obterQuantidadeInscricoes();

        // Valor base + valor por disciplina
        return VALOR_BASE + (quantidadeInscricoes * VALOR_POR_DISCIPLINA);
    }

    @Override
    public String getDescricao() {
        return "Mensalidade Integral - Valor base: R$ " + VALOR_BASE +
               " + R$ " + VALOR_POR_DISCIPLINA + " por disciplina";
    }
}

