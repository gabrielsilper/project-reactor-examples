package com.github.gabrielsilper.project_reactor_examples.services;

import com.github.gabrielsilper.project_reactor_examples.models.Video;
import com.github.gabrielsilper.project_reactor_examples.models.YoutubeChannel;
import reactor.core.publisher.Flux;

public class YoutubeChannelService {
    MonetizationService monetizationService;

    public Flux<Double> getAllVideosMonetization(YoutubeChannel channel) {
        this.monetizationService = new MonetizationService();

        return channel.getAllVideos()
                .map(this.monetizationService::calculateMonetization);
    }

    public Flux<Double> getAllVideosMonetizationAsync(YoutubeChannel channel) {
        this.monetizationService = new MonetizationService();

        return channel.getAllVideos()
                .doOnNext(this::printInfoMonetization)
                .flatMap(this.monetizationService::calculateMonetizationAsync);
    }

    private void printInfoMonetization(Video video) {
        System.out.println("Calculating monetization for video: " + video.getName() + " with views: " + video.getViews());
    }
}
