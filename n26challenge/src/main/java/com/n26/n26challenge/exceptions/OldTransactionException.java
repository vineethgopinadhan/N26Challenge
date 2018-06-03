package com.n26.n26challenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NO_CONTENT)
public class OldTransactionException extends RuntimeException{
	public static final long serialVersionUID=1L;
}
