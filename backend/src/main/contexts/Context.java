package main.contexts;

import com.sun.net.httpserver.HttpServer;

public interface Context {
    public void createContexts(HttpServer server);
}
