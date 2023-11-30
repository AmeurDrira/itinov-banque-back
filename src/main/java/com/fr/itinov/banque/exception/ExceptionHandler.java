package com.fr.itinov.banque.exception;

import java.net.URI;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@ControllerAdvice
public class ExceptionHandler implements ProblemHandling {

    private static final String CODE_KEY = "code";
    private static final String ERREUR_KEY = "erreur";
    private static final String DESCRIPTION_KEY = "description";
    private static final String URI_KEY = "URI";

    @Override
    public ProblemBuilder prepare(
            @NonNull final Throwable throwable,
            @NonNull final StatusType status,
            @NonNull final URI type) {
        ProblemBuilder problemBuilder =
                ProblemHandling.super
                        .prepare(throwable, status, type)
                        .with(CODE_KEY, status.getStatusCode())
                        .with(ERREUR_KEY, status.getReasonPhrase())
                        .with(DESCRIPTION_KEY, throwable.getMessage());
        if (!Problem.DEFAULT_TYPE.equals(type)) {
            problemBuilder.with(URI_KEY, type);
        }

        return problemBuilder;
    }

    @Override
    public ResponseEntity<Problem> create(
            @NonNull final Throwable throwable,
            final Problem originalProblem,
            @NonNull final NativeWebRequest request,
            @NonNull final HttpHeaders headers) {
        ProblemBuilder problemBuilder =
                prepare(
                        throwable,
                        Objects.requireNonNull(originalProblem.getStatus()),
                        Problem.DEFAULT_TYPE);
        ThrowableProblem problem = problemBuilder.build();
        return ProblemHandling.super.create(throwable, problem, request, headers);
    }
}
