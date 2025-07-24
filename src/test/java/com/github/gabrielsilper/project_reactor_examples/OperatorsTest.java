package com.github.gabrielsilper.project_reactor_examples;

import com.github.gabrielsilper.project_reactor_examples.mocks.MockVideo;
import com.github.gabrielsilper.project_reactor_examples.models.Video;
import com.github.gabrielsilper.project_reactor_examples.models.YoutubeChannel;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class OperatorsTest {

    @Test
    public void printVideosNames() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos());

        channel.getAllVideos()
                .log()
                .subscribe(v -> System.out.println(v.getName()));
    }

    @Test
    public void printFirstTwoVideosNames() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos());

        channel.getAllVideos(2)
                .log()
                .subscribe(v -> System.out.println(v.getName()));
    }

    @Test
    public void printVideosNamesWhile() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos2());

        channel.getAllVideos()
                .log()
                .takeWhile(video -> video.getViews() < 70000)
                .subscribe(v -> System.out.println(v.getName()));
        // My Own Note: This will print the names of videos until it encounters a video with views >= 70,000
        // If a video has less than 70,000 views, but comes after a video with more than 70,000 views, it will not be printed
    }

    @Test
    public void printVideosViews() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos3());

        channel.getAllVideosViews()
                .log()
                .subscribe(System.out::println);
    }

    @Test
    public void printVideosNamesWithMap() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos3());

        channel.getAllVideosNames()
                .log()
                .subscribe(System.out::println);
    }

    @Test
    public void flatMapVideosNames() {
        List<YoutubeChannel> channelList = List.of(
                new YoutubeChannel(MockVideo.getMockVideos()),
                new YoutubeChannel(MockVideo.getMockVideos2()),
                new YoutubeChannel(MockVideo.getMockVideos3())
        );

        Flux<YoutubeChannel> channelFlux = Flux.fromIterable(channelList);

        channelFlux.flatMap(YoutubeChannel::getAllVideosNames)
                .log()
                .subscribe();
    }

    @Test
    public void testFlatMapLikesVideos() {
        List<Video> videos = MockVideo.getMockVideos3();
        YoutubeChannel channel = new YoutubeChannel(videos);

        Flux<Integer> likesFlux = channel.getAllVideos()
                .flatMap(Video::giveLike)
                .map(Video::getLikes);

        StepVerifier
                .create(likesFlux)
                .expectNext(videos.get(0).getLikes() + 1)
                .expectNext(videos.get(1).getLikes() + 1)
                .verifyComplete();
    }

    @Test
    public void filterVideosByRating() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos());

        channel.getVideosByRating(3000)
                .subscribe(video -> System.out.println(video.getName() + " - Likes: " + video.getLikes()));
    }

    @Test
    public void testPrintVideosNamesWithDelay() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos2());

        Flux<String> videosNamesFlux = channel.getAllVideosNames()
                .log()
                .delayElements(Duration.ofSeconds(2));

        StepVerifier
                .create(videosNamesFlux)
                .expectNext("Video 6")
                .expectNext("Video 7")
                .expectNext("Video 8")
                .verifyComplete();
    }

    @Test
    public void PrintVideosNamesWithDelay() throws InterruptedException {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos2());

        channel.getAllVideosNames()
                .log()
                .delayElements(Duration.ofSeconds(2))
                .subscribe();

        // Note: This is not a good practice in production code, but it's used here for demonstration.
        Thread.sleep(Duration.ofSeconds(5));
    }

    @Test
    public void printVideosNamesWithoutTransform () {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos());

        channel.getAllVideos()
                .filter(v -> v.getLikes() >= 3000)
                .doOnNext(video -> System.out.println(video.getName()))
                .map(video -> video.getName() + " - Look at me")
                .log()
                .subscribe();

        System.out.println("-------");

        channel.getAllVideos()
                .filter(v -> v.getLikes() < 3000)
                .doOnNext(video -> System.out.println(video.getName()))
                .map(video -> video.getName() + " - Look at me")
                .log()
                .subscribe();
    }

    @Test
    public void printVideosNamesWithTransform () {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos());

        channel.getVideosByRating(3000)
                .transform((addLookAtMeOnName()))
                .log()
                .subscribe();

        System.out.println("-------");

        channel.getAllVideos()
                .filter(v -> v.getLikes() < 3000)
                .transform(addLookAtMeOnName())
                .log()
                .subscribe();
    }

    private Function<Flux<Video>, Flux<String>> addLookAtMeOnName(){
        return videoFlux -> videoFlux
                .doOnNext(video -> System.out.println(video.getName()))
                .map(video -> video.getName() + " - Look at me");
    }

    @Test
    public void printVideosNamesUpperCaseWithTransform() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos());

        channel.getAllVideos()
                .transform(transformToUpperCase())
                .subscribe(System.out::println);
    }

    private Function<Flux<Video>, Flux<String>> transformToUpperCase() {
        return videoFlux -> videoFlux
                .filter(video -> video.getLikes() >= 3000)
                .map(Video::getName)
                .map(String::toUpperCase);
    }

    @Test
    public void sideEffects() {
        YoutubeChannel channel = new YoutubeChannel(MockVideo.getMockVideos());

        channel.getAllVideosNames()
                .log()
                .doAfterTerminate(() -> System.out.println("Stream finished"))
                .doOnTerminate(() -> System.out.println("Stream completed"))
                .doFinally(signalType -> System.out.println("Stream ended with signal: " + signalType))
                .subscribe();
    }
}
