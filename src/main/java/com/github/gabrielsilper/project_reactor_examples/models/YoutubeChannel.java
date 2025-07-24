package com.github.gabrielsilper.project_reactor_examples.models;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class YoutubeChannel {
    private final List<Video> videos;

    public YoutubeChannel() {
        this.videos = new ArrayList<>();
    }

    public YoutubeChannel(List<Video> videos) {
        this.videos = videos;
    }

    public void addVideo(Video video) {
        videos.add(video);
    }

    public Flux<Video> getAllVideos() {
        return Flux.fromIterable(videos);
    }

    public Flux<Video> getAllVideos(long numberOfVideos) {
        return Flux.fromIterable(videos)
                .take(numberOfVideos);
    }

    public Flux<Integer> getAllVideosViews() {
        return Flux.fromIterable(videos)
                .map(Video::getViews);
    }

    public Flux<String> getAllVideosNames() {
        return Flux.fromIterable(videos)
                .map(Video::getName);
    }

    public Flux<Video> getVideosByRating(int rate){
        return Flux.fromIterable(videos)
                .filter(video -> video.getLikes() >= rate);
    }

    public Flux<Double> getAllVideosMonetization() {
        return Flux.fromIterable(videos)
                .map(Video::getMonetization);
    }
}
