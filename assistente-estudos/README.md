# Assistente Inteligente de Estudos

API REST em Java + Spring Boot para organizar matérias, tarefas e prazos de estudo.

## Como rodar

1. Crie o banco no PostgreSQL:
   ```sql
   CREATE DATABASE estudos;
   ```
2. (Opcional) Ajuste as variáveis `DB_URL`, `DB_USER`, `DB_PASSWORD` (padrões em `application.properties`).
3. Execute:
   ```bash
   mvn spring-boot:run
   ```
4. Swagger: http://localhost:8080/swagger-ui.html
5. Testes: `mvn test`

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/usuarios` | Cadastra estudante (senha com BCrypt) |
| GET | `/usuarios/{id}` | Busca estudante |
| POST/GET | `/materias` | Cria / lista (`?usuarioId=`) |
| GET/PUT/DELETE | `/materias/{id}` | Busca / edita / exclui (apaga as tarefas da matéria) |
| POST | `/tarefas` | Cria tarefa |
| GET | `/tarefas` | Lista com filtros `usuarioId`, `status`, `prioridade`, `materiaId`, `dataEntrega` |
| GET/PUT/DELETE | `/tarefas/{id}` | Busca / edita / exclui |
| PATCH | `/tarefas/{id}/status` | Altera status `{"status":"EM_ANDAMENTO"}` |
| PATCH | `/tarefas/{id}/concluir` | Marca como concluída |
| GET | `/tarefas/proximas-do-vencimento?usuarioId=1&dias=3` | Prazo próximo |
| GET | `/tarefas/atrasadas?usuarioId=1` | Tarefas atrasadas |
| GET | `/dashboard?usuarioId=1` | Resumo e progresso por matéria |

### Exemplo

```json
POST /tarefas
{
  "titulo": "Estudar SQL JOIN",
  "descricao": "INNER, LEFT e RIGHT",
  "materiaId": 1,
  "prioridade": "ALTA",
  "dataEntrega": "2026-10-15"
}
```

## Decisões

- Tarefas abertas com prazo vencido viram `ATRASADA` automaticamente a cada consulta.
- No dashboard, `pendentes` = `PENDENTE` + `EM_ANDAMENTO`.
- Ainda não há login: as rotas recebem `usuarioId`. Spring Security + JWT entram na etapa futura,
  quando o usuário passará a vir do token.
- Próximas etapas: integração com LLM, Docker, Redis.
