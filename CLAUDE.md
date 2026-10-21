# CLAUDE.md - Diretrizes para o Projeto (Java 25 / Spring Boot 4)

Este documento orienta o assistente de IA (Claude) e os desenvolvedores sobre a arquitetura, padrões de código, comandos e boas práticas deste microsserviço.

---

## 🛠️ Comandos de Build e Execução

- **Compilar o projeto:** `./mvnw clean compile`
- **Executar testes unitários:** `./mvnw test`
- **Executar testes de integração:** `./mvnw verify`
- **Executar a aplicação localmente:** `./mvnw spring-boot:run`
- **Gerar relatório de cobertura (JaCoCo):** `./mvnw test jacoco:report`

---

## 🏗️ Arquitetura e Organização de Pacotes

O projeto segue estritamente os princípios da **Arquitetura Hexagonal (Ports and Adapters)** combinados com **Domain-Driven Design (DDD)**.

```text
br.com.kumabe.insurance.catalog
├── domain             # Lógica de negócio pura (Modelos de Domínio, Value Objects, Domain Events)
│   ├── model          # Entidades e agregados de negócio
│   └── exception      # Exceções de negócio customizadas
├── ports              # Contratos (Interfaces)
│   ├── inbound        # Casos de uso / Primary Ports (ex: UseCase interfaces)
│   └── outbound       # Gateways / Secondary Ports (ex: Repositories, Client interfaces)
└── adapters           # Adaptadores tecnológicos (Implementações dos ports)
    ├── inbound        # Entradas (ex: RestControllers, Consumers de mensageria)
    └── outbound       # Saídas (ex: Spring Data JPA, WebClients para APIs externas)

```

### Regras de Ouro da Arquitetura:

1. **O Domínio é o núcleo isolado:** A pasta `domain` **nunca** deve importar dependências de frameworks externos (Spring Data, Web, JPA, etc.).
2. **Inversão de Dependência:** As camadas externas (`adapters`) conhecem o núcleo (`domain`), mas o núcleo nunca conhece as tecnologias externas.
3. **Imutabilidade:** Prefira records (`record`) para DTOs, Value Objects e eventos sempre que aplicável.

---

## ☕ Padrões de Código para Java 25 & Spring Boot 4

* **Versão do Java:** Utilize recursos modernos do Java 25 (como pattern matching avançado, virtual threads nativas habilitadas, records e text blocks).
* **Injeção de Dependências:** **Sempre** utilize injeção via construtor (evite `@Autowired` em campos). Torne os campos `private final`.
* **Gerenciamento de Erros:**
* Utilize um `@RestControllerAdvice` global para capturar exceções de negócio e técnicas.
* Retorne payloads padronizados seguindo o padrão **RFC 7807 (Problem Details)**, nativo no Spring Framework.


* **Assincronicidade / Concorrência:** Aproveite as **Virtual Threads** do Java configuradas globalmente (`spring.threads.virtual.enabled=true`). Evite bloqueios desnecessários de threads OS em operações I/O bound.

---

## 🌐 Microsserviços e Boas Práticas de Resiliência

1. **Comunicação entre Microsserviços:**
* Para chamadas HTTP síncronas, utilize o `RestClient` ou `WebClient` reativo do Spring com timeouts configurados explicitamente.
* Aplique padrões de resiliência (**Circuit Breaker**, **Retry** e **Rate Limiter** via Resilience4j) em todas as chamadas de integração externa.


2. **Observabilidade (SRE & OpenTelemetry):**
* **Nunca** utilize `System.out.println` ou logger direto sem contexto. Utilize o SLF4J com Lombok (`@Slf4j`).
* Garanta a propagação automática de rastreio (`trace_id` e `span_id`) utilizando **OpenTelemetry** e Micrometer.
* Exponha métricas padronizadas no endpoint `/actuator/prometheus`.


3. **Resiliência e Configuração:**
* Mantenha configurações sensíveis ou específicas de ambiente injetadas via variáveis de ambiente (`ConfigMap`/Secrets em ambientes Kubernetes/EKS).



---

## 🧪 Testes Automatizados

* **Testes Unitários:** Obrigatórios para toda a camada de domínio e casos de uso (`domain` e `ports`). Utilize JUnit 5 e Mockito.
* **Testes de Integração:** Utilize **Testcontainers** para subir dependências reais (PostgreSQL, Redis, Kafka) em testes de integração de repositórios e adaptadores.
* **Cobertura Mínima:** O projeto exige cobertura de testes unitários superior a **80%** medida pelo JaCoCo.

---

## 🔒 Segurança e Boas Práticas (Security by Design)

* Valide todos os payloads de entrada utilizando Bean Validation (`@Valid`, `@NotNull`, `@Size`, etc.) nos DTOs de borda.
* Nunca exponha detalhes internos de exceções ou stack traces nas respostas HTTP de produção.
* Garanta autenticação/autorização baseada em tokens JWT propagados via headers de API Gateway / mTLS conforme o padrão da malha de serviços.

```