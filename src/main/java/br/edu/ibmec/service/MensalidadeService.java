package br.edu.ibmec.service;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.strategy.MensalidadeStrategy;
import org.springframework.stereotype.Service;

/**
 * Service para cálculo de mensalidade usando Strategy Pattern.
 * Permite trocar a estratégia de cálculo dinamicamente.
 */
@Service
public class MensalidadeService {

    private MensalidadeStrategy estrategiaAtual;

    public MensalidadeService() {
        // Estratégia padrão pode ser definida aqui
    }

    /**
     * Define a estratégia de cálculo de mensalidade.
     * @param estrategia Estratégia a ser utilizada
     */
    public void setEstrategia(MensalidadeStrategy estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("Estratégia não pode ser nula");
        }
        this.estrategiaAtual = estrategia;
    }

    /**
     * Calcula a mensalidade usando a estratégia definida.
     * @param aluno Aluno para calcular a mensalidade
     * @return Valor da mensalidade
     */
    public double calcularMensalidade(Aluno aluno) {
        if (estrategiaAtual == null) {
            throw new IllegalStateException("Nenhuma estratégia de cálculo foi definida");
        }
        return estrategiaAtual.calcularMensalidade(aluno);
    }

    /**
     * Retorna descrição da estratégia atual.
     * @return Descrição da estratégia
     */
    public String getDescricaoEstrategia() {
        if (estrategiaAtual == null) {
            return "Nenhuma estratégia definida";
        }
        return estrategiaAtual.getDescricao();
    }
}

