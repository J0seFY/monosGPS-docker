FROM maven AS builder


#se define la carpeta de trabajo
WORKDIR /src

#copia todo el proyecto
COPY . .

#empaqueta todo el proyecto sin tests
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /src

COPY --from=builder src/target/MaterialesService-0.0.1-SNAPSHOT.jar MaterialesService-0.0.1-SNAPSHOT.jar

EXPOSE 8083
ENTRYPOINT ["java","-jar","MaterialesService-0.0.1-SNAPSHOT.jar"]
