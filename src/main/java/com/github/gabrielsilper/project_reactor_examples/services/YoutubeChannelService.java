package com.github.gabrielsilper.project_reactor_examples.services;

import com.github.gabrielsilper.project_reactor_examples.models.Video;
import com.github.gabrielsilper.project_reactor_examples.models.YoutubeChannel;
import reactor.core.publisher.Flux;

import java.util.Random;

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
                .doOnNext(video -> printInfoMonetization(video, null))
                .flatMap(this.monetizationService::calculateMonetizationAsync);
    }

    public Flux<Double> getAllVideosMonetizationWithRateAsync(YoutubeChannel channel) {
        this.monetizationService = new MonetizationService();

        return channel.getAllVideos()
                .flatMap(video -> {
                    Integer rate = new Random().nextInt(15) - 1;
                    printInfoMonetization(video, rate);
                    return this.monetizationService.calculateMonetizationAsync(video, rate);
                });
    }

    private void printInfoMonetization(Video video, Integer rate) {
        if (rate != null) {
            System.out.println("Calculating monetization for video: " + video.getName() + " with views: " + video.getViews() + " and rate: " + rate);
        }
        System.out.println("Calculating monetization for video: " + video.getName() + " with views: " + video.getViews());
    }
}
