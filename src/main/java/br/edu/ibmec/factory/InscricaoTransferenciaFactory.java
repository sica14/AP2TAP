package br.edu.ibmec.factory;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.entity.Situacao;
import br.edu.ibmec.entity.Turma;
import org.springframework.stereotype.Component;

/**
 * Factory Pattern: Factory para criar inscrições de transferência.
 * Permite inicializar com notas e situação específicas.
 */
@Component
public class InscricaoTransferenciaFactory extends InscricaoFactory {

    private float nota1Transferencia = 0.0f;
    private float nota2Transferencia = 0.0f;
    private int faltasTransferencia = 0;

    /**
     * Define as notas e faltas do aluno transferido.
     */
    public void setDadosTransferencia(float nota1, float nota2, int faltas) {
        this.nota1Transferencia = nota1;
        this.nota2Transferencia = nota2;
        this.faltasTransferencia = faltas;
    }

    @Override
    public Inscricao criarInscricao(Aluno aluno, Turma turma) {
        // Valida se os dados de transferência foram configurados
        Inscricao inscricao = super.criarInscricao(aluno, turma);

        // Reseta os dados após criar a inscrição
        this.nota1Transferencia = 0.0f;
        this.nota2Transferencia = 0.0f;
        this.faltasTransferencia = 0;

        return inscricao;
    }

    @Override
    protected float getAvaliacaoInicial1() {
        return nota1Transferencia;
    }

    @Override
    protected float getAvaliacaoInicial2() {
        return nota2Transferencia;
    }

    @Override
    protected int getFaltasInicial() {
        return faltasTransferencia;
    }

    @Override
    protected Situacao getSituacaoInicial() {
        // Determina situação com base nas notas transferidas
        float media = (nota1Transferencia + nota2Transferencia) / 2;
        if (media >= 7.0f && faltasTransferencia <= 15) {
            return Situacao.aprovado;
        } else if (media < 3.0f || faltasTransferencia > 15) {
            return Situacao.reprovado;
        }
        return Situacao.cursando;
    }
}

