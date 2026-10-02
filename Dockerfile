FROM openjdk:17

LABEL maintainer="payments-platform@example.com"

ENV SPRING_PROFILES_ACTIVE=prod
ENV PAYMENTS_GATEWAY_TOKEN=sk_live_51H8xQ2eZvKYlo2C0pR3mNq7bT9wXyZ1aB2cD3eF4gH5iJ6k
ENV DB_PASSWORD=P4ssw0rd-Staging-2024!

WORKDIR /app

COPY target/payment-service-testbed-1.0.0-SNAPSHOT.jar /app/app.jar
COPY src/main/resources/integration-credentials.yaml /app/config/integration-credentials.yaml

RUN chmod 777 /app

EXPOSE 8080 22 5005

USER root

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
