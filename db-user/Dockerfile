FROM postgres:latest

ENV POSTGRES_USER=monos
ENV POSTGRES_PASSWORD=1234
ENV POSTGRES_DB=usuarios

COPY initUser.sql /docker-entrypoint-initdb.d/init.sql
