# Estudo Jakarta EE

Projeto para estudo do Jakarta EE.

Neste projeto, podemos visualizar uma aplicação que utiliza a imagem em Docker do [Wildfly](https://hub.docker.com/r/jboss/wildfly) e,
neste container, são configurados um Datasource para um banco de dados PostgreSQL
utlizado no projeto e uma fila JMS.

Para visualizar a forma com que foi criado o Datasource, basta analisar o arquivo `install_pg.sh`.

## Datasource

Nele, é feito o download do `jar` do Driver JDBC, e com o auxílio do comando `Jboss-CLI` adicionamos o driver no servidor 
Wildfly e criação de um datasource.

## Fila JMS

Para visualizar a forma com que foi criada a fila `PaymentQueue`, basta analisar o arquivo `install_queue.sh`.
Nele é criada a fila através do comando `jms-queue`, utilizando o `Jboss-CLI`. 

**É importenta saber que, para habilitar o uso do JMS no container do Wildfly, devemos utilizar `standalone-full.xml`.**


## Transações EJB

No código da classe `OrderService`, podemos ver a utilização de um contextos transacional através da configuração:

**Modelos exemplificados no projeto:**

### Required

```
@TransactionAttribute(TransactionAttributeType.REQUIRED)
```

Este modelo é utilizado quando desejamos que o método seja executado dentro de um contexto transacional, ou seja, tudo 
que é executado dentro deste método pode ser revertido.


### NOT_SUPPORTED

```
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
```

Este modelo é utlizado quando não desejamos que o método seja executad dentro de um contexto transacional.

## Modelos não exemplificados no projeto:

Outros modelos transacionais podem ser utilizados, como [encontrado no site](https://lucianomolinari.wordpress.com/2012/04/04/entendendo-os-atributos-de-transacao/):

### REQUIRES_NEW

Significa que independentemente do cliente estar rodando ou não dentro de uma transação, uma nova transação será criada para a execução desse método e, no final de sua execução, essa transação sofrerá o commit/rollback.
Se o cliente já estiver dentro de uma transação, essa fica suspensa até o término da transação que foi criada para a execução do método. É importante notar que são 2 transações distintas, dessa forma uma não afeta a outra.


### SUPPORTS

Significa que o método será executado dentro ou fora de uma transação, dependendo do cliente. Se o cliente chamar o método fora de uma transação, nenhuma será criada e o método será executado fora de uma transação. Se o cliente chamar dentro de uma transação, o método será executado dentro dessa mesma transação.

### MANDATORY

Significa que sempre que o cliente for chamar o método, ele já deve estar em uma transação. Nesse caso, o método será executado dentro dessa mesma transação. Caso o cliente chame o método fora de uma transação, um erro será lançado e o código do método não será executado.

### NEVER

Significa que o cliente nunca deve estar envolvido em uma transação do momento da chamada do método. Nesse caso, o método será executado normalmente sem criar nenhuma transação. Caso o cliente esteja envolvido em uma transação no momento da chamada, um erro será lançado e o código do método não será executado.

## Exceptions

Uma `Exception` pode reverter um método que utiliza o contexto transacional.
É importante saber que nem toda `Exception` acaba por causar um `Rollback` no método.

Por padrão, apenas unchecked exceptions (`RuntimeException`) causam um `Rollback`, mas podemos configurar checked 
exceptions (que extendem `Exception`) para gerar um `Rollback` através da anotação:

```
@ApplicationException(rollback = true)
```

O mesmo vale para o contrário, caso não seja de nosso interesse que uma unchecked exception (que extende `RuntimeException`)
gere um `Rollback`, podemos adicionar a anotação:

```
@ApplicationException(rollback = false)
```