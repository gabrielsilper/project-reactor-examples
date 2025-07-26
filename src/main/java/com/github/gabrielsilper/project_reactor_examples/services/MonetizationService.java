package com.github.gabrielsilper.project_reactor_examples.services;

import com.github.gabrielsilper.project_reactor_examples.exceptions.VideoMonetizationException;
import com.github.gabrielsilper.project_reactor_examples.models.Video;
import reactor.core.publisher.Mono;

public class MonetizationService {

    public Double calculateMonetization(Video video) {
        return video.getViews() * 0.2;
    }

    public Mono<Double> calculateMonetizationAsync(Video video) {
        if (video.getViews() < 1500){
            throw new VideoMonetizationException("Video views are too low for monetization");
        }

        return Mono.just(video.getViews() * 0.2);
    }
}
