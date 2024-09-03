package org.example;

import java.util.function.BiFunction;

public class Route {
    private final String path;
    private final BiFunction<Request, Response, String> handler;

    public Route(String path, BiFunction<Request, Response, String> handler) {
        this.path = path;
        this.handler = handler;
    }

    public String getPath() {
        return path;
    }

    public BiFunction<Request, Response, String> getHandler() {
        return handler;
    }
}