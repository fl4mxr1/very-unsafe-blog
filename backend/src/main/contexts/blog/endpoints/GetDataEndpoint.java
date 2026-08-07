package main.contexts.blog.endpoints;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.util.SearchQuery;

public class GetDataEndpoint implements HttpHandler {
    private String getPostData(String id) throws IOException {
        InputStream stream;
        try {
            stream = Files.newInputStream(
                Paths.get("storage/posts/" + id + "/data.json"), 
                StandardOpenOption.READ
            );  
        } catch (IOException e) {
            return null;
        }
        if (stream == null) {
            return null;
        }
        return IOUtils.toString(stream, StandardCharsets.UTF_8);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            SearchQuery query = new SearchQuery(exchange.getRequestURI().getQuery());
            
            Headers headers = exchange.getResponseHeaders();
            headers.set("Access-Control-Allow-Origin", "*");

            String postDataJsonString = null;
            
            try {
                postDataJsonString = getPostData(query.get("id"));
            } catch (IOException e) {
                String errMessage = "Unexpected error while getting post data: " + e.getMessage();
                System.err.println(errMessage);
                
                exchange.sendResponseHeaders(500, errMessage.length());
                
                OutputStream output = exchange.getResponseBody();
                output.write(errMessage.getBytes());
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
