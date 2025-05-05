FROM maven AS builder


#se define la carpeta de trabajo
WORKDIR /src

#copia todo el proyecto
COPY . .

#empaqueta todo el proyecto sin test
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /src

COPY --from=builder src/target/AuthService-0.0.1-SNAPSHOT.jar AuthService-0.0.1-SNAPSHOT.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","AuthService-0.0.1-SNAPSHOT.jar"]
