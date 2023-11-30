package com.fr.itinov.banque.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class OperationException extends AbstractThrowableProblem {

    private static final String EXCEPTION_TITLE_OPERATION_REFUSED = "Operation refusé";

    public OperationException(String message) {
        super(null, EXCEPTION_TITLE_OPERATION_REFUSED, Status.BAD_REQUEST, message);
    }

}
