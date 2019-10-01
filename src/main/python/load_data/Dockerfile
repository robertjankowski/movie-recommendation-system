FROM postgres:11.1-alpine

COPY 001_data.sql /docker-entrypoint.initdb.d/001_data.sql

ENV PGDATA=/data
