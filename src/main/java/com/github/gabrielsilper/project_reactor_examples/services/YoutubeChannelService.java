package com.github.gabrielsilper.project_reactor_examples.services;

import com.github.gabrielsilper.project_reactor_examples.models.YoutubeChannel;
import reactor.core.publisher.Flux;

public class YoutubeChannelService {
    MonetizationService monetizationService;

    public Flux<Double> getAllVideosMonetization(YoutubeChannel channel) {
        monetizationService = new MonetizationService();

        return channel.getAllVideos()
                .map(monetizationService::calculateMonetization);
    }
}
