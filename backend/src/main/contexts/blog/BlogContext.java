package main.contexts.blog;

import com.sun.net.httpserver.HttpServer;

import main.contexts.Context;
import main.contexts.blog.endpoints.GetContentEndpoint;
import main.contexts.blog.endpoints.GetDataEndpoint;
import main.contexts.blog.endpoints.PublishEndpoint;

public class BlogContext implements Context {
    public BlogContext() {}

    @Override
    public void createContexts(HttpServer server) {
        server.createContext("/post/publish", new PublishEndpoint());
        server.createContext("/post/get-data", new GetDataEndpoint());
        server.createContext("/post/get-content", new GetContentEndpoint());
    }

    //TODO: Add /blog/post/get-all endpoint with pagination
}
