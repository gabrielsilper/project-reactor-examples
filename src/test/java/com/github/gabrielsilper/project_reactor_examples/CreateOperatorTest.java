package com.github.gabrielsilper.project_reactor_examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@DisplayName("Create Operator Test - Tests to understand Flux.create() operator")
public class CreateOperatorTest {

    @Test
    @DisplayName("Test file reading using Flux.create() operator synchronously")
    public void testFileReadingCreateOperatorSync() {
        String sampleFilePath = "src/test/resources/sample.txt";
        String namesFilePath = "src/test/resources/names.txt";
        String fruitsFilePath = "src/test/resources/fruits.txt";

        Flux<Object> fileFlux = Flux.create(emitter -> {
            readFileFluxSink(emitter, sampleFilePath);
            readFileFluxSink(emitter, namesFilePath);
            readFileFluxSink(emitter, fruitsFilePath);

            emitter.complete();
        }).log();

        StepVerifier
                .create(fileFlux)
                .expectNext("Line 1")
                .expectNext("Line 2")
                .expectNext("Line 3")
                .expectNext("Gabriel Pereira")
                .expectNext("Maiana Macedo")
                .expectNext("ChatGPT")
                .expectNext("Apple")
                .expectNext("Watermelon")
                .expectNext("Banana")
                .expectNext("Avocado")
                .verifyComplete();
    }

    @Test
    @DisplayName("Test file reading using Flux.create() operator asynchronously with CompletableFuture")
    public void testFileReadingCreateOperatorAsync() {
        String sampleFilePath = "src/test/resources/sample.txt";
        String namesFilePath = "src/test/resources/names.txt";
        String fruitsFilePath = "src/test/resources/fruits.txt";

        Flux<Object> fileFlux = Flux.create(emitter -> {
            CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> readFileFluxSink(emitter, sampleFilePath));
            CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> readFileFluxSink(emitter, namesFilePath));
            CompletableFuture<Void> task3 = CompletableFuture.runAsync(() -> readFileFluxSink(emitter, fruitsFilePath));
            CompletableFuture.allOf(task1, task2, task3).join();

            emitter.complete();
        }, FluxSink.OverflowStrategy.BUFFER).log();

        StepVerifier
                .create(fileFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    private static void readFileFluxSink(FluxSink<Object> emitter, String filePath) {
        System.out.println("Reading file: " + filePath + " on " + Thread.currentThread().getName() + " thread.");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                emitter.next(line);
            }
        } catch (IOException e) {
            emitter.error(new RuntimeException("Error reading file: " + filePath, e));
        }
    }
}
