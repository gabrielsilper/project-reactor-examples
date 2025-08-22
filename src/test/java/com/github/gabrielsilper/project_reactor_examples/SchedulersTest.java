package com.github.gabrielsilper.project_reactor_examples;

import com.github.gabrielsilper.project_reactor_examples.mocks.MockVideo;
import com.github.gabrielsilper.project_reactor_examples.models.YoutubeChannel;
import com.github.gabrielsilper.project_reactor_examples.services.VideoAnalyserService;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SchedulersTest {

    @Test
    public void blockingOperation() throws InterruptedException {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos());

        Flux<String> videosName = channel.getAllVideos()
                .filter(video -> {
                    System.out.println("Filter1 - Thread: " + Thread.currentThread().getName());
                    return video.getLikes() > 2000;
                })
                .map(video -> {
                    System.out.println("Map1 - Thread: " + Thread.currentThread().getName());
                    return video.getDescription();
                })
                .map(description -> {
                    try {
                        System.out.println("Blocking operation - Thread: " + Thread.currentThread().getName());
                        Thread.sleep(5000); // Simulating a blocking operation
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return description.toUpperCase();
                });

        for (int i = 0; i < 2; i++) {
            System.out.println("Execute operation " + (i + 1) + " - Thread: " + Thread.currentThread().getName());
            videosName.subscribe(System.out::println);
        }

        Thread.sleep(200);
    }

    @Test
    public void publishOnBlockingOperation() throws InterruptedException {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideosWithLowViews());

        Flux<String> videosName = channel.getAllVideos()
                .filter(video -> {
                    System.out.println("Filter1 - Thread: " + Thread.currentThread().getName());
                    return video.getLikes() > 200;
                })
                .map(video -> {
                    System.out.println("Map1 - Thread: " + Thread.currentThread().getName());
                    return video.getDescription();
                })
                .publishOn(Schedulers.boundedElastic())
                .map(description -> {
                    try {
                        System.out.println("Blocking operation - Thread: " + Thread.currentThread().getName());
                        Thread.sleep(5000); // Simulating a blocking operation
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return description.toUpperCase();
                });

        for (int i = 0; i < 2; i++) {
            System.out.println("Execute operation " + (i + 1) + " - Thread: " + Thread.currentThread().getName());
            videosName.subscribe(System.out::println);
        }

        Thread.sleep(20_000);
    }

    @Test
    public void subscribeOnBlockingOperation() throws InterruptedException {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideosWithLowViews());

        Flux<String> videosName = channel.getAllVideos()
                .filter(video -> {
                    System.out.println("Filter1 - Thread: " + Thread.currentThread().getName());
                    return video.getLikes() > 200;
                })
                .map(video -> {
                    System.out.println("Map1 - Thread: " + Thread.currentThread().getName());
                    return video.getDescription();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(description -> {
                    try {
                        System.out.println("Blocking operation - Thread: " + Thread.currentThread().getName());
                        Thread.sleep(5000); // Simulating a blocking operation
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return description.toUpperCase();
                });

        for (int i = 0; i < 3; i++) {
            System.out.println("Execute operation " + (i + 1) + " - Thread: " + Thread.currentThread().getName());
            videosName.subscribe(System.out::println);
        }

        Thread.sleep(30_000);
    }

    @Test
    public void parallelBlockingOperation() throws InterruptedException {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideosWithLowViews());

        VideoAnalyserService analyserService = new VideoAnalyserService();

        channel.getAllVideos()
                .filter(video -> {
                    System.out.println("Filter1 - Thread: " + Thread.currentThread().getName());
                    return video.getLikes() > 200;
                })
                .parallel().runOn(Schedulers.boundedElastic())
                .map(analyserService::analyzeBlocking)
                .subscribe(rate -> System.out.println("Rate: " + rate));

        Thread.sleep(6_000);
    }

    @Test
    public void parallelPublishOnBlockingOperation() throws InterruptedException {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideosWithLowViews());

        VideoAnalyserService analyserService = new VideoAnalyserService();

        channel.getAllVideos()
                .filter(video -> {
                    System.out.println("Filter1 - Thread: " + Thread.currentThread().getName());
                    return video.getLikes() > 200;
                })
                .flatMap(analyserService::analyzeBlockingAsync)
                .subscribe(rate -> System.out.println("Rate: " + rate));

        Thread.sleep(6_000);
    }
}
