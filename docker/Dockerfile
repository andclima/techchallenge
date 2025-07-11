# cache das dependências do Maven
FROM maven:3.9.9-eclipse-temurin-21 AS dependency-builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

# constrói a aplicação Java
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY --from=dependency-builder /root/.m2 /root/.m2
COPY src src
COPY pom.xml .
RUN mvn package -DskipTests -B

# cria a imagem final com o JRE e a aplicação
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]