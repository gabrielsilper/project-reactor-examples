package com.github.gabrielsilper.project_reactor_examples.models;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class YoutubeChannel {
    private final List<Video> videos;

    public YoutubeChannel() {
        this.videos = new ArrayList<>();
    }

    public void addVideo(Video video) {
        videos.add(video);
    }

    public Flux<Video> getAllVideos() {
        return Flux.fromIterable(videos);
    }
}
