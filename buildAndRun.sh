#!/bin/sh
mvn clean package && docker build -t saulocn/jakarta-ee-project .
docker network create javaee_network
docker rm -f ms_pg || true && docker run -p 5432:5432 --network javaee_network  --name ms_pg -v /home/sacarvalho/workspace/microservicos/pg_data:/var/lib/postgresql/data -e POSTGRES_PASSWORD=postgres -d postgres
docker rm -f jakarta-ee-project || true && docker run -d --network javaee_network -p 8080:8080 -p 9990:9990 --name jakarta-ee-project saulocn/jakarta-ee-project