package br.edu.ibmec.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um professor que leciona disciplinas em turmas.
 */
@Entity
@Table(name = "professores")
@Getter
@NoArgsConstructor
public class Professor {

    private static final int CODIGO_MINIMO = 1;
    private static final int NOME_TAMANHO_MAXIMO = 60;

    @Id
    @Column(name = "codigo")
    @Setter(AccessLevel.NONE)
    private int codigo;

    @Column(name = "nome", nullable = false, length = NOME_TAMANHO_MAXIMO)
    @Setter
    private String nome;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Setter(AccessLevel.NONE)
    private List<Turma> turmas = new ArrayList<>();

    public Professor(int codigo, String nome) {
        setCodigo(codigo);
        setNome(nome);
    }

    // Métodos de negócio para turmas
    public void adicionarTurma(Turma turma) {
        validarObjetoNaoNulo(turma, "Turma");
        if (!turmas.contains(turma)) {
            turmas.add(turma);
            turma.setProfessor(this);
        }
    }

    public void removerTurma(Turma turma) {
        if (isObjetoValido(turma)) {
            turmas.remove(turma);
            turma.setProfessor(null);
        }
    }

    // Setter customizado com validação para código
    public void setCodigo(int codigo) {
        if (codigo < CODIGO_MINIMO) {
            throw new IllegalArgumentException("Código deve ser um número positivo");
        }
        this.codigo = codigo;
    }

    // Setter customizado com validação para nome
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (nome.length() > NOME_TAMANHO_MAXIMO) {
            throw new IllegalArgumentException("Nome deve ter no máximo " + NOME_TAMANHO_MAXIMO + " caracteres");
        }
        this.nome = nome.trim();
    }

    // Métodos de acesso às listas
    public List<Turma> getTurmas() {
        return new ArrayList<>(turmas);
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = criarListaSegura(turmas);
    }

    // Métodos utilitários privados
    private void validarObjetoNaoNulo(Object objeto, String nomeObjeto) {
        if (objeto == null) {
            throw new IllegalArgumentException(nomeObjeto + " não pode ser nulo");
        }
    }

    private boolean isObjetoValido(Object objeto) {
        return objeto != null;
    }

    private <T> List<T> criarListaSegura(List<T> lista) {
        return lista != null ? new ArrayList<>(lista) : new ArrayList<>();
    }

    // Métodos de consulta
    public int getQuantidadeTurmas() {
        return turmas.size();
    }

    public boolean possuiTurma(Turma turma) {
        return isObjetoValido(turma) && turmas.contains(turma);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", quantidadeTurmas=" + turmas.size() +
                '}';
    }
}

