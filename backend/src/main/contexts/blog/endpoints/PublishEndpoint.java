package main.contexts.blog.endpoints;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class PublishEndpoint implements HttpHandler {
    private String publishPost(String title, String author, String content) throws IOException, FileAlreadyExistsException {
        // GENERATING POST ID
        String postId = UUID.randomUUID().toString();
        String postDirectory = "storage/posts/" + postId;

        // GENERATING DATA JSON
        JSONObject dataJson = new JSONObject();
        dataJson.put("id", postId);
        dataJson.put("title", title);
        dataJson.put("preview", (String) content.substring(0, Math.min(99, content.length() - 1))); // i have to cast this to a string??
        dataJson.put("postedAt", System.currentTimeMillis());
        dataJson.put("author", author);
        String dataJsonString = dataJson.toString();

        // CREATING AND WRITING TO DATA.JSON
        File dataJsonFile = new File(
            Paths.get(postDirectory + "/data.json").toAbsolutePath().toUri()
        );
        dataJsonFile.getParentFile().mkdirs();
        System.out.println(Paths.get(postDirectory + "/data.json").toAbsolutePath().toString());
        if (!dataJsonFile.createNewFile()) {
            throw new FileAlreadyExistsException("Couldn't create post as one already exists with the randomly generated ID, please try again.");
        } else {
            System.out.println("created");
        }
        try (FileWriter dataJsonWriter = new FileWriter(dataJsonFile)) {
            dataJsonWriter.write(dataJsonString);
        } catch (IOException e) {
            System.out.println("IOException when writing to data.json: " + e.getMessage());
            return null;
        }
        System.out.println("Successfully wrote to " + postDirectory + "/data.json");

        // CREATING AND WRITING TO CONTENT.MD
        File contentFile = new File(
            Paths.get(postDirectory + "/content.md").toAbsolutePath().toUri()
        );
        if (!contentFile.createNewFile()) {
            throw new FileAlreadyExistsException("Couldn't create post, please try again.");
        }
        try (FileWriter contentFileWriter = new FileWriter(contentFile)) {
            contentFileWriter.write(content);
        } catch (IOException e) {
            System.out.println("IOException when writing to content.md: " + e.getMessage());
            return null;
        }
        System.out.println("Successfully wrote to " + postDirectory + "/content.md");
        
        System.out.println("Returning post id");
        return postId;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            System.out.println("Received request");
            if (!"POST".equals(exchange.getRequestMethod())) {
                String errMessage = "Method not allowed. You can only use POST.";
                exchange.sendResponseHeaders(405, errMessage.length());
                OutputStream response = exchange.getResponseBody();
                response.write(errMessage.getBytes());
                System.out.println("invalid method");

                return;
            }

            Headers headers = exchange.getRequestHeaders();
            if (!headers.getFirst("Content-Type").equals("application/json")) {
                String errMessage = "Request body has to be a JSON.";
                exchange.sendResponseHeaders(400, errMessage.length());
                OutputStream response = exchange.getResponseBody();
                response.write(errMessage.getBytes());
                System.out.println("invalid content-type");

                return;
            }

            InputStream body = exchange.getRequestBody(); // json containing author, title and content

            // PARSING REQUEST BODY
            String bodyJson = IOUtils.toString(body, StandardCharsets.UTF_8);
            JSONObject bodyJsonObject;
            try {
                bodyJsonObject = new JSONObject(bodyJson);
            } catch (JSONException e) {
                String errMessage = "Could not parse request body JSON: " + e.getMessage();
                exchange.sendResponseHeaders(400, errMessage.length());
                OutputStream response = exchange.getResponseBody();
                response.write(errMessage.getBytes());
                System.out.println("couldnt parse request body");

                return;
            }

            String title;
            String author;
            String content;

            try {
                title = (String) bodyJsonObject.get("title");
                author = (String) bodyJsonObject.get("author");
                content = (String) bodyJsonObject.get("content");
            } catch (JSONException e) {
                String errMessage = "Request body JSON is missing title, author or content. Error: " + e.getMessage();
                exchange.sendResponseHeaders(400, errMessage.length());
                OutputStream response = exchange.getResponseBody();
                response.write(errMessage.getBytes());
                System.out.println("invalid body json");

                return;
            }

            String postId = null;
            try {
                System.out.println("creating post");
                postId = publishPost(title, author, content);
                System.out.println("post published");
            } catch (FileAlreadyExistsException e) {
                String errMessage = "Error when creating post files, please try again. " + e.getMessage();
                exchange.sendResponseHeaders(500, errMessage.length());
                OutputStream response = exchange.getResponseBody();
                response.write(errMessage.getBytes());

                System.out.println("already exists");
            } catch (IOException e) {
                String errMessage = "Unexpected error when creating post: " + e.getMessage();
                exchange.sendResponseHeaders(500, errMessage.getBytes().length);
                OutputStream response = exchange.getResponseBody();
                response.write(errMessage.getBytes());

                System.out.println("ioexception");
            } finally {
                if (postId != null) {
                    exchange.sendResponseHeaders(200, postId.length());

                    OutputStream response = exchange.getResponseBody();
                    response.write(postId.getBytes());

                    System.out.println("Succesfully created post");
                } else {
                    System.out.println("Failed to create post for some reason");
                }
            }
        }
    }
}
