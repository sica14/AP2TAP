package br.edu.ibmec.observer;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Curso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer Pattern: Observador para log de matrículas.
 * Registra em log quando alunos são matriculados/desmatriculados.
 */
@Component
public class LogMatriculaObserver implements MatriculaObserver {

    private static final Logger logger = LoggerFactory.getLogger(LogMatriculaObserver.class);

    @Override
    public void onAlunoMatriculado(Aluno aluno, Curso curso) {
        logger.info("MATRÍCULA REALIZADA - Aluno: {} (Matrícula: {}) matriculado no curso: {} (Código: {})",
                aluno.getNomeCompleto(),
                aluno.getNumeroMatricula(),
                curso.getNomeCurso(),
                curso.getCodigoCurso());
    }

    @Override
    public void onAlunoDesmatriculado(Aluno aluno, Curso curso) {
        logger.info("DESMATRÍCULA REALIZADA - Aluno: {} (Matrícula: {}) desmatriculado do curso: {} (Código: {})",
                aluno.getNomeCompleto(),
                aluno.getNumeroMatricula(),
                curso.getNomeCurso(),
                curso.getCodigoCurso());
    }
}
