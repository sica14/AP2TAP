# AP2 — Entrega Individual

Resolvendo 3 problemas com Design Patterns, conforme solicitado:
- Inscrição em turma (Factory Pattern)
- Matrícula do aluno em curso (Observer Pattern)
- Cálculo da mensalidade (Strategy Pattern)

Esta página é um resumo objetivo só do que é novo/necessário para a AP2.

---

## Como executar e testar

1) Inicie a aplicação (porta 8080)
```
./mvnw spring-boot:run
```

2) Acesse o Swagger UI
```
http://localhost:8080/swagger-ui.html
```

3) Para ter dados mínimos, crie na ordem (exemplos):
- POST /api/curso
```
{
  "codigoCurso": 1,
  "nomeCurso": "Engenharia"
}
```
- POST /api/disciplina
```
{
  "codigo": 1,
  "nome": "Português",
  "curso": 1
}
```
- POST /api/professores (opcional para AP2, mas disponível)
```
{
  "codigo": 1,
  "nome": "Thiago"
}
```
- POST /api/turma (usa códigos numéricos)
```
{
  "codigo": 1,
  "ano": 2025,
  "semestre": 1,
  "disciplina": 1,
  "professor": 1
}
```
- POST /api/aluno (matriculado em curso 1)
```
{
  "numeroMatricula": 1,
  "nomeCompleto": "Maria Silva",
  "dataNascimento": {"dia": 15, "mes": 3, "ano": 2000},
  "estadoCivilAtual": "SOLTEIRO",
  "curso": 1
}
```

Obs.: nos JSONs, curso/disciplinas/professor são códigos numéricos (não nomes).

---

## 1) Inscrição (Factory Pattern)

Padrão: Factory + Template Method
- InscricaoRegularFactory cria inscrição com notas/faltas zeradas e situação CURSANDO
- InscricaoTransferenciaFactory cria inscrição com dados existentes (notas/faltas)

Endpoints:
- Criar inscrição REGULAR
```
POST /api/inscricao/regular/{matricula}/{codigo}/{ano}/{semestre}
Ex.: /api/inscricao/regular/1/1/2025/1
```
- Criar inscrição de TRANSFERÊNCIA
```
POST /api/inscricao/transferencia/{matricula}/{codigo}/{ano}/{semestre}
Body:
{
  "avaliacao1": 7.5,
  "avaliacao2": 8.0,
  "faltas": 2
}
```

Resultados esperados (Regular):
- avaliacao1 = 0.0
- avaliacao2 = 0.0
- numFaltas = 0
- situacao = CURSANDO

Validações:
- Aluno e turma devem existir
- Impede duplicidade de inscrição para a mesma (matricula/codigo/ano/semestre)

---

## 2) Matrícula do aluno em curso (Observer Pattern)

Padrão: Observer
- Ao matricular/desmatricular/transferir, observadores são notificados
- Implementações: LogMatriculaObserver e EmailNotificacaoObserver

Endpoints:
- Matricular aluno em curso
```
POST /api/matriculas/{matriculaAluno}/curso/{codigoCurso}
Ex.: /api/matriculas/1/curso/1
```
- Desmatricular aluno
```
DELETE /api/matriculas/{matriculaAluno}
Ex.: /api/matriculas/1
```
- Transferir aluno de curso
```
PUT /api/matriculas/{matriculaAluno}/transferir/{novoCodigoCurso}
Ex.: /api/matriculas/1/transferir/2
```

Efeitos colaterais esperados (logs):
- Log informativo da matrícula/desmatrícula/transferência
- Simulação de envio de e-mail de notificação

Validações:
- Aluno/curso existem
- Não permite matricular aluno já matriculado
- Não permite desmatricular quem não está matriculado

---

## 3) Cálculo da Mensalidade (Strategy Pattern)

Padrão: Strategy
- MensalidadeIntegralStrategy
- MensalidadeMeioPeriodoStrategy
- MensalidadeBolsistaStrategy

Endpoints:
- Integral
```
GET /api/mensalidades/aluno/{matricula}/integral
```
- Meio Período
```
GET /api/mensalidades/aluno/{matricula}/meio-periodo
```
- Bolsista
```
GET /api/mensalidades/aluno/{matricula}/bolsista
```
- Comparar todas
```
GET /api/mensalidades/aluno/{matricula}/comparar
```

Resposta típica (comparar):
```
{
  "matricula": 1,
  "nomeAluno": "Maria Silva",
  "quantidadeInscricoes": 1,
  "valorIntegral": 1000.0,
  "valorMeioPeriodo": 500.0,
  "valorBolsista": 300.0
}
```

Observações técnicas:
- Para evitar LazyInitializationException, a coleção de inscrições do aluno é carregada na transação antes do cálculo
- O valor final depende do número de inscrições do aluno

---

## Demonstração (checklist rápido)

1) Criar Curso, Disciplina, (Professor opcional) e Turma
2) Criar Aluno (já com curso)
3) Criar Inscrição Regular em uma Turma
4) Calcular Mensalidade (todas as estratégias)
5) Testar Matrícula em Curso, Desmatricular e Transferir (ver logs/"e-mails")

---

## O que foi implementado (resumo)

- Inscrição com Factory Pattern (Regular e Transferência)
- Matrícula em curso com Observer Pattern (log e e-mail simulados)
- Mensalidade com Strategy Pattern (integral, meio período, bolsista)
- Endpoints REST e validações essenciais
- Ajustes para evitar problemas de Lazy Loading no cálculo de mensalidade

Extras úteis no repositório:
- `EXEMPLOS_USO.md` — exemplos completos de chamadas
- `README.md` — visão geral do projeto

