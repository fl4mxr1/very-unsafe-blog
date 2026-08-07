package main;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import main.contexts.blog.BlogContext;

public class Main {
    static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer httpServer;
        try {
            httpServer = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        } catch (IOException e) {
            System.out.println("Failed to start server! Error: " + e.getMessage());
            return;
        }
        
        BlogContext blog = BlogContext.get();
        blog.createContexts(httpServer);
        httpServer.start();

        System.out.println("Server started at port " + SERVER_PORT);
        System.out.println("URL: http://localhost:" + SERVER_PORT);
    }
}