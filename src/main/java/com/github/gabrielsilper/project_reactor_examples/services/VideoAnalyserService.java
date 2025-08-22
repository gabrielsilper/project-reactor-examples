package com.github.gabrielsilper.project_reactor_examples.services;

import com.github.gabrielsilper.project_reactor_examples.models.Video;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;

public class VideoAnalyserService {

    public Double analyzeBlocking(Video video) {
        double rate = new Random().nextDouble(12) - 1;
        System.out.println("Analyzing video: " + video.getName() + " with rate: " + rate + " - Thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return rate;
    }

    public Mono<Double> analyzeBlockingAsync(Video video) {
        return Mono.fromCallable(() -> analyzeBlocking(video))
                .publishOn(Schedulers.boundedElastic());
    }
}
