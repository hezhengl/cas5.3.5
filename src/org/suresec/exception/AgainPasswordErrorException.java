package org.suresec.exception;

import org.apereo.cas.authentication.RootCasException;

public class AgainPasswordErrorException extends RootCasException{

    public static final String CODE = "required.again";

    /**
     * Constructs a TicketCreationException with the default exception code.
     */
    public  AgainPasswordErrorException() {
        super(CODE);
    }

    public  AgainPasswordErrorException(String msg) {
        super(msg);
    }
    /**
     * Constructs a TicketCreationException with the default exception code and
     * the original exception that was thrown.
     * 
     * @param throwable
     *            the chained exception
     */
    public  AgainPasswordErrorException(final Throwable throwable) {
        super(CODE, throwable);
    }
}
