package com.github.gabrielsilper.project_reactor_examples;

import com.github.gabrielsilper.project_reactor_examples.models.User;
import com.github.gabrielsilper.project_reactor_examples.models.Video;
import com.github.gabrielsilper.project_reactor_examples.models.YoutubeChannel;

public class SubscriberTest {
    public static void main(String[] args) {
        YoutubeChannel channel = new YoutubeChannel();

        channel.addVideo(new Video("Video 1", "Description 1", 100, 1000));
        channel.addVideo(new Video("Video 2", "Description 2", 200, 2000));
        channel.addVideo(new Video("Video 3", "Description 3", 300, 3000));

        User user = new User("Gabriel Silper");

        channel.getAllVideos().subscribe(v -> System.out.println(v.getName()),
                error -> System.out.println("Error: " + error.getMessage()),
                () -> System.out.println("All videos processed."));
    }
}
