package com.fr.itinov.banque.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class NotFoundException extends AbstractThrowableProblem {

    public static final String EXCEPTION_TITLE_NOT_FOUND = "Not found";

    public NotFoundException(String message) {
        super(null, EXCEPTION_TITLE_NOT_FOUND, Status.NOT_FOUND, message);
    }

}
