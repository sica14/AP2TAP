package br.edu.ibmec.factory;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.entity.Situacao;
import br.edu.ibmec.entity.Turma;

/**
 * Factory Pattern: Classe abstrata para criação de inscrições.
 * Define o template para criar diferentes tipos de inscrições.
 */
public abstract class InscricaoFactory {

    /**
     * Factory Method: Cria uma inscrição com valores padrão apropriados.
     * @param aluno Aluno a ser inscrito
     * @param turma Turma na qual o aluno será inscrito
     * @return Inscrição criada
     */
    public Inscricao criarInscricao(Aluno aluno, Turma turma) {
        validarParametros(aluno, turma);

        Inscricao inscricao = new Inscricao(
            getAvaliacaoInicial1(),
            getAvaliacaoInicial2(),
            getFaltasInicial(),
            getSituacaoInicial(),
            aluno,
            turma
        );

        configurarInscricao(inscricao);

        return inscricao;
    }

    /**
     * Valida os parâmetros básicos.
     */
    protected void validarParametros(Aluno aluno, Turma turma) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo");
        }
        if (turma == null) {
            throw new IllegalArgumentException("Turma não pode ser nula");
        }
    }

    /**
     * Hook method: Configurações adicionais específicas do tipo de inscrição.
     */
    protected void configurarInscricao(Inscricao inscricao) {
        // Implementação opcional nas subclasses
    }

    // Template Methods - devem ser implementados pelas subclasses
    protected abstract float getAvaliacaoInicial1();
    protected abstract float getAvaliacaoInicial2();
    protected abstract int getFaltasInicial();
    protected abstract Situacao getSituacaoInicial();
}

