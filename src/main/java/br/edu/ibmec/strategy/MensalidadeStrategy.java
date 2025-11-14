package br.edu.ibmec.strategy;

import br.edu.ibmec.entity.Aluno;

/**
 * Strategy Pattern: Interface para diferentes estratégias de cálculo de mensalidade.
 */
public interface MensalidadeStrategy {
    /**
     * Calcula o valor da mensalidade para um aluno.
     * @param aluno Aluno para o qual calcular a mensalidade
     * @return Valor da mensalidade
     */
    double calcularMensalidade(Aluno aluno);

    /**
     * Retorna a descrição da estratégia de cálculo.
     * @return Descrição da estratégia
     */
    String getDescricao();
}

