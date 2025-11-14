package br.edu.ibmec.controller;

import br.edu.ibmec.dto.AlunoDTO;
import br.edu.ibmec.dto.EstadoCivilDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.observer.EmailNotificacaoObserver;
import br.edu.ibmec.observer.LogMatriculaObserver;
import br.edu.ibmec.service.MatriculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;

/**
 * Controller para gerenciar matrículas de alunos em cursos.
 * Demonstra uso de Observer Pattern.
 */
@RestController
@RequestMapping("/api/matriculas")
@Tag(name = "Matrícula", description = "Endpoints para gerenciar matrícula de alunos em cursos")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private LogMatriculaObserver logObserver;

    @Autowired
    private EmailNotificacaoObserver emailObserver;

    @PostConstruct
    public void init() {
        // Registra os observadores
        matriculaService.addObserver(logObserver);
        matriculaService.addObserver(emailObserver);
    }

    @PostMapping("/{matriculaAluno}/curso/{codigoCurso}")
    @Operation(summary = "Matricular aluno em curso",
               description = "Matricula um aluno em um curso específico. Notifica observadores.")
    public ResponseEntity<?> matricularAluno(
            @Parameter(description = "Número de matrícula do aluno")
            @PathVariable int matriculaAluno,
            @Parameter(description = "Código do curso")
            @PathVariable int codigoCurso) {
        try {
            Aluno aluno = matriculaService.matricularAlunoEmCurso(matriculaAluno, codigoCurso);
            AlunoDTO dto = convertToDTO(aluno);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao processar matrícula: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{matriculaAluno}")
    @Operation(summary = "Desmatricular aluno",
               description = "Remove a matrícula de um aluno de seu curso atual")
    public ResponseEntity<?> desmatricularAluno(
            @Parameter(description = "Número de matrícula do aluno")
            @PathVariable int matriculaAluno) {
        try {
            Aluno aluno = matriculaService.desmatricularAlunoDeCurso(matriculaAluno);
            AlunoDTO dto = convertToDTO(aluno);
            return ResponseEntity.ok(dto);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao processar desmatrícula: " + e.getMessage()));
        }
    }

    @PutMapping("/{matriculaAluno}/transferir/{novoCodigoCurso}")
    @Operation(summary = "Transferir aluno",
               description = "Transfere um aluno de seu curso atual para um novo curso")
    public ResponseEntity<?> transferirAluno(
            @Parameter(description = "Número de matrícula do aluno")
            @PathVariable int matriculaAluno,
            @Parameter(description = "Código do novo curso")
            @PathVariable int novoCodigoCurso) {
        try {
            Aluno aluno = matriculaService.transferirAluno(matriculaAluno, novoCodigoCurso);
            AlunoDTO dto = convertToDTO(aluno);
            return ResponseEntity.ok(dto);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao processar transferência: " + e.getMessage()));
        }
    }

    private AlunoDTO convertToDTO(Aluno aluno) {
        AlunoDTO dto = new AlunoDTO();
        dto.setMatricula(aluno.getNumeroMatricula());
        dto.setNome(aluno.getNomeCompleto());
        dto.setIdade(aluno.getIdadeAtual());
        dto.setMatriculaAtiva(aluno.isPossuiMatriculaAtiva());
        dto.setTelefones(aluno.getNumerosTelefone());

        // Convert EstadoCivil to EstadoCivilDTO
        if (aluno.getEstadoCivilAtual() != null) {
            EstadoCivilDTO estadoCivilDTO = EstadoCivilDTO.valueOf(
                aluno.getEstadoCivilAtual().name()
            );
            dto.setEstadoCivil(estadoCivilDTO);
        }

        return dto;
    }

    // Inner class para resposta de erro
    private static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}

