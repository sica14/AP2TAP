package br.edu.ibmec.observer;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Curso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer Pattern: Observador para envio de notificações por email.
 * Simula o envio de email quando alunos são matriculados/desmatriculados.
 */
@Component
public class EmailNotificacaoObserver implements MatriculaObserver {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificacaoObserver.class);

    @Override
    public void onAlunoMatriculado(Aluno aluno, Curso curso) {
        // Simula envio de email
        String mensagem = String.format(
            "Prezado(a) %s,\n\n" +
            "Sua matrícula no curso %s foi realizada com sucesso!\n" +
            "Matrícula: %d\n" +
            "Código do Curso: %d\n\n" +
            "Bem-vindo(a)!",
            aluno.getNomeCompleto(),
            curso.getNomeCurso(),
            aluno.getNumeroMatricula(),
            curso.getCodigoCurso()
        );

        logger.info("EMAIL ENVIADO para aluno {}: {}", aluno.getNomeCompleto(), mensagem);
    }

    @Override
    public void onAlunoDesmatriculado(Aluno aluno, Curso curso) {
        // Simula envio de email
        String mensagem = String.format(
            "Prezado(a) %s,\n\n" +
            "Sua desmatrícula do curso %s foi processada.\n" +
            "Matrícula: %d\n\n" +
            "Desejamos sucesso em sua jornada!",
            aluno.getNomeCompleto(),
            curso.getNomeCurso(),
            aluno.getNumeroMatricula()
        );

        logger.info("EMAIL ENVIADO para aluno {}: {}", aluno.getNomeCompleto(), mensagem);
    }
}
