package org.suresec.exception;

import org.apereo.cas.authentication.RootCasException;

public class OldPasswordErrorException  extends RootCasException {

    /** Serializable ID for unique id. */
    private static final long serialVersionUID = 5501212207531289997L;

    /** Code description. */
    public static final String CODE = "required.oldpwd";

    /**
     * Constructs a TicketCreationException with the default exception code.
     */
    public OldPasswordErrorException() {
        super(CODE);
    }

    public OldPasswordErrorException(String msg) {
        super(msg);
    }
    /**
     * Constructs a TicketCreationException with the default exception code and
     * the original exception that was thrown.
     * 
     * @param throwable
     *            the chained exception
     */
    public OldPasswordErrorException(final Throwable throwable) {
        super(CODE, throwable);
    }

}