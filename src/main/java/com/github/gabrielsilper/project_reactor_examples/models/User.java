package com.github.gabrielsilper.project_reactor_examples.models;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class User implements Subscriber<Video> {
    private final String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        System.out.println("User " + name + " subscribed to video updates.");
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Video video) {
        System.out.println("User " + name + " received video: " + video.getName());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("User " + name + " encountered an error: " + throwable);
    }

    @Override
    public void onComplete() {
        System.out.println("User " + name + " has completed receiving videos.");
    }
}
