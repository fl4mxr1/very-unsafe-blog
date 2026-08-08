package main.util.data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.JSONPropertyName;

public class Post {
    public final String id;
    public final String title;
    public final String author;
    public final String preview;
    public final long postedAt;

    public Post(String id, String title, String author, String preview, long postedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.postedAt = postedAt;
        this.preview = preview;
    }

    public Post(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        
        String id = jsonObject.getString("id");
        String title = jsonObject.getString("title");
        String author = jsonObject.getString("author");
        String preview = jsonObject.getString("preview");
        Long postedAt = jsonObject.getLong("postedAt");

        if (id == null || title == null || author == null || postedAt == null) {
            throw new InvalidPostJsonException("Missing id, title, author or post creation timestamp.");
        }

        this.id = id;
        this.title = title;
        this.author = author;
        this.preview = preview;
        this.postedAt = postedAt;
    }
    
    // Finds a post using its post id string. May return null if it isn't found or if any errors occur.
    public static Post fromId(String id) {
        InputStream stream;
        try {
            stream = Files.newInputStream(
                Path.of("storage/posts/" + id + "/data.json"), 
                StandardOpenOption.READ
            );  
        } catch (IOException e) {
            return null;
        }
        if (stream == null) {
            return null;
        }
        String jsonString;
        try {
            jsonString = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Couldn't parse data.json of post id " + id + ", error:" + e.getMessage());
            return null;
        }
        Post post = null;
        try {
            post = new Post(jsonString);
        } catch (Post.InvalidPostJsonException e) {
            System.err.println("Couldn't get post data because the file is invalid. Error: " + e.getMessage());
        }
        return post;
    }

    public String getContent() throws IOException {
        InputStream stream;
        try {
            stream = Files.newInputStream(
                Paths.get("storage/posts/" + id + "/content.md"), 
                StandardOpenOption.READ
            );  
        } catch (IOException e) {
            return null;
        }
        if (stream == null) {
            return null;
        }
        String contentString;
        try {
            contentString = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Couldn't parse content.md of post id " + id + ", error:" + e.getMessage());
            return null;
        }
        return contentString;
    }

    @JSONPropertyName("id")
    public String getId() {
        return id;
    }

    @JSONPropertyName("title")
    public String getTitle() {
        return title;
    }

    @JSONPropertyName("author")
    public String getAuthor() {
        return author;
    }

    @JSONPropertyName("postedAt")
    public long getCreationTime() {
        return postedAt;
    }

    public static class InvalidPostJsonException extends RuntimeException {
        public InvalidPostJsonException(String message) { super(message); }
        @Override
        public String getMessage() { return super.getMessage(); }
    }
}