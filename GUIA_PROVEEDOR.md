# Guía para proveedores de la herramienta DevSecOps

Este repositorio es el punto de partida de una PoC de SAST, SCA y detección de secretos sobre una API REST de pagos. **El proveedor prepara, ejecuta y presenta el escenario completo de extremo a extremo.** El equipo cliente observa la demostración final y la califica con su checklist; no introduce código, configura herramientas ni opera los pipelines durante la prueba.

# Checkmarx

## Alcance del análisis

El análisis debe cubrir todo el repositorio, no solo el código Java:

- **Código fuente** — `src/main/java`, `src/test/java`
- **Configuración** — `src/main/resources` (properties y YAML)
- **Dependencias** — `pom.xml` y su árbol transitivo
- **Contenedor** — `Dockerfile`
- **Infraestructura** — `deploy/k8s/deployment.yaml`
- **Pipeline** — `.github/workflows/build.yml`, el pipeline de construcción heredado del equipo de plataforma

El workflow `build.yml` forma parte del material a analizar, no del montaje de la prueba: sus jobs están condicionados a la variable de repositorio `ENABLE_DEMO_PIPELINE`, que no está definida, de modo que no se ejecuta por sí solo. El proveedor configura sus propios workflows de seguridad por separado.

Se solicita además que el inventario de hallazgos se entregue en un formato procesable (SARIF, CSV o consulta por API), con archivo y línea por hallazgo, para poder contrastarlo fuera de la sesión.

## Preparación a cargo del proveedor

1. Crear una copia de trabajo propia (fork o repositorio de prueba) con `main` y `develop`, y registrar el commit de partida. Mantener la deuda de la línea base durante la prueba y conservar intacto el repositorio público de origen para que otros proveedores puedan usar el mismo punto de partida.
2.  Antes de introducir cualquier cambio, ejecutar un escaneo completo de SAST, SCA y detección de secretos sobre la rama main en el commit de partida. El resultado de ese escaneo es la deuda de línea base: la referencia contra la cual los análisis diferenciales posteriores distinguirán los hallazgos nuevos de la deuda heredada. Registrar el hash del commit de partida y adjuntar este escaneo inicial como evidencia. (En el commit de partida, main y develop parten del mismo estado; main es la rama de referencia para la línea base.)
3. Preparar su sandbox o tenant, el IDE con su extensión oficial y la integración con GitHub. Configurar por su cuenta los análisis, las políticas, los permisos, las compuertas y la automatización de los PR. El repositorio de origen no suministra workflows ni reglas de exclusión.
3. Asegurar que los checks requeridos impidan realmente el merge cuando fallen. Documentar las versiones, políticas, umbrales y cualquier ajuste que influya en el resultado.
4. Usar únicamente credenciales sintéticas para la demostración y mantener los secretos reales de la plataforma fuera del repositorio y de las capturas.

## Flujo que debe ejecutar y mostrar en vivo

### 1. Desarrollo local y Shift-Left

Desde una rama `feature/*`, introducir una debilidad SAST y un secreto de prueba antes de abrir el PR. Mostrar su detección en el IDE, la explicación del hallazgo y una corrección asistida por IA aplicada en el editor. Mostrar en la consola central la métrica de desarrolladores activos que usan la extensión. El proveedor diseña sus propios cambios de prueba y muestra que la corrección conserva el funcionamiento de la API.

### 2. Integración `feature/*` → `develop`

Abrir un PR con un hallazgo Alto o Crítico nuevo, o con un secreto de prueba. Ejecutar análisis diferencial de SAST, SCA y secretos; demostrar qué cambios se analizaron y cómo se distingue la deuda previa. Mostrar un check fallido y un bloqueo efectivo del merge. Subir un commit corrector al mismo PR, mostrar el reanálisis automático, el check aprobado y el merge a `develop`.

### 3. Release `develop` → `main`

Abrir el PR de release y ejecutar un análisis completo de SAST, SCA y secretos. Exportar un SBOM descargable en CycloneDX o SPDX. Mostrar la clasificación de hallazgos heredados frente a hallazgos nuevos y la decisión de la compuerta: la deuda heredada permanece visible y el PR limpio puede avanzar. Demostrar también que un hallazgo grave o secreto nuevo bloquearía el release, y cerrar la demostración con el PR nuevamente limpio. Si se presenta una capacidad de reachability, enseñar la evidencia de la ruta de ejecución que sustenta la clasificación.

### 4. Dictamen en GitHub

Publicar automáticamente en el PR un comentario o Job Summary que muestre el estado de la compuerta, **0 vulnerabilidades nuevas y 0 secretos nuevos** en el caso aprobado, el recuento separado de deuda histórica y un enlace funcional al SBOM. El dictamen debe poder entenderse desde GitHub sin depender de una explicación verbal o de entrar en otra consola.

## Evidencia que debe entregar y explicar

- Enlaces a la copia de trabajo, ramas, commits, PR y ejecuciones de checks; indicar el commit de línea base y la secuencia temporal.
- Evidencia del IDE antes del PR, de la corrección con IA y de la telemetría de adopción.
- Resultados del PR a `develop` antes y después del commit corrector, incluido el bloqueo efectivo y la distinción entre cambios nuevos y deuda previa.
- Resultados del PR a `main`, inventario completo de hallazgos, decisión de la compuerta, SBOM descargable y dictamen publicado en GitHub.
- Configuración aplicada, duración de cada análisis, limitaciones observadas y pasos necesarios para reproducir la integración.

La presentación final debe recorrer el flujo en ese orden y permitir inspeccionar los resultados reales. La configuración de pipelines y compuertas es responsabilidad íntegra del proveedor; no se entrega una lista pública de hallazgos esperados ni código de inyección preparado por el cliente.
