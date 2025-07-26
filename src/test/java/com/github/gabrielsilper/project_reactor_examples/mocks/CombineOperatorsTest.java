package com.github.gabrielsilper.project_reactor_examples.mocks;

import com.github.gabrielsilper.project_reactor_examples.models.YoutubeChannel;
import com.github.gabrielsilper.project_reactor_examples.services.YoutubeChannelService;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class CombineOperatorsTest {

    @Test
    public void concatVideosNames() throws InterruptedException {
        Flux<String> videosNames1 = new YoutubeChannel(MockVideo.getMockVideos())
                .getAllVideosNames()
                .delayElements(Duration.ofMillis(800));

        Flux<String> videosNames2 = new YoutubeChannel(MockVideo.getMockVideos2())
                .getAllVideosNames()
                .delayElements(Duration.ofMillis(400));

//      I can also use videosNames1.concatWith(videosNames2).log().subscribe();
        Flux.concat(videosNames1, videosNames2)
                .log()
                .subscribe();

        // Note: This is not a good practice in production code, it's just to wait the flux to complete
        Thread.sleep(6000);
    }

    @Test
    public void mergeVideosNames() throws InterruptedException {
        Flux<String> videosNames1 = new YoutubeChannel(MockVideo.getMockVideos())
                .getAllVideosNames()
                .delayElements(Duration.ofMillis(600));

        Flux<String> videosNames2 = new YoutubeChannel(MockVideo.getMockVideos2())
                .getAllVideosNames()
                .delayElements(Duration.ofMillis(350));

//        videosNames1.mergeWith(videosNames2).log().subscribe();
        Flux.merge(videosNames1, videosNames2)
                .log()
                .subscribe();

        // Note: This is not a good practice in production code, it's just to wait the flux to complete
        Thread.sleep(5000);
    }

    @Test
    public void zipVideosNames() throws InterruptedException {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos2());
        YoutubeChannelService youtubeChannelService = new YoutubeChannelService();

        Flux<String> videosNames = channel.getAllVideosNames()
                .delayElements(Duration.ofMillis(250))
                .log();

        Flux<Double> monetization = youtubeChannelService.getAllVideosMonetization(channel)
                .delayElements(Duration.ofMillis(600))
                .log();

        Flux.zip(videosNames, monetization)
                .map(tuple -> "Video: " + tuple.getT1() + " - Monetization: $" + tuple.getT2())
                .log()
                .subscribe(s -> System.out.println("Zip: " + s));

        // Note: This is not a good practice in production code, it's just to wait the flux to complete
        Thread.sleep(4000);
    }
}
