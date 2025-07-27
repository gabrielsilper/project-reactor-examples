package com.github.gabrielsilper.project_reactor_examples.exceptions;

public class VideoMonetizationException extends RuntimeException {

    public VideoMonetizationException() {
        super("Video views are too low for monetization");
    }

    public VideoMonetizationException(String s) {
        super(s);
    }
}
