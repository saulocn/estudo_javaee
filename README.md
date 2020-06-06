# Estudo Jakarta EE

Projeto para estudo do Jakarta EE.

Neste projeto, podemos visualizar uma aplica��o que utiliza a imagem em Docker do [Wildfly](https://hub.docker.com/r/jboss/wildfly) e,
neste container, s�o configurados um Datasource para um banco de dados PostgreSQL
utlizado no projeto e uma fila JMS.

Para visualizar a forma com que foi criado o Datasource, basta analisar o arquivo `install_pg.sh`.

## Datasource

Nele, � feito o download do `jar` do Driver JDBC, e com o aux�lio do comando `Jboss-CLI` adicionamos o driver no servidor 
Wildfly e cria��o de um datasource.

## Fila JMS

Para visualizar a forma com que foi criada a fila `PaymentQueue`, basta analisar o arquivo `install_queue.sh`.
Nele � criada a fila atrav�s do comando `jms-queue`, utilizando o `Jboss-CLI`. 

**� importenta saber que, para habilitar o uso do JMS no container do Wildfly, devemos utilizar `standalone-full.xml`.**


## Transa��es EJB

No c�digo da classe `OrderService`, podemos ver a utiliza��o de um contextos transacional atrav�s da configura��o:

**Modelos exemplificados no projeto:**

### Required

```
@TransactionAttribute(TransactionAttributeType.REQUIRED)
```

Este modelo � utilizado quando desejamos que o m�todo seja executado dentro de um contexto transacional, ou seja, tudo 
que � executado dentro deste m�todo pode ser revertido.


### NOT_SUPPORTED

```
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
```

Este modelo � utlizado quando n�o desejamos que o m�todo seja executad dentro de um contexto transacional.

## Modelos n�o exemplificados no projeto:

Outros modelos transacionais podem ser utilizados, como [encontrado no site](https://lucianomolinari.wordpress.com/2012/04/04/entendendo-os-atributos-de-transacao/):

### REQUIRES_NEW

Significa que independentemente do cliente estar rodando ou n�o dentro de uma transa��o, uma nova transa��o ser� criada para a execu��o desse m�todo e, no final de sua execu��o, essa transa��o sofrer� o commit/rollback.
Se o cliente j� estiver dentro de uma transa��o, essa fica suspensa at� o t�rmino da transa��o que foi criada para a execu��o do m�todo. � importante notar que s�o 2 transa��es distintas, dessa forma uma n�o afeta a outra.


### SUPPORTS

Significa que o m�todo ser� executado dentro ou fora de uma transa��o, dependendo do cliente. Se o cliente chamar o m�todo fora de uma transa��o, nenhuma ser� criada e o m�todo ser� executado fora de uma transa��o. Se o cliente chamar dentro de uma transa��o, o m�todo ser� executado dentro dessa mesma transa��o.

### MANDATORY

Significa que sempre que o cliente for chamar o m�todo, ele j� deve estar em uma transa��o. Nesse caso, o m�todo ser� executado dentro dessa mesma transa��o. Caso o cliente chame o m�todo fora de uma transa��o, um erro ser� lan�ado e o c�digo do m�todo n�o ser� executado.

### NEVER

Significa que o cliente nunca deve estar envolvido em uma transa��o do momento da chamada do m�todo. Nesse caso, o m�todo ser� executado normalmente sem criar nenhuma transa��o. Caso o cliente esteja envolvido em uma transa��o no momento da chamada, um erro ser� lan�ado e o c�digo do m�todo n�o ser� executado.

## Exceptions

Uma `Exception` pode reverter um m�todo que utiliza o contexto transacional.
� importante saber que nem toda `Exception` acaba por causar um `Rollback` no m�todo.

Por padr�o, apenas unchecked exceptions (`RuntimeException`) causam um `Rollback`, mas podemos configurar checked 
exceptions (que extendem `Exception`) para gerar um `Rollback` atrav�s da anota��o:

```
@ApplicationException(rollback = true)
```

O mesmo vale para o contr�rio, caso n�o seja de nosso interesse que uma unchecked exception (que extende `RuntimeException`)
gere um `Rollback`, podemos adicionar a anota��o:

```
@ApplicationException(rollback = false)
```