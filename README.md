# Payment Service

API REST de pagos de demostración con Java 17, Spring Boot 3.5 y H2 en memoria.

Si participas como proveedor de AppSec, consulta [GUIA_PROVEEDOR.md](GUIA_PROVEEDOR.md).

## Requisitos

- JDK 17
- Maven 3.9 o superior

## Ejecutar

```bash
mvn test
mvn spring-boot:run
```

La base de datos H2 se crea al iniciar la aplicación y se descarta al detenerla.

## API

Crear un pago:

```bash
curl -i -X POST http://localhost:8080/api/payments \
  -H 'Content-Type: application/json' \
  -d '{"merchantId":"merchant-demo","amount":12.50,"currency":"USD"}'
```

Consultarlo con el `id` devuelto:

```bash
curl -i http://localhost:8080/api/payments/UUID_DEL_PAGO
```

### Endpoints disponibles

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/payments` | Registra un pago. |
| `GET` | `/api/payments/{id}` | Consulta un pago por identificador. |
| `GET` | `/api/payments/search` | Busca pagos por estado y comercio. |
| `GET` | `/api/payments/search/portal` | Variante usada por el portal interno. |
| `GET` | `/api/payments/invoices` | Facturación por comercio, fecha y moneda. |
| `GET` | `/api/payments/refunds` | Devoluciones por comercio y monto mínimo. |
| `GET` | `/api/payments/settlements/{bucket}` | Resumen de liquidación por estado. |
| `GET` | `/api/legacy/reports` | Reporte heredado por comercio. |
| `GET` | `/api/payments/receipts` | Descarga de comprobantes. |
| `POST` | `/api/payments/archive` | Archiva una carga útil y devuelve su nombre. |
| `GET` | `/api/payments/chargebacks/evidence` | Evidencia de contracargos. |
| `POST` | `/api/payments/merchants/notes` | Guarda una nota operativa. |
| `GET` | `/api/payments/merchants/notes/search` | Busca notas por fragmento. |
| `POST` | `/api/payments/audit/event` | Registra un evento de auditoría. |
| `POST` | `/api/payments/tokens/fingerprint` | Huella del número de tarjeta. |
| `POST` | `/api/payments/tokens/protect` | Cifra un valor sensible. |
| `GET` | `/api/payments/callback/validate` | Verifica el callback de un comercio. |
| `POST` | `/api/payments/notifications/preview` | Previsualiza una plantilla de notificación. |
| `POST` | `/api/payments/import` | Importa pagos en XML. |
| `POST` | `/api/payments/config` | Carga configuración en YAML. |
| `GET` | `/api/payments/return` | Redirección de retorno de pasarela. |
| `GET` | `/api/payments/diagnostics/ping` | Diagnóstico de red. |
| `GET` | `/api/payments/diagnostics/reachable` | Alcance de servicios internos. |
| `POST` | `/api/payments/maintenance/run` | Ejecuta una tarea de mantenimiento. |

Ejemplos:

```bash
curl -i 'http://localhost:8080/api/legacy/reports?merchant=merchant-demo'
curl -i 'http://localhost:8080/api/payments/settlements/authorized'
curl -i -X POST 'http://localhost:8080/api/payments/merchants/notes?merchant=merchant-demo&note=revision'
```

## Despliegue

El `Dockerfile` construye la imagen del servicio y `deploy/k8s/deployment.yaml`
contiene el manifiesto de despliegue. El workflow `.github/workflows/build.yml`
es el pipeline heredado del equipo de plataforma; sus jobs están condicionados a
la variable de repositorio `ENABLE_DEMO_PIPELINE`, que no está definida.

```bash
mvn -B package
docker build -t payment-service:local .
```

## Estructura

```text
src/main/java/com/example/payment/
├── PaymentApplication.java
├── api/            PaymentController, SearchController
├── audit/          AuditController
├── billing/        InvoiceController, InvoiceRepository
├── chargebacks/    ChargebackController
├── config/         YamlController
├── crypto/         HashUtil, TokenController
├── domain/         Payment, PaymentRepository, SearchRepository
├── files/          FileController, ArchiveController
├── gateway/        GatewayClient
├── legacy/         LegacyReportController, LegacyReportRepository
├── merchants/      MerchantNoteController, MerchantNoteRepository
├── net/            FetchController
├── refunds/        RefundController, RefundRepository
├── service/        ReportService
├── settlements/    SettlementBucket, SettlementController, SettlementRepository
├── sys/            PingController, MaintenanceController, ReachabilityController
├── vendor/         StringInterpolator, TemplateController
├── web/            RedirectController
└── xml/            XmlController
src/main/resources/
├── application.properties
├── application-prod.properties
├── integration-credentials.yaml
└── schema.sql
src/test/java/com/example/payment/PaymentApplicationTests.java
deploy/k8s/deployment.yaml
Dockerfile
pom.xml
```
