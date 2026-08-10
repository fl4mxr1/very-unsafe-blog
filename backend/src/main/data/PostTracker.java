package main.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;

public class PostTracker {
    public static final PostTracker tracker = new PostTracker();
    private Map<String, Post> posts = new HashMap<>();

    public PostTracker() {
        try (DirectoryStream<Path> postStorageDirectory = 
            Files.newDirectoryStream(Path.of("storage/posts").toAbsolutePath())
        ) {
            for (Path path: postStorageDirectory) {
                File postDirectory = path.toFile();
                if (!postDirectory.isDirectory()) {
                    return;
                }
                String postId = postDirectory.getName();
                posts.put(
                    postId, 
                    Post.fromId(postId)
                );
            }
        } catch (IOException e) {
            System.out.println("Couldnt load existing posts due to IOException: " + e.getMessage());
        } finally {
            System.out.println(posts.toString());
        }
    }

    public Post getPostFromId(String id) {
        Post post = posts.get(id);
        if (post != null) {
            return post;
        }
        post = Post.fromId(id); // In case the post tracker does not have it
        System.out.println("Post wasnt loaded, loaded it");
        System.out.println(post.title);
        if (post != null) {
            posts.put(id, post);
        }
        return post;
    }

    public Post publishPost(String title, String author, String content) throws IOException, FileAlreadyExistsException {
        // GENERATING POST ID AND CREATING THE DIRECTORY
        String postId = UUID.randomUUID().toString();
        String postDirectoryPath = "storage/posts/" + postId;
        File postDirectory = new File(
            Path.of(postDirectoryPath).toAbsolutePath().toUri()
        );
        postDirectory.mkdirs();

        // GENERATING DATA JSON
        long postedAt = System.currentTimeMillis();
        String preview = content.substring(0, Math.min(99, content.length() - 1));

        JSONObject dataJson = new JSONObject();
        dataJson.put("id", postId);
        dataJson.put("title", title);
        dataJson.put("preview", preview);
        dataJson.put("postedAt", postedAt);
        dataJson.put("author", author);
        String dataJsonString = dataJson.toString();
        
        // CREATING AND WRITING TO DATA.JSON
        File dataJsonFile = new File(
            Path.of(postDirectory + "/data.json").toAbsolutePath().toUri()
        );
        dataJsonFile.getParentFile().mkdirs();
        
        if (!dataJsonFile.createNewFile()) {
            throw new FileAlreadyExistsException("Couldn't create post as one already exists with the randomly generated ID, please try again.");
        }
        try (FileWriter dataJsonWriter = new FileWriter(dataJsonFile)) {
            dataJsonWriter.write(dataJsonString);
        } catch (IOException e) {
            return null;
        }

        // CREATING AND WRITING TO CONTENT.MD
        File contentFile = new File(
            Path.of(postDirectory + "/content.md").toAbsolutePath().toUri()
        );
        if (!contentFile.createNewFile()) {
            throw new FileAlreadyExistsException("Couldn't create post, please try again.");
        }
        try (FileWriter contentFileWriter = new FileWriter(contentFile)) {
            contentFileWriter.write(content);
        } catch (IOException e) {
            return null;
        }

        Post post = new Post(postId, title, author, preview, postedAt);
        posts.put(postId, post);
        return post;
    }

    public Post[] queryPosts() {
        return null;
    }
}
