package com.github.gabrielsilper.project_reactor_examples;

import com.github.gabrielsilper.project_reactor_examples.mocks.MockVideo;
import com.github.gabrielsilper.project_reactor_examples.models.YoutubeChannel;
import org.junit.jupiter.api.Test;

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
}
