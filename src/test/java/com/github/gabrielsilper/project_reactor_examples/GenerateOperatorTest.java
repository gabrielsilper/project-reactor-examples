package com.github.gabrielsilper.project_reactor_examples;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.BufferedReader;
import java.io.FileReader;

public class GenerateOperatorTest {
    private static final Logger log = LoggerFactory.getLogger(GenerateOperatorTest.class);
    String sampleFilePath = "src/test/resources/sample.txt";

    @Test
    public void fluxGenerate() {
        Flux<Object> generateFlux = Flux.generate(
                () -> {
                    log.info("Creating BufferedReader");
                    return new BufferedReader(new FileReader(sampleFilePath));
                },
                ((bufferedReader, synchronousSink) -> {
                    try {
                        String line = bufferedReader.readLine();
                        if (line != null) {
                            synchronousSink.next(line);
                        } else {
                            synchronousSink.complete();
                        }
                    } catch (Exception e) {
                        synchronousSink.error(e);
                    }
                    return bufferedReader;
                }),
                (bufferedReader -> {
                    try {
                        bufferedReader.close();
                        log.info("BufferedReader closed successfully.");
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                })
        ).log();

        StepVerifier
                .create(generateFlux)
                .expectNext("Line 1", "Line 2", "Line 3")
                .verifyComplete();
    }
}
