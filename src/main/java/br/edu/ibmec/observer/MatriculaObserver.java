package br.edu.ibmec.observer;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Curso;

/**
 * Observer Pattern: Interface para observadores de matrícula.
 * Implementadores serão notificados quando um aluno se matricular em um curso.
 */
public interface MatriculaObserver {
    /**
     * Método chamado quando um aluno é matriculado em um curso.
     * @param aluno Aluno matriculado
     * @param curso Curso no qual o aluno foi matriculado
     */
    void onAlunoMatriculado(Aluno aluno, Curso curso);

    /**
     * Método chamado quando um aluno é desmatriculado de um curso.
     * @param aluno Aluno desmatriculado
     * @param curso Curso do qual o aluno foi desmatriculado
     */
    void onAlunoDesmatriculado(Aluno aluno, Curso curso);
}

