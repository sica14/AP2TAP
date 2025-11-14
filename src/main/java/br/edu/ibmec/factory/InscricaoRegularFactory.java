package br.edu.ibmec.factory;

import br.edu.ibmec.entity.Situacao;
import org.springframework.stereotype.Component;

/**
 * Factory Pattern: Factory para criar inscrições regulares (novas).
 * Inicializa com valores padrão para uma nova inscrição.
 */
@Component
public class InscricaoRegularFactory extends InscricaoFactory {

    @Override
    protected float getAvaliacaoInicial1() {
        return 0.0f; // Sem nota inicial
    }

    @Override
    protected float getAvaliacaoInicial2() {
        return 0.0f; // Sem nota inicial
    }

    @Override
    protected int getFaltasInicial() {
        return 0; // Sem faltas no início
    }

    @Override
    protected Situacao getSituacaoInicial() {
        return Situacao.cursando; // Status inicial: cursando
    }
}

