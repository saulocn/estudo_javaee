package br.com.estudo.saulocn.exceptions;

import javax.ejb.ApplicationException;

/**
 * Exception de teste.
 *
 * Inicialmente ela foi criada para demonstrar que, como ela é uma checked exception (Extends Exception), ela não gera
 * Rollback no contexto transacional(TransactionAttributeType.REQUIRED).
 *
 * Para que esse tipo de exception gere rollbacl, devemos configurá-la adicionando as anotações
 * @ApplicationException(rollback = true)
 *
 */
@ApplicationException(rollback = true)
public class PaymentErrorException extends Exception{
}
