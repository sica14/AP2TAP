package br.edu.ibmec.exception;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class ServiceException extends Exception {
    private String message;
    private ServiceExceptionEnum tipo;

    /**
     * Exceção de regra de negócio da aplicação com suporte a enumeração tipada
     * e mensagem amigável.
     */

    public ServiceException() {

    }

    public ServiceException(String msg) {
        super(msg);
        this.message = msg;
    }

    public ServiceException(ServiceExceptionEnum tipo) {
        this.tipo = tipo;
        this.message = (tipo != null) ? tipo.getDescricao() : null;
    }

    public ServiceException(ServiceExceptionEnum tipo, String customMessage) {
        this.tipo = tipo;
        this.message = customMessage;
    }

    public ServiceException(ArrayList<String> listaErrosCurso) {
        this.message = String.join(", ", listaErrosCurso);
    }

    public String getMessage() {
        if (message != null && !message.isEmpty()) {
            return message;
        }
        if (tipo != null && tipo.getDescricao() != null && !tipo.getDescricao().isEmpty()) {
            return tipo.getDescricao();
        }
        return super.getMessage();
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public ServiceExceptionEnum getTipo() {
        return tipo;
    }

    public void setTipo(ServiceExceptionEnum tipo) {
        this.tipo = tipo;
    }

    public enum ServiceExceptionEnum {

        CURSO_CODIGO_INVALIDO("Código de curso inválido"),
        CURSO_NOME_INVALIDO("Nome de curso inválido"),
        CURSO_CODIGO_DUPLICADO("Código de curso já existe"),
        CURSO_NAO_ENCONTRADO("Curso não encontrado"),
        ALUNO_MATRICULA_INVALIDA("Matrícula de aluno inválida"),
        ALUNO_NOME_INVALIDO("Nome de aluno inválido"),
        ALUNO_NAO_ENCONTRADO("Aluno não encontrado"),
        ALUNO_JA_MATRICULADO("Aluno já está matriculado em um curso"),
        ALUNO_NAO_MATRICULADO("Aluno não está matriculado em nenhum curso");

        private String descricao;
        
        private ServiceExceptionEnum() {
            this.descricao = "";
        }

        private ServiceExceptionEnum(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }

    }
}