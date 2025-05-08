FROM maven AS builder


#se define la carpeta de trabajo
WORKDIR /src

#copia todo el proyecto
COPY . .

#empaqueta todo el proyecto
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /src

COPY --from=builder src/target/BuscarPersonas-0.0.1-SNAPSHOT.jar BuscarPersonas-0.0.1-SNAPSHOT.jar

EXPOSE 8082
ENTRYPOINT ["java","-jar","BuscarPersonas-0.0.1-SNAPSHOT.jar"]
