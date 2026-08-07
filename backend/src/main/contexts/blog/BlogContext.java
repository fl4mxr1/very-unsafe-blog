package main.contexts.blog;

import com.sun.net.httpserver.HttpServer;

import main.contexts.Context;
import main.contexts.blog.endpoints.GetContentEndpoint;
import main.contexts.blog.endpoints.GetDataEndpoint;
import main.contexts.blog.endpoints.PublishEndpoint;

public class BlogContext implements Context {
    public static BlogContext instance;

    private BlogContext() {}

    @Override
    public void createContexts(HttpServer server) {
        server.createContext("/blog/post/publish", new PublishEndpoint());
        server.createContext("/blog/post/get-data", new GetDataEndpoint());
        server.createContext("/blog/post/get-content", new GetContentEndpoint());
    }

    public static BlogContext get() {
        if (instance == null) {
            instance = new BlogContext();
        }
        return instance;
    }

    //TODO: Add /blog/post/get-all endpoint with pagination
}
