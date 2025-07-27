package com.github.gabrielsilper.project_reactor_examples;

import com.github.gabrielsilper.project_reactor_examples.exceptions.VideoMonetizationException;
import com.github.gabrielsilper.project_reactor_examples.mocks.MockVideo;
import com.github.gabrielsilper.project_reactor_examples.models.Video;
import com.github.gabrielsilper.project_reactor_examples.models.YoutubeChannel;
import com.github.gabrielsilper.project_reactor_examples.services.YoutubeChannelService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

public class ErrorHandlerOperatorsTest {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandlerOperatorsTest.class);

    @Test
    public void onErrorReturnMonetization() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideosWithLowViews());
        YoutubeChannelService channelService = new YoutubeChannelService();

        channelService.getAllVideosMonetizationAsync(channel)
                .onErrorReturn(VideoMonetizationException.class, 0.0)
                .subscribe(video -> System.out.println("Monetization: $" + video));
    }

    @Test
    public void onErrorResumeMonetization() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideosWithLowViews());
        YoutubeChannelService channelService = new YoutubeChannelService();

        channelService.getAllVideosMonetizationAsync(channel)
                .onErrorResume(VideoMonetizationException.class, e -> {
                    log.error("(OnErrorResume) Error calculating monetization: {}", e.getMessage());
                    return Flux.just(0.0, 999D, 1000.0);
                })
                .subscribe(video -> System.out.println("Monetization: $" + video));
    }

    @Test
    public void onErrorContinueMonetization() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideosWithLowViews());
        YoutubeChannelService channelService = new YoutubeChannelService();

        channelService.getAllVideosMonetizationAsync(channel)
                .onErrorContinue(VideoMonetizationException.class, (e, object) -> {
                    Video video = (Video) object;
                    log.error("(OnErrorContinue) Error calculating monetization for video {}: {}", video.getName(), e.getMessage());
                })
                .subscribe(video -> System.out.println("Monetization: $" + video));
    }

    @Test
    public void onErrorMapMonetization() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideosWithLowViews());
        YoutubeChannelService channelService = new YoutubeChannelService();

//        channelService.getAllVideosMonetizationAsync(channel)
//                .onErrorMap(Throwable.class, e -> {
//                    log.error("(OnErrorMap) Error calculating monetization: {}", e.getMessage());
//                    return new VideoMonetizationException("Monetization calculation failed");
//                })
//                .subscribe(video -> System.out.println("Monetization: $" + video));

        channelService.getAllVideosMonetizationAsync(channel)
                .onErrorMap(Throwable.class, e -> {
                    log.error("(OnErrorMap) Error calculating monetization: {}", e.getMessage());
                    return new VideoMonetizationException("Monetization calculation failed");
                })
                .subscribe(video -> System.out.println("Monetization: $" + video),
                        error -> log.error("Error in subscription: {}", error.getMessage()));
    }

    @Test
    public void onErrorCompleteMonetization() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideosWithLowViews());
        YoutubeChannelService channelService = new YoutubeChannelService();

//        channelService.getAllVideosMonetizationAsync(channel)
//                .doFinally(signalType -> System.out.println("Monetization calculation ended with signal: " + signalType))
//                .subscribe(video -> System.out.println("Monetization: $" + video),
//                        error -> log.error("Error in subscription: {}", error.getMessage()));

        channelService.getAllVideosMonetizationAsync(channel)
                .onErrorComplete()
                .doFinally(signalType -> System.out.println("Monetization calculation ended with signal: " + signalType))
                .subscribe(video -> System.out.println("Monetization: $" + video),
                        error -> log.error("Error in subscription: {}", error.getMessage()));
    }

    @Test
    public void emptyMonetization() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideosWithNullViews());
        YoutubeChannelService channelService = new YoutubeChannelService();

        channelService.getAllVideosMonetizationAsync(channel)
                .defaultIfEmpty(0.0)
                .log()
                .subscribe(video -> System.out.println("Monetization: $" + video));

//        channelService.getAllVideosMonetizationAsync(channel)
//                .switchIfEmpty(Flux.just(0.0))
//                .log()
//                .subscribe(video -> System.out.println("Monetization: $" + video));
    }

    @Test
    public void retryMonetization() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos());
        YoutubeChannelService channelService = new YoutubeChannelService();

        channelService.getAllVideosMonetizationWithRateAsync(channel)
                .retryWhen(Retry.max(3))
                .subscribe(video -> System.out.println("Monetization: $" + video));
    }
}
