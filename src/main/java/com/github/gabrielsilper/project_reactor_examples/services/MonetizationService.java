package com.github.gabrielsilper.project_reactor_examples.services;

import com.github.gabrielsilper.project_reactor_examples.exceptions.VideoMonetizationException;
import com.github.gabrielsilper.project_reactor_examples.models.Video;
import reactor.core.publisher.Mono;

public class MonetizationService {

    public Double calculateMonetization(Video video) {
        return video.getViews() * 0.2;
    }

    public Mono<Double> calculateMonetizationAsync(Video video) {
        return calculateMonetizationAsync(video, null);
    }

    public Mono<Double> calculateMonetizationAsync(Video video, Integer rate) {
        // just to test and use empty operators
        if (video.getViews() == null) {
            return Mono.empty();
        }

        if (rate != null && (rate < 0 || rate > 10)) {
            throw new RuntimeException("Rate value must be between 0 and 10");
        }

        if (video.getViews() < 1500){
            throw new VideoMonetizationException("Video " + video.getName() + " has low views: " + video.getViews());
        }

        return Mono.just(video.getViews() * 0.2);
    }
}
