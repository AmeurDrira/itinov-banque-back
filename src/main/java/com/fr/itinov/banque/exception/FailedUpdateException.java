package com.fr.itinov.banque.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class FailedUpdateException extends AbstractThrowableProblem {

    private static final String EXCEPTION_TITLE_FAILED_CREATION = "Modification échouée";

    public FailedUpdateException(String message) {
        super(null, EXCEPTION_TITLE_FAILED_CREATION, Status.BAD_REQUEST, message);
    }
}
