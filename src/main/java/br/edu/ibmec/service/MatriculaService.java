package br.edu.ibmec.service;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.observer.MatriculaObserver;
import br.edu.ibmec.repository.AlunoRepository;
import br.edu.ibmec.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service para gerenciar matrícula de alunos em cursos.
 * Implementa Observer Pattern para notificar sobre matrículas/desmatrículas.
 */
@Service
public class MatriculaService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    private List<MatriculaObserver> observers = new ArrayList<>();

    /**
     * Adiciona um observador para ser notificado sobre matrículas.
     * @param observer Observador a ser adicionado
     */
    public void addObserver(MatriculaObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Remove um observador.
     * @param observer Observador a ser removido
     */
    public void removeObserver(MatriculaObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica todos os observadores sobre uma matrícula.
     */
    private void notifyMatricula(Aluno aluno, Curso curso) {
        for (MatriculaObserver observer : observers) {
            observer.onAlunoMatriculado(aluno, curso);
        }
    }

    /**
     * Notifica todos os observadores sobre uma desmatrícula.
     */
    private void notifyDesmatricula(Aluno aluno, Curso curso) {
        for (MatriculaObserver observer : observers) {
            observer.onAlunoDesmatriculado(aluno, curso);
        }
    }

    /**
     * Matricula um aluno em um curso.
     * @param matriculaAluno Número de matrícula do aluno
     * @param codigoCurso Código do curso
     * @return Aluno matriculado
     * @throws ServiceException se aluno ou curso não existir
     */
    @Transactional
    public Aluno matricularAlunoEmCurso(int matriculaAluno, int codigoCurso) throws ServiceException {
        // Busca o aluno
        Aluno aluno = alunoRepository.findById(matriculaAluno)
                .orElseThrow(() -> new ServiceException(
                        ServiceException.ServiceExceptionEnum.ALUNO_NAO_ENCONTRADO,
                        "Aluno não encontrado"
                ));

        // Busca o curso
        Curso curso = cursoRepository.findById(codigoCurso)
                .orElseThrow(() -> new ServiceException(
                        ServiceException.ServiceExceptionEnum.CURSO_NAO_ENCONTRADO,
                        "Curso não encontrado"
                ));

        // Valida se aluno já está matriculado em algum curso
        if (aluno.isMatriculadoEmCurso()) {
            throw new ServiceException(
                    ServiceException.ServiceExceptionEnum.ALUNO_JA_MATRICULADO,
                    "Aluno já está matriculado no curso: " + aluno.getCursoMatriculado().getNomeCurso()
            );
        }

        // Realiza a matrícula
        aluno.setCursoMatriculado(curso);
        aluno.setPossuiMatriculaAtiva(true);
        curso.adicionarAlunoMatriculado(aluno);

        // Salva as alterações
        alunoRepository.save(aluno);
        cursoRepository.save(curso);

        // Notifica os observadores
        notifyMatricula(aluno, curso);

        // Força inicialização de coleções LAZY necessárias ao DTO
        aluno.getNumerosTelefone().size();

        return aluno;
    }

    /**
     * Desmatricula um aluno de seu curso atual.
     * @param matriculaAluno Número de matrícula do aluno
     * @return Aluno desmatriculado
     * @throws ServiceException se aluno não existir ou não estiver matriculado
     */
    @Transactional
    public Aluno desmatricularAlunoDeCurso(int matriculaAluno) throws ServiceException {
        // Busca o aluno
        Aluno aluno = alunoRepository.findById(matriculaAluno)
                .orElseThrow(() -> new ServiceException(
                        ServiceException.ServiceExceptionEnum.ALUNO_NAO_ENCONTRADO,
                        "Aluno não encontrado"
                ));

        // Valida se aluno está matriculado
        if (!aluno.isMatriculadoEmCurso()) {
            throw new ServiceException(
                    ServiceException.ServiceExceptionEnum.ALUNO_NAO_MATRICULADO,
                    "Aluno não está matriculado em nenhum curso"
            );
        }

        Curso curso = aluno.getCursoMatriculado();

        // Realiza a desmatrícula
        curso.removerAlunoMatriculado(aluno);
        aluno.setCursoMatriculado(null);
        aluno.setPossuiMatriculaAtiva(false);

        // Salva as alterações
        alunoRepository.save(aluno);
        cursoRepository.save(curso);

        // Notifica os observadores
        notifyDesmatricula(aluno, curso);

        // Força inicialização de coleções LAZY necessárias ao DTO
        aluno.getNumerosTelefone().size();

        return aluno;
    }

    /**
     * Transfere um aluno de um curso para outro.
     * @param matriculaAluno Número de matrícula do aluno
     * @param novoCodigoCurso Código do novo curso
     * @return Aluno transferido
     * @throws ServiceException se houver algum erro
     */
    @Transactional
    public Aluno transferirAluno(int matriculaAluno, int novoCodigoCurso) throws ServiceException {
        // Desmatricula do curso atual
        desmatricularAlunoDeCurso(matriculaAluno);

        // Matricula no novo curso
        Aluno aluno = matricularAlunoEmCurso(matriculaAluno, novoCodigoCurso);

        // Força inicialização de coleções LAZY necessárias ao DTO
        aluno.getNumerosTelefone().size();

        return aluno;
    }
}
