package main.contexts.blog.endpoints;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONObject;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.data.Post;
import main.data.PostTracker;
import main.util.UrlQueryParameters;

public class GetDataEndpoint implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            UrlQueryParameters query = new UrlQueryParameters(exchange.getRequestURI().getQuery());
            
            Headers headers = exchange.getResponseHeaders();
            headers.set("Access-Control-Allow-Origin", "*");

            String postDataJsonString = null;
            try {
                System.out.println("getting post");
                Post post = PostTracker.tracker.getPostFromId(query.get("id"));
                System.out.println(post);
                if (post == null) {
                    System.out.println("doesnt exist");
                    throw new IOException("Post either doesn't exist or there was an unexpected error.");
                }

                JSONObject postJson = new JSONObject(post);
                postDataJsonString = postJson.toString();
            } catch (IOException e) {
                String errMessage = "Unexpected error while getting post data: " + e.getMessage();
                System.err.println(errMessage);
                
                exchange.sendResponseHeaders(500, errMessage.length());
                
                OutputStream output = exchange.getResponseBody();
                output.write(errMessage.getBytes());

                System.out.println(e.getMessage());
            } finally {
                if (postDataJsonString != null) {
                    headers.set("Content-Type", "application/json");
                    
                    exchange.sendResponseHeaders(200, postDataJsonString.length());
                    
                    OutputStream output = exchange.getResponseBody();
                    output.write(postDataJsonString.getBytes());
                } else {
                    exchange.sendResponseHeaders(404, HttpExchange.RSPBODY_EMPTY);
                }
            }
        }
    }
}
