package main.contexts.blog.endpoints;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;

import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.data.Post;
import main.data.PostTracker;
import main.util.UrlQueryParameters;

public class PublishEndpoint implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            if (!"POST".equals(exchange.getRequestMethod())) { // Make sure it's a post request
                String errMessage = "Method not allowed. You can only use POST.";
                exchange.sendResponseHeaders(405, errMessage.length());
                OutputStream response = exchange.getResponseBody();
                response.write(errMessage.getBytes());

                return;
            }

            UrlQueryParameters query = new UrlQueryParameters(exchange.getRequestURI().getQuery());
            if (!query.exists("title") || !query.exists("author")) { // Make sure URL has title and author parameters
                String errMessage = "URL missing title or author parameters.";
                exchange.sendResponseHeaders(400, errMessage.length());
                OutputStream response = exchange.getResponseBody();
                response.write(errMessage.getBytes());

                return;
            }

            Headers headers = exchange.getRequestHeaders();
            if (!headers.getFirst("Content-Type").equals("text/markdown")) { // Make sure request body is a markdown file
                String errMessage = "Request body has to be text/markdown.";
                exchange.sendResponseHeaders(400, errMessage.length());
                OutputStream response = exchange.getResponseBody();
                response.write(errMessage.getBytes());

                return;
            }

            InputStream requestBody = exchange.getRequestBody(); // post content markdown file

            String title = query.get("title");
            String author = query.get("author");
            String content = IOUtils.toString(requestBody, StandardCharsets.UTF_8);

            if (content.isBlank() || content.isEmpty()) {
                String errMessage = "Request body cannot be empty or only whitespace.";
                exchange.sendResponseHeaders(400, errMessage.length());
                OutputStream response = exchange.getResponseBody();
                response.write(errMessage.getBytes());

                return;
            }

            String postId = null;
            try {
                Post post = PostTracker.tracker.publishPost(title, author, content);
                postId = post.id;
            } catch (FileAlreadyExistsException e) {
                String errMessage = "Error when creating post files, please try again. " + e.getMessage();
                exchange.sendResponseHeaders(500, errMessage.length());
                OutputStream response = exchange.getResponseBody();
                response.write(errMessage.getBytes());
            } catch (IOException e) {
                String errMessage = "Unexpected error when creating post: " + e.getMessage();
                exchange.sendResponseHeaders(500, errMessage.getBytes().length);
                OutputStream response = exchange.getResponseBody();
                response.write(errMessage.getBytes());
            } finally {
                if (postId != null) {
                    exchange.sendResponseHeaders(200, postId.length());

                    OutputStream response = exchange.getResponseBody();
                    response.write(postId.getBytes());
                }
            }
        }
    }
}
