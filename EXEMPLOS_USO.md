# Guia de Uso - Design Patterns do Projeto

## Início Rápido

Este guia mostra como usar as funcionalidades implementadas com Design Patterns.

---

## 1. Calcular Mensalidade de Aluno (Strategy Pattern)

### Cenário: Você precisa calcular a mensalidade de um aluno

### Exemplo 1: Aluno de Período Integral

**Request:**
```http
GET http://localhost:8080/api/mensalidades/aluno/12345/integral
```

**Response esperada:**
```json
{
  "matricula": 12345,
  "nomeAluno": "João Silva",
  "valorMensalidade": 1400.0,
  "estrategiaUtilizada": "Mensalidade Integral - Valor base: R$ 800.0 + R$ 200.0 por disciplina",
  "quantidadeInscricoes": 3
}
```

**Cálculo:** 800 (base) + (3 disciplinas × 200) = R$ 1.400,00

---

### Exemplo 2: Aluno de Meio Período

**Request:**
```http
GET http://localhost:8080/api/mensalidades/aluno/12345/meio-periodo
```

**Response esperada:**
```json
{
  "matricula": 12345,
  "nomeAluno": "João Silva",
  "valorMensalidade": 1150.0,
  "estrategiaUtilizada": "Mensalidade Meio Período - Taxa administrativa: R$ 100.0 + R$ 350.0 por disciplina",
  "quantidadeInscricoes": 3
}
```

**Cálculo:** 100 (taxa) + (3 disciplinas × 350) = R$ 1.150,00

---

### Exemplo 3: Aluno Bolsista

**Request:**
```http
GET http://localhost:8080/api/mensalidades/aluno/12345/bolsista
```

**Response esperada:**
```json
{
  "matricula": 12345,
  "nomeAluno": "João Silva",
  "valorMensalidade": 700.0,
  "estrategiaUtilizada": "Mensalidade Bolsista - 50.0% de desconto sobre valor integral",
  "quantidadeInscricoes": 3
}
```

**Cálculo:** (800 + 3 × 200) × 0.5 = R$ 700,00

---

### Exemplo 4: Comparar Todas as Estratégias

**Request:**
```http
GET http://localhost:8080/api/mensalidades/aluno/12345/comparar
```

**Response esperada:**
```json
{
  "matricula": 12345,
  "nomeAluno": "João Silva",
  "quantidadeInscricoes": 3,
  "valorIntegral": 1400.0,
  "valorMeioPeriodo": 1150.0,
  "valorBolsista": 700.0
}
```

**Uso:** Útil para mostrar ao aluno todas as opções de pagamento disponíveis.

---

## 2. Matricular Aluno em Curso (Observer Pattern)

### Cenário: Matricular um novo aluno em um curso

**Request:**
```http
POST http://localhost:8080/api/matriculas/12345/curso/101
```

**Response esperada:**
```json
{
  "matricula": 12345,
  "nome": "João Silva",
  "idade": 20,
  "matriculaAtiva": true,
  "estadoCivil": {
    "estadoCivil": "solteiro"
  },
  "telefones": ["11999998888", "1133334444"]
}
```

**O que acontece internamente:**

1. **MatriculaService** executa a lógica de matrícula
2. **LogMatriculaObserver** registra no log:
   ```
   INFO - MATRÍCULA REALIZADA - Aluno: João Silva (Matrícula: 12345) 
          matriculado no curso: Engenharia de Software (Código: 101)
   ```
3. **EmailNotificacaoObserver** simula envio de email:
   ```
   INFO - EMAIL ENVIADO para aluno João Silva: 
          Prezado(a) João Silva,
          Sua matrícula no curso Engenharia de Software foi realizada com sucesso!
          Matrícula: 12345
          Código do Curso: 101
          Bem-vindo(a)!
   ```

---

### Cenário: Erro ao matricular aluno já matriculado

**Request:**
```http
POST http://localhost:8080/api/matriculas/12345/curso/102
```

**Response esperada (400 Bad Request):**
```json
{
  "error": "Aluno já está matriculado no curso: Engenharia de Software"
}
```

---

## 3. Desmatricular Aluno (Observer Pattern)

**Request:**
```http
DELETE http://localhost:8080/api/matriculas/12345
```

**Response esperada:**
```json
{
  "matricula": 12345,
  "nome": "João Silva",
  "idade": 20,
  "matriculaAtiva": false,
  "estadoCivil": {
    "estadoCivil": "solteiro"
  },
  "telefones": ["11999998888", "1133334444"]
}
```

**Logs gerados:**
```
INFO - DESMATRÍCULA REALIZADA - Aluno: João Silva (Matrícula: 12345) 
       desmatriculado do curso: Engenharia de Software (Código: 101)

INFO - EMAIL ENVIADO para aluno João Silva:
       Prezado(a) João Silva,
       Sua desmatrícula do curso Engenharia de Software foi processada.
       Matrícula: 12345
       Desejamos sucesso em sua jornada!
```

---

## 4. Transferir Aluno de Curso (Observer Pattern)

**Request:**
```http
PUT http://localhost:8080/api/matriculas/12345/transferir/102
```

**O que acontece:**
1. Desmatricula do curso atual (101)
2. Matricula no novo curso (102)
3. Notifica observadores duas vezes (desmatrícula + matrícula)

**Response esperada:**
```json
{
  "matricula": 12345,
  "nome": "João Silva",
  "idade": 20,
  "matriculaAtiva": true,
  "estadoCivil": {
    "estadoCivil": "solteiro"
  },
  "telefones": ["11999998888", "1133334444"]
}
```

---

## 5. Criar Inscrição em Turma (Factory Pattern)

### Uso em Código Java

#### Inscrição Regular (nova)

```java
@Autowired
private InscricaoRegularFactory regularFactory;

@Autowired
private AlunoRepository alunoRepository;

@Autowired
private TurmaRepository turmaRepository;

// Buscar entidades
Aluno aluno = alunoRepository.findById(12345).get();
Turma turma = turmaRepository.findById(new TurmaId(2024, "CS101", 1)).get();

// Criar inscrição usando Factory
Inscricao inscricao = regularFactory.criarInscricao(aluno, turma);

// Resultado:
// - avaliacao1: 0.0
// - avaliacao2: 0.0
// - numFaltas: 0
// - situacao: CURSANDO
```

---

#### Inscrição de Transferência

```java
@Autowired
private InscricaoTransferenciaFactory transferenciaFactory;

// Buscar entidades
Aluno aluno = alunoRepository.findById(67890).get();
Turma turma = turmaRepository.findById(new TurmaId(2024, "CS101", 1)).get();

// Configurar dados de transferência
transferenciaFactory.setDadosTransferencia(8.0f, 7.5f, 3);

// Criar inscrição com histórico
Inscricao inscricao = transferenciaFactory.criarInscricao(aluno, turma);

// Resultado:
// - avaliacao1: 8.0
// - avaliacao2: 7.5
// - media: 7.75
// - numFaltas: 3
// - situacao: APROVADO (média >= 7.0 e faltas <= 15)
```

---

## 6. Exemplos Completos de Fluxo

### Fluxo 1: Novo Aluno

```bash
# 1. Criar aluno (endpoint existente)
curl -X POST http://localhost:8080/api/alunos \
  -H "Content-Type: application/json" \
  -d '{
    "matricula": 20001,
    "nome": "Maria Santos",
    "idade": 19,
    "estadoCivil": {"estadoCivil": "solteiro"},
    "matriculaAtiva": true,
    "telefones": ["11987654321"]
  }'

# 2. Matricular em curso (novo endpoint - Observer Pattern)
curl -X POST http://localhost:8080/api/matriculas/20001/curso/101

# 3. Calcular mensalidade integral (novo endpoint - Strategy Pattern)
curl http://localhost:8080/api/mensalidades/aluno/20001/integral

# 4. Comparar opções de mensalidade
curl http://localhost:8080/api/mensalidades/aluno/20001/comparar
```

---

### Fluxo 2: Aluno Bolsista

```bash
# 1. Aluno já existe com matrícula 20001

# 2. Ver mensalidade como bolsista
curl http://localhost:8080/api/mensalidades/aluno/20001/bolsista

# Resposta mostra 50% de desconto
```

---

### Fluxo 3: Transferência de Curso

```bash
# 1. Aluno matriculado no curso 101

# 2. Transferir para curso 102
curl -X PUT http://localhost:8080/api/matriculas/20001/transferir/102

# 3. Verificar logs no console
# - Verá notificação de desmatrícula do curso 101
# - Verá notificação de matrícula no curso 102
# - Verá simulação de 2 emails enviados
```

---

## 7. Testando via Swagger

Acesse: `http://localhost:8080/swagger-ui.html`

### Seção "Matrícula"
- `POST /api/matriculas/{matriculaAluno}/curso/{codigoCurso}`
- `DELETE /api/matriculas/{matriculaAluno}`
- `PUT /api/matriculas/{matriculaAluno}/transferir/{novoCodigoCurso}`

### Seção "Mensalidade"
- `GET /api/mensalidades/aluno/{matricula}/integral`
- `GET /api/mensalidades/aluno/{matricula}/meio-periodo`
- `GET /api/mensalidades/aluno/{matricula}/bolsista`
- `GET /api/mensalidades/aluno/{matricula}/comparar`

---

## 8. Código Java - Extensão Futura

### Adicionar Nova Estratégia de Mensalidade

```java
@Component
public class MensalidadeVeteranoStrategy implements MensalidadeStrategy {
    
    private static final double VALOR_BASE = 800.0;
    private static final double VALOR_POR_DISCIPLINA = 200.0;
    private static final double DESCONTO_POR_ANO = 0.05; // 5% por ano
    
    @Override
    public double calcularMensalidade(Aluno aluno) {
        int anosMatriculado = calcularAnosMatriculado(aluno);
        int quantidadeInscricoes = aluno.obterQuantidadeInscricoes();
        
        double valorTotal = VALOR_BASE + (quantidadeInscricoes * VALOR_POR_DISCIPLINA);
        double desconto = Math.min(anosMatriculado * DESCONTO_POR_ANO, 0.30); // max 30%
        
        return valorTotal * (1 - desconto);
    }
    
    @Override
    public String getDescricao() {
        return "Mensalidade Veterano - Desconto progressivo por tempo de casa";
    }
    
    private int calcularAnosMatriculado(Aluno aluno) {
        // Implementar lógica
        return 0;
    }
}
```

---

### Adicionar Novo Observer

```java
@Component
public class SMSNotificacaoObserver implements MatriculaObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(SMSNotificacaoObserver.class);
    
    @Override
    public void onAlunoMatriculado(Aluno aluno, Curso curso) {
        String telefone = aluno.getNumerosTelefone().isEmpty() 
            ? "Sem telefone" 
            : aluno.getNumerosTelefone().get(0);
            
        String mensagem = String.format(
            "Olá %s! Sua matrícula no curso %s foi confirmada. Bem-vindo!",
            aluno.getNomeCompleto(),
            curso.getNomeCurso()
        );
        
        enviarSMS(telefone, mensagem);
    }
    
    @Override
    public void onAlunoDesmatriculado(Aluno aluno, Curso curso) {
        // Similar
    }
    
    private void enviarSMS(String telefone, String mensagem) {
        // Integração com API de SMS (Twilio, etc)
        logger.info("SMS ENVIADO para {}: {}", telefone, mensagem);
    }
}
```

**Registrar o novo observer:**
```java
@PostConstruct
public void init() {
    matriculaService.addObserver(logObserver);
    matriculaService.addObserver(emailObserver);
    matriculaService.addObserver(smsObserver); // Novo!
}
```

---

## 9. Casos de Erro

### Aluno não encontrado
```http
GET http://localhost:8080/api/mensalidades/aluno/99999/integral

Response: 404 Not Found
{
  "error": "Aluno não encontrado"
}
```

### Curso não encontrado
```http
POST http://localhost:8080/api/matriculas/12345/curso/99999

Response: 400 Bad Request
{
  "error": "Curso não encontrado"
}
```

### Aluno já matriculado
```http
POST http://localhost:8080/api/matriculas/12345/curso/101

Response: 400 Bad Request
{
  "error": "Aluno já está matriculado no curso: Engenharia de Software"
}
```

### Aluno não matriculado (tentar desmatricular)
```http
DELETE http://localhost:8080/api/matriculas/12345

Response: 400 Bad Request
{
  "error": "Aluno não está matriculado em nenhum curso"
}
```

---

## 10. Checklist de Testes

- [ ] Calcular mensalidade integral
- [ ] Calcular mensalidade meio período
- [ ] Calcular mensalidade bolsista
- [ ] Comparar todas as estratégias
- [ ] Matricular aluno em curso
- [ ] Verificar logs de matrícula
- [ ] Verificar simulação de email
- [ ] Desmatricular aluno
- [ ] Transferir aluno de curso
- [ ] Testar erros (aluno não encontrado, curso não encontrado, etc.)

---

## Contato

Para dúvidas sobre os Design Patterns implementados, consulte o arquivo `DESIGN_PATTERNS.md`.

