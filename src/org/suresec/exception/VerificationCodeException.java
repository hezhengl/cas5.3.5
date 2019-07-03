package org.suresec.exception;

import org.apereo.cas.authentication.RootCasException;

public class VerificationCodeException extends RootCasException {

	private static final long serialVersionUID = 1L;

	public static final String CODE = "verification.error";
	 
	public VerificationCodeException() {
		super(CODE);
	}
	public VerificationCodeException(String code) {
		super(code);
	}
	
	public VerificationCodeException(final Throwable throwable) {
        super(CODE, throwable);
    }

}
