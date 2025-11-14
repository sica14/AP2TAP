package br.edu.ibmec.strategy;

import br.edu.ibmec.entity.Aluno;
import org.springframework.stereotype.Component;

/**
 * Strategy Pattern: Cálculo de mensalidade para alunos de meio período.
 * Valor proporcional ao número de disciplinas.
 */
@Component
public class MensalidadeMeioPeriodoStrategy implements MensalidadeStrategy {

    private static final double VALOR_POR_DISCIPLINA = 350.0;
    private static final double TAXA_ADMINISTRATIVA = 100.0;

    @Override
    public double calcularMensalidade(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo");
        }

        int quantidadeInscricoes = aluno.obterQuantidadeInscricoes();

        // Taxa administrativa + valor por disciplina
        return TAXA_ADMINISTRATIVA + (quantidadeInscricoes * VALOR_POR_DISCIPLINA);
    }

    @Override
    public String getDescricao() {
        return "Mensalidade Meio Período - Taxa administrativa: R$ " + TAXA_ADMINISTRATIVA +
               " + R$ " + VALOR_POR_DISCIPLINA + " por disciplina";
    }
}

