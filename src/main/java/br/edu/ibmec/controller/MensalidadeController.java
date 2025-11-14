package br.edu.ibmec.controller;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.repository.AlunoRepository;
import br.edu.ibmec.service.MensalidadeService;
import br.edu.ibmec.strategy.MensalidadeBolsistaStrategy;
import br.edu.ibmec.strategy.MensalidadeIntegralStrategy;
import br.edu.ibmec.strategy.MensalidadeMeioPeriodoStrategy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para cálculo de mensalidades.
 * Demonstra uso de Strategy Pattern.
 */
@RestController
@RequestMapping("/api/mensalidades")
@Tag(name = "Mensalidade", description = "Endpoints para cálculo de mensalidades usando diferentes estratégias")
public class MensalidadeController {

    @Autowired
    private MensalidadeService mensalidadeService;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private MensalidadeIntegralStrategy integralStrategy;

    @Autowired
    private MensalidadeMeioPeriodoStrategy meioPeriodoStrategy;

    @Autowired
    private MensalidadeBolsistaStrategy bolsistaStrategy;

    @GetMapping("/aluno/{matricula}/integral")
    @Operation(summary = "Calcular mensalidade integral",
               description = "Calcula mensalidade usando estratégia de período integral")
    @Transactional(readOnly = true)
    public ResponseEntity<?> calcularMensalidadeIntegral(
            @Parameter(description = "Número de matrícula do aluno")
            @PathVariable int matricula) {
        try {
            Aluno aluno = buscarAluno(matricula);
            mensalidadeService.setEstrategia(integralStrategy);
            double valor = mensalidadeService.calcularMensalidade(aluno);

            return ResponseEntity.ok(new MensalidadeResponse(
                aluno.getNumeroMatricula(),
                aluno.getNomeCompleto(),
                valor,
                mensalidadeService.getDescricaoEstrategia(),
                aluno.obterQuantidadeInscricoes()
            ));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao calcular mensalidade: " + e.getMessage()));
        }
    }

    @GetMapping("/aluno/{matricula}/meio-periodo")
    @Operation(summary = "Calcular mensalidade meio período",
               description = "Calcula mensalidade usando estratégia de meio período")
    @Transactional(readOnly = true)
    public ResponseEntity<?> calcularMensalidadeMeioPeriodo(
            @Parameter(description = "Número de matrícula do aluno")
            @PathVariable int matricula) {
        try {
            Aluno aluno = buscarAluno(matricula);
            mensalidadeService.setEstrategia(meioPeriodoStrategy);
            double valor = mensalidadeService.calcularMensalidade(aluno);

            return ResponseEntity.ok(new MensalidadeResponse(
                aluno.getNumeroMatricula(),
                aluno.getNomeCompleto(),
                valor,
                mensalidadeService.getDescricaoEstrategia(),
                aluno.obterQuantidadeInscricoes()
            ));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao calcular mensalidade: " + e.getMessage()));
        }
    }

    @GetMapping("/aluno/{matricula}/bolsista")
    @Operation(summary = "Calcular mensalidade bolsista",
               description = "Calcula mensalidade usando estratégia de bolsista (com desconto)")
    @Transactional(readOnly = true)
    public ResponseEntity<?> calcularMensalidadeBolsista(
            @Parameter(description = "Número de matrícula do aluno")
            @PathVariable int matricula) {
        try {
            Aluno aluno = buscarAluno(matricula);
            mensalidadeService.setEstrategia(bolsistaStrategy);
            double valor = mensalidadeService.calcularMensalidade(aluno);

            return ResponseEntity.ok(new MensalidadeResponse(
                aluno.getNumeroMatricula(),
                aluno.getNomeCompleto(),
                valor,
                mensalidadeService.getDescricaoEstrategia(),
                aluno.obterQuantidadeInscricoes()
            ));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao calcular mensalidade: " + e.getMessage()));
        }
    }

    @GetMapping("/aluno/{matricula}/comparar")
    @Operation(summary = "Comparar estratégias",
               description = "Compara valores de mensalidade usando todas as estratégias")
    @Transactional(readOnly = true)
    public ResponseEntity<?> compararEstrategias(
            @Parameter(description = "Número de matrícula do aluno")
            @PathVariable int matricula) {
        try {
            Aluno aluno = buscarAluno(matricula);

            mensalidadeService.setEstrategia(integralStrategy);
            double valorIntegral = mensalidadeService.calcularMensalidade(aluno);

            mensalidadeService.setEstrategia(meioPeriodoStrategy);
            double valorMeioPeriodo = mensalidadeService.calcularMensalidade(aluno);

            mensalidadeService.setEstrategia(bolsistaStrategy);
            double valorBolsista = mensalidadeService.calcularMensalidade(aluno);

            return ResponseEntity.ok(new ComparacaoResponse(
                aluno.getNumeroMatricula(),
                aluno.getNomeCompleto(),
                aluno.obterQuantidadeInscricoes(),
                valorIntegral,
                valorMeioPeriodo,
                valorBolsista
            ));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao comparar estratégias: " + e.getMessage()));
        }
    }

    private Aluno buscarAluno(int matricula) throws ServiceException {
        Aluno aluno = alunoRepository.findById(matricula)
                .orElseThrow(() -> new ServiceException(
                        ServiceException.ServiceExceptionEnum.ALUNO_NAO_ENCONTRADO,
                        "Aluno não encontrado"
                ));
        // Forçar carregamento da coleção lazy antes de retornar
        aluno.obterTodasInscricoes().size();
        return aluno;
    }

    // Inner classes para respostas
    private static class MensalidadeResponse {
        private int matricula;
        private String nomeAluno;
        private double valorMensalidade;
        private String estrategiaUtilizada;
        private int quantidadeInscricoes;

        public MensalidadeResponse(int matricula, String nomeAluno, double valorMensalidade,
                                  String estrategiaUtilizada, int quantidadeInscricoes) {
            this.matricula = matricula;
            this.nomeAluno = nomeAluno;
            this.valorMensalidade = valorMensalidade;
            this.estrategiaUtilizada = estrategiaUtilizada;
            this.quantidadeInscricoes = quantidadeInscricoes;
        }

        // Getters
        public int getMatricula() { return matricula; }
        public String getNomeAluno() { return nomeAluno; }
        public double getValorMensalidade() { return valorMensalidade; }
        public String getEstrategiaUtilizada() { return estrategiaUtilizada; }
        public int getQuantidadeInscricoes() { return quantidadeInscricoes; }
    }

    private static class ComparacaoResponse {
        private int matricula;
        private String nomeAluno;
        private int quantidadeInscricoes;
        private double valorIntegral;
        private double valorMeioPeriodo;
        private double valorBolsista;

        public ComparacaoResponse(int matricula, String nomeAluno, int quantidadeInscricoes,
                                 double valorIntegral, double valorMeioPeriodo, double valorBolsista) {
            this.matricula = matricula;
            this.nomeAluno = nomeAluno;
            this.quantidadeInscricoes = quantidadeInscricoes;
            this.valorIntegral = valorIntegral;
            this.valorMeioPeriodo = valorMeioPeriodo;
            this.valorBolsista = valorBolsista;
        }

        // Getters
        public int getMatricula() { return matricula; }
        public String getNomeAluno() { return nomeAluno; }
        public int getQuantidadeInscricoes() { return quantidadeInscricoes; }
        public double getValorIntegral() { return valorIntegral; }
        public double getValorMeioPeriodo() { return valorMeioPeriodo; }
        public double getValorBolsista() { return valorBolsista; }
    }

    private static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}

