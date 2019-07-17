package org.suresec.webflow.configurer;

import org.apereo.cas.authentication.RootCasException;

public class ChangPwdSuccess  extends RootCasException {

	    /** Serializable ID for unique id. */
	    private static final long serialVersionUID = 5501212207431289999L;

	    /** Code description. */
	    public static final String CODE = "required.success";

	    /**
	     * Constructs a TicketCreationException with the default exception code.
	     */
	    public ChangPwdSuccess() {
	        super(CODE);
	    }

	    public ChangPwdSuccess(String msg) {
	        super(msg);
	    }
	    /**
	     * Constructs a TicketCreationException with the default exception code and
	     * the original exception that was thrown.
	     * 
	     * @param throwable
	     *            the chained exception
	     */
	    public ChangPwdSuccess(final Throwable throwable) {
	        super(CODE, throwable);
	    }

	}