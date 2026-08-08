package main.contexts.blog.endpoints;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.util.SearchQuery;
import main.util.data.Post;

public class GetContentEndpoint implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            SearchQuery query = new SearchQuery(exchange.getRequestURI().getQuery());
            
            Headers headers = exchange.getResponseHeaders();
            headers.set("Access-Control-Allow-Origin", "*");

            String postContentString = null;
            try {
                Post post = Post.fromId(query.get("id"));
                postContentString = post.getContent();
            } catch (IOException e) {
                String errMessage = "Unexpected error while getting post data: " + e.getMessage();
                System.err.println(errMessage);

                exchange.sendResponseHeaders(500, errMessage.length());
                
                OutputStream output = exchange.getResponseBody();
                output.write(errMessage.getBytes());
            } finally {
                if (postContentString != null) {
                    headers.set("Content-Type", "text/markdown");
                    
                    exchange.sendResponseHeaders(200, postContentString.length());
                    
                    OutputStream output = exchange.getResponseBody();
                    output.write(postContentString.getBytes());
                } else {
                    exchange.sendResponseHeaders(404, HttpExchange.RSPBODY_EMPTY);
                }
            }
        }
    }
}
