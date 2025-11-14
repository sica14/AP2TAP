package br.edu.ibmec.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ibmec.dto.ProfessorDTO;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.service.ProfessorRepositoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/professores")
@Tag(name = "Professor", description = "API para gerenciamento de professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepositoryService professorService;

    @GetMapping
    @Operation(summary = "Listar todos os professores")
    public ResponseEntity<List<ProfessorDTO>> listar() {
        try {
            List<ProfessorDTO> professores = professorService.listarProfessoresCompletos();
            return ResponseEntity.ok(professores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Buscar professor por código")
    public ResponseEntity<?> buscar(@PathVariable int codigo) {
        try {
            ProfessorDTO professor = professorService.buscarProfessor(codigo);
            return ResponseEntity.ok(professor);
        } catch (DaoException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar professor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo professor")
    public ResponseEntity<?> criar(@Valid @RequestBody ProfessorDTO professorDTO) {
        try {
            professorService.cadastrarProfessor(professorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(professorDTO);
        } catch (ServiceException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao cadastrar professor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{codigo}")
    @Operation(summary = "Atualizar professor existente")
    public ResponseEntity<?> atualizar(@PathVariable int codigo, @Valid @RequestBody ProfessorDTO professorDTO) {
        try {
            professorDTO.setCodigo(codigo);
            professorService.alterarProfessor(professorDTO);
            return ResponseEntity.ok(professorDTO);
        } catch (DaoException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (ServiceException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao atualizar professor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{codigo}")
    @Operation(summary = "Remover professor")
    public ResponseEntity<?> remover(@PathVariable int codigo) {
        try {
            professorService.removerProfessor(codigo);
            return ResponseEntity.noContent().build();
        } catch (DaoException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao remover professor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

