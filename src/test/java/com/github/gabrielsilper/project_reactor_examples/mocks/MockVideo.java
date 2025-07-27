package com.github.gabrielsilper.project_reactor_examples.mocks;

import com.github.gabrielsilper.project_reactor_examples.models.Video;

import java.util.List;

public class MockVideo {

    public static List<Video> getMockVideos() {
        return List.of(
                new Video("Video 1", "Description 1", 1000, 10000),
                new Video("Video 2", "Description 2", 2000, 20000),
                new Video("Video 3", "Description 3", 3000, 30000),
                new Video("Video 4", "Description 4", 4000, 40000),
                new Video("Video 5", "Description 5", 5000, 50000)
        );
    }

    public static List<Video> getMockVideos2() {
        return List.of(
                new Video("Video 6", "Description 6", 6000, 60000),
                new Video("Video 7", "Description 7", 7000, 70000),
                new Video("Video 8", "Description 8", 8000, 60000)
        );
    }

    public static List<Video> getMockVideos3() {
        return List.of(
                new Video("Video 9", "Description 9", 9000, 90000),
                new Video("Video 10", "Description 10", 10000, 100000)
        );
    }

    public static List<Video> getMockVideosWithLowViews() {
        return List.of(
                new Video("Video 11", "Description 11", 300, 3000),
                new Video("Video 12", "Description 12", 100, 1000),
                new Video("Video 13", "Description 13", 400, 4000),
                new Video("Video 14", "Description 14", 200, 2000)
        );
    }

    public static List<Video> getMockVideosWithNullViews() {
        return List.of(
                new Video("Video 15", "Description 15", 1000, 10000),
                new Video("Video 16", "Description 16", 2000, null),
                new Video("Video 17", "Description 17", 3000, 30000),
                new Video("Video 18", "Description 18", 4000, null)
        );
    }
}
