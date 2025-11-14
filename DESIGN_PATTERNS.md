# Design Patterns - Projeto Universidade

## Visão Geral

Este documento explica os Design Patterns implementados para resolver os três problemas principais do projeto:

1. **Inscrição** - Criação e gerenciamento de inscrições de alunos em turmas
2. **Matrícula do aluno em curso** - Processo de matricular/desmatricular alunos em cursos
3. **Cálculo da mensalidade** - Diferentes formas de calcular mensalidade baseadas no tipo de aluno

---

## 1. Strategy Pattern - Cálculo de Mensalidade

### Problema
Diferentes tipos de alunos pagam mensalidades calculadas de formas diferentes:
- Alunos de período integral
- Alunos de meio período
- Alunos bolsistas

### Solução
O **Strategy Pattern** permite trocar o algoritmo de cálculo dinamicamente em tempo de execução.

### Implementação

#### Interface Strategy
```java
// MensalidadeStrategy.java
public interface MensalidadeStrategy {
    double calcularMensalidade(Aluno aluno);
    String getDescricao();
}
```

#### Estratégias Concretas

1. **MensalidadeIntegralStrategy**
   - Valor base: R$ 800,00
   - Valor adicional por disciplina: R$ 200,00
   - Fórmula: `800 + (numDisciplinas * 200)`

2. **MensalidadeMeioPeriodoStrategy**
   - Taxa administrativa: R$ 100,00
   - Valor por disciplina: R$ 350,00
   - Fórmula: `100 + (numDisciplinas * 350)`

3. **MensalidadeBolsistaStrategy**
   - Usa o cálculo integral com 50% de desconto
   - Fórmula: `(800 + (numDisciplinas * 200)) * 0.5`

#### Service
```java
// MensalidadeService.java
@Service
public class MensalidadeService {
    private MensalidadeStrategy estrategiaAtual;
    
    public void setEstrategia(MensalidadeStrategy estrategia) {
        this.estrategiaAtual = estrategia;
    }
    
    public double calcularMensalidade(Aluno aluno) {
        return estrategiaAtual.calcularMensalidade(aluno);
    }
}
```

### Endpoints REST

- `GET /api/mensalidades/aluno/{matricula}/integral` - Calcula mensalidade integral
- `GET /api/mensalidades/aluno/{matricula}/meio-periodo` - Calcula mensalidade meio período
- `GET /api/mensalidades/aluno/{matricula}/bolsista` - Calcula mensalidade bolsista
- `GET /api/mensalidades/aluno/{matricula}/comparar` - Compara todas as estratégias

### Vantagens
- ✅ Fácil adicionar novas estratégias sem modificar código existente
- ✅ Permite trocar o algoritmo em tempo de execução
- ✅ Elimina condicionais complexos (if/else)
- ✅ Cada estratégia é uma classe independente e testável

---

## 2. Observer Pattern - Notificações de Matrícula

### Problema
Quando um aluno é matriculado ou desmatriculado, várias ações devem ser executadas:
- Registrar log no sistema
- Enviar email de notificação
- Atualizar estatísticas
- Enviar SMS (futuro)

### Solução
O **Observer Pattern** permite notificar múltiplos objetos quando uma matrícula acontece, sem acoplar o código.

### Implementação

#### Interface Observer
```java
// MatriculaObserver.java
public interface MatriculaObserver {
    void onAlunoMatriculado(Aluno aluno, Curso curso);
    void onAlunoDesmatriculado(Aluno aluno, Curso curso);
}
```

#### Observadores Concretos

1. **LogMatriculaObserver**
   - Registra em log todas as matrículas/desmatrículas
   - Usa SLF4J Logger

2. **EmailNotificacaoObserver**
   - Simula envio de email para o aluno
   - Notifica sobre matrícula ou desmatrícula

#### Subject (MatriculaService)
```java
@Service
public class MatriculaService {
    private List<MatriculaObserver> observers = new ArrayList<>();
    
    public void addObserver(MatriculaObserver observer) {
        observers.add(observer);
    }
    
    public Aluno matricularAlunoEmCurso(int matricula, int codigoCurso) {
        // ... lógica de matrícula ...
        notifyMatricula(aluno, curso);
        return aluno;
    }
    
    private void notifyMatricula(Aluno aluno, Curso curso) {
        for (MatriculaObserver observer : observers) {
            observer.onAlunoMatriculado(aluno, curso);
        }
    }
}
```

### Endpoints REST

- `POST /api/matriculas/{matriculaAluno}/curso/{codigoCurso}` - Matricular aluno
- `DELETE /api/matriculas/{matriculaAluno}` - Desmatricular aluno
- `PUT /api/matriculas/{matriculaAluno}/transferir/{novoCodigoCurso}` - Transferir aluno

### Vantagens
- ✅ Desacoplamento entre a matrícula e as notificações
- ✅ Fácil adicionar novos observadores (SMS, WhatsApp, etc.)
- ✅ Observadores podem ser adicionados/removidos dinamicamente
- ✅ Cada observador é independente e testável

---

## 3. Factory Pattern - Criação de Inscrições

### Problema
Criar inscrições com diferentes configurações iniciais:
- Inscrição regular (nova)
- Inscrição de transferência (com notas existentes)
- Inscrição de dependência

### Solução
O **Factory Pattern** (combinado com **Template Method**) encapsula a lógica de criação de inscrições.

### Implementação

#### Factory Abstrata
```java
// InscricaoFactory.java
public abstract class InscricaoFactory {
    public Inscricao criarInscricao(Aluno aluno, Turma turma) {
        validarParametros(aluno, turma);
        
        Inscricao inscricao = new Inscricao(
            getAvaliacaoInicial1(),
            getAvaliacaoInicial2(),
            getFaltasInicial(),
            getSituacaoInicial(),
            aluno,
            turma
        );
        
        configurarInscricao(inscricao);
        return inscricao;
    }
    
    // Template Methods
    protected abstract float getAvaliacaoInicial1();
    protected abstract float getAvaliacaoInicial2();
    protected abstract int getFaltasInicial();
    protected abstract Situacao getSituacaoInicial();
}
```

#### Factories Concretas

1. **InscricaoRegularFactory**
   - Notas iniciais: 0.0
   - Faltas iniciais: 0
   - Situação: CURSANDO

2. **InscricaoTransferenciaFactory**
   - Permite configurar notas e faltas do histórico anterior
   - Calcula situação baseada nas notas transferidas
   - Usa método `setDadosTransferencia(nota1, nota2, faltas)`

### Uso
```java
@Autowired
private InscricaoRegularFactory regularFactory;

// Criar inscrição regular
Inscricao inscricao = regularFactory.criarInscricao(aluno, turma);

// Criar inscrição de transferência
InscricaoTransferenciaFactory transferenciaFactory = new InscricaoTransferenciaFactory();
transferenciaFactory.setDadosTransferencia(8.0f, 7.5f, 3);
Inscricao inscricaoTransf = transferenciaFactory.criarInscricao(aluno, turma);
```

### Vantagens
- ✅ Encapsula lógica complexa de criação
- ✅ Fácil adicionar novos tipos de inscrição
- ✅ Garante que inscrições sejam criadas corretamente
- ✅ Template Method evita duplicação de código

---

## 4. Template Method Pattern

### Problema
O processo de criação de inscrições tem etapas comuns, mas alguns detalhes variam.

### Solução
Já implementado dentro do **Factory Pattern** acima.

### Estrutura
```
criarInscricao() [template method]
    ├── validarParametros() [comum]
    ├── getAvaliacaoInicial1() [abstrato - varia]
    ├── getAvaliacaoInicial2() [abstrato - varia]
    ├── getFaltasInicial() [abstrato - varia]
    ├── getSituacaoInicial() [abstrato - varia]
    └── configurarInscricao() [hook - opcional]
```

---

## Resumo dos Patterns Aplicados

| Pattern | Onde foi usado | Problema resolvido |
|---------|----------------|-------------------|
| **Strategy** | Cálculo de mensalidade | Múltiplos algoritmos de cálculo |
| **Observer** | Matrícula de alunos | Notificações descopladas |
| **Factory** | Criação de inscrições | Encapsular criação complexa |
| **Template Method** | Dentro do Factory | Reutilizar código com variações |

---

## Como Testar

### 1. Testar Mensalidade (Strategy Pattern)

```bash
# Calcular mensalidade integral
curl http://localhost:8080/api/mensalidades/aluno/1/integral

# Calcular mensalidade meio período
curl http://localhost:8080/api/mensalidades/aluno/1/meio-periodo

# Calcular mensalidade bolsista
curl http://localhost:8080/api/mensalidades/aluno/1/bolsista

# Comparar todas as estratégias
curl http://localhost:8080/api/mensalidades/aluno/1/comparar
```

### 2. Testar Matrícula (Observer Pattern)

```bash
# Matricular aluno em curso
curl -X POST http://localhost:8080/api/matriculas/1/curso/1

# Desmatricular aluno
curl -X DELETE http://localhost:8080/api/matriculas/1

# Transferir aluno para outro curso
curl -X PUT http://localhost:8080/api/matriculas/1/transferir/2
```

### 3. Verificar Logs dos Observadores

Após executar as operações de matrícula, verifique o console do Spring Boot para ver:
- Logs de matrícula (LogMatriculaObserver)
- Simulação de emails enviados (EmailNotificacaoObserver)

---

## Estrutura de Pacotes

```
br.edu.ibmec
├── strategy/                    # Strategy Pattern
│   ├── MensalidadeStrategy.java
│   ├── MensalidadeIntegralStrategy.java
│   ├── MensalidadeMeioPeriodoStrategy.java
│   └── MensalidadeBolsistaStrategy.java
│
├── observer/                    # Observer Pattern
│   ├── MatriculaObserver.java
│   ├── LogMatriculaObserver.java
│   └── EmailNotificacaoObserver.java
│
├── factory/                     # Factory + Template Method
│   ├── InscricaoFactory.java
│   ├── InscricaoRegularFactory.java
│   └── InscricaoTransferenciaFactory.java
│
├── service/
│   ├── MensalidadeService.java  # Usa Strategy
│   └── MatriculaService.java    # Usa Observer
│
└── controller/
    ├── MensalidadeController.java
    └── MatriculaController.java
```

---

## Benefícios da Solução

### Manutenibilidade
- Código organizado em responsabilidades claras
- Fácil localizar e modificar funcionalidades

### Extensibilidade
- Novos tipos de mensalidade: criar nova Strategy
- Novas notificações: criar novo Observer
- Novos tipos de inscrição: criar nova Factory

### Testabilidade
- Cada classe tem responsabilidade única
- Mocks podem ser facilmente criados
- Testes unitários isolados

### SOLID Principles
- **S**ingle Responsibility: Cada classe tem um propósito
- **O**pen/Closed: Aberto para extensão, fechado para modificação
- **L**iskov Substitution: Todas as strategies são intercambiáveis
- **I**nterface Segregation: Interfaces focadas e específicas
- **D**ependency Inversion: Depende de abstrações, não implementações

---

## Próximos Passos (Melhorias Futuras)

1. **Adicionar mais Strategies**
   - MensalidadeVeterano (desconto por tempo de casa)
   - MensalidadeAtleta (desconto para atletas)

2. **Adicionar mais Observers**
   - SMSNotificacaoObserver
   - WhatsAppNotificacaoObserver
   - DashboardObserver (atualizar estatísticas)

3. **Adicionar mais Factories**
   - InscricaoDependenciaFactory
   - InscricaoEspecialFactory

4. **Implementar Chain of Responsibility**
   - Para validação de inscrições em etapas

5. **Implementar Decorator**
   - Para adicionar taxas extras na mensalidade (material, laboratório, etc.)

---

## Referências

- Gang of Four - Design Patterns: Elements of Reusable Object-Oriented Software
- Head First Design Patterns
- Refactoring Guru - https://refactoring.guru/design-patterns

---

**Desenvolvido para:** Projeto AP2 - TAP Refactoring 2025/23  
**Patterns:** Strategy, Observer, Factory, Template Method

