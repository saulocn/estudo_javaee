APP_DIR=$(shell pwd)


build:
	@echo -------------------------------------------
	@echo ------- CONSTRUINDO APLICAÇÃO -------------
	@echo -------------------------------------------
	mvn clean package && docker build -t saulocn/jakarta-ee-project .
	@echo -------------------------------------------
	@echo ------- FINALIZANDO CONSTRUÇÃO ------------
	@echo -------------------------------------------

build-queue:
	@echo -------------------------------------------
	@echo ------- CONSTRUINDO APLICAÇÃO -------------
	@echo -------------------------------------------
	cd jms && docker build -t saulocn/queue-jee .
	@echo -------------------------------------------
	@echo ------- FINALIZANDO CONSTRUÇÃO ------------
	@echo -------------------------------------------

delete-network:
	@echo -------------------------------------------
	@echo ------- CONSTRUINDO NETWORK ---------------
	@echo -------------------------------------------
	docker network rm javaee_network || true 
	@echo -------------------------------------------
	@echo ------- FINALIZANDO NETWORK ---------------
	@echo -------------------------------------------

create-network: delete-network
	@echo -------------------------------------------
	@echo ------- CONSTRUINDO NETWORK ---------------
	@echo -------------------------------------------
	docker network create javaee_network
	@echo -------------------------------------------
	@echo ------- FINALIZANDO NETWORK ---------------
	@echo -------------------------------------------

delete-pg:
	@echo -------------------------------------------
	@echo ------- CONSTRUINDO POSTGRESQL ------------
	@echo -------------------------------------------
	docker stop ms_pg || true && docker rm -f ms_pg || true
	@echo -------------------------------------------
	@echo ------- FINALIZANDO POSTGRESQL ------------
	@echo -------------------------------------------

start-pg: delete-pg
	@echo -------------------------------------------
	@echo ------- CONSTRUINDO POSTGRESQL ------------
	@echo -------------------------------------------
	docker run -p 5432:5432 --network javaee_network  --name ms_pg -v ~/workspace/microservicos/pg_data:/var/lib/postgresql/data -e POSTGRES_PASSWORD=postgres -d postgres
	@echo -------------------------------------------
	@echo ------- FINALIZANDO POSTGRESQL ------------
	@echo -------------------------------------------
	

delete-app:
	@echo -------------------------------------------
	@echo ------- INICIANDO APLICAÇÃO ---------------
	@echo -------------------------------------------
	docker stop jakarta-ee-project || true && docker rm -f jakarta-ee-project || true
	@echo -------------------------------------------
	@echo ------ FINALIZANDO APLICAÇÃO --------------
	@echo -------------------------------------------

run: delete-app
	@echo -------------------------------------------
	@echo ------- INICIANDO APLICAÇÃO ---------------
	@echo -------------------------------------------
	docker run -d --network javaee_network -p 8080:8080 -p 9990:9990 --name jakarta-ee-project saulocn/jakarta-ee-project
	@echo -------------------------------------------
	@echo ------ FINALIZANDO APLICAÇÃO --------------
	@echo -------------------------------------------

delete-queue:
	@echo -------------------------------------------
	@echo ------- INICIANDO FILA --------------------
	@echo -------------------------------------------
	docker stop queue-jee || true && docker rm -f queue-jee || true
	@echo -------------------------------------------
	@echo ------ FINALIZANDO FILA -------------------
	@echo -------------------------------------------

queue: delete-queue
	@echo -------------------------------------------
	@echo ------- INICIANDO FILA --------------------
	@echo -------------------------------------------
	docker stop queue-jee || true && docker rm -f queue-jee || true && docker run -d --network javaee_network -p 8085:8080 -p 9995:9990 -p 5445:5445 --name queue-jee saulocn/queue-jee
	@echo -------------------------------------------
	@echo ------ FINALIZANDO FILA -------------------
	@echo -------------------------------------------

delete:	delete-app delete-queue delete-pg delete-network

build-run: build run

all: delete build build-queue create-network start-pg queue run