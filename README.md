# catalogs

Suba a infraestrutura de observabilidade:

```bash
docker compose up -d
```

Para executar a aplicação no host usando OTLP/HTTP:

```bash
export OTEL_METRIC_EXPORT_INTERVAL=10000
export OTEL_RESOURCE_ATTRIBUTES=service.name=catalog
export OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318
curl -L -O https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar
java -javaagent:opentelemetry-javaagent.jar -jar app/target/catalog-0.0.1-SNAPSHOT.jar
```

O endpoint deve ser a raiz `http://localhost:4318`; o agente acrescenta `/v1/traces`,
`/v1/metrics` e `/v1/logs` por sinal. O Collector encaminha traces para o Tempo,
métricas para o Prometheus e logs para o Loki.

Se logs não forem necessários, desabilite somente a exportação de logs:

```bash
export OTEL_LOGS_EXPORTER=none
```
