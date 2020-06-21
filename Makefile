SHELL := /bin/bash

APP_DIR=$(shell pwd)


build:
	@echo -------------------------------------------
	@echo ------- CONSTRUINDO APLICAÇÃO -------------
	@echo -------------------------------------------
	mvn clean package && docker build -t saulocn/jakarta-ee-project .
	@echo -------------------------------------------
	@echo ------- FINALIZANDO CONSTRUÇÃO ------------
	@echo -------------------------------------------

create-network:
	@echo -------------------------------------------
	@echo ------- CONSTRUINDO NETWORK ---------------
	@echo -------------------------------------------
	docker network rm javaee_network || true && docker network create javaee_network
	@echo -------------------------------------------
	@echo ------- FINALIZANDO NETWORK ---------------
	@echo -------------------------------------------

start-pg:
	@echo -------------------------------------------
	@echo ------- CONSTRUINDO POSTGRESQL ------------
	@echo -------------------------------------------
	docker rm -f ms_pg || true && docker run -p 5432:5432 --network javaee_network  --name ms_pg -v ~/workspace/microservicos/pg_data:/var/lib/postgresql/data -e POSTGRES_PASSWORD=postgres -d postgres
	@echo -------------------------------------------
	@echo ------- FINALIZANDO POSTGRESQL ------------
	@echo -------------------------------------------
	

run:
	@echo -------------------------------------------
	@echo ------- INICIANDO APLICAÇÃO ---------------
	@echo -------------------------------------------
	docker rm -f jakarta-ee-project || true && docker run -d --network javaee_network -p 8080:8080 -p 9990:9990 --name jakarta-ee-project saulocn/jakarta-ee-project
	@echo -------------------------------------------
	@echo ------ FINALIZANDO APLICAÇÃO --------------
	@echo -------------------------------------------

delete:
	@echo -------------------------------------------
	@echo ------- REMOVENDO APLICAÇÃO ---------------
	@echo -------------------------------------------
	docker rm -f jakarta-ee-project && docker rm -f ms_pg && docker network rm javaee_network
	@echo -------------------------------------------
	@echo -------- REMOVENDO APLICAÇÃO --------------
	@echo -------------------------------------------

build-run: build run

all: build create-network start-pg run