package com.github.gabrielsilper.project_reactor_examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Stream;

@DisplayName("Operators Test - Tests to understand Flux and Mono Operators")
public class OperatorsTest {

    @Test
    public void fluxJust() {
        Flux<Integer> simpleFlux = Flux.just(1, 2, 3, 4);

        StepVerifier
                .create(simpleFlux)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void fluxFromIterable() {
        Flux<Integer> simpleFlux = Flux.fromIterable(List.of(1, 2, 3, 4));

        StepVerifier
                .create(simpleFlux)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void fluxFromArray() {
        Integer[] array = {1, 2, 3, 4};
        Flux<Integer> simpleFlux = Flux.fromArray(array);

        StepVerifier
                .create(simpleFlux)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void fluxFromStream() {
        Flux<Integer> simpleFlux = Flux.fromStream(Stream.of(1, 2, 3, 4));

        StepVerifier
                .create(simpleFlux)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void fluxRange() {
        Flux<Integer> simpleFlux = Flux.range(1, 4);

        StepVerifier
                .create(simpleFlux)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }
}
