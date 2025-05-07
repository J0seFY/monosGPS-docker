FROM postgres:latest

ENV POSTGRES_USER=monos
ENV POSTGRES_PASSWORD=1234
ENV POSTGRES_DB=establecimientos

COPY init.sql /docker-entrypoint-initdb.d/
