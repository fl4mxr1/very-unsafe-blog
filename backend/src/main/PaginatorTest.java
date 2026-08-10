package main;

import java.util.ArrayList;

import main.data.Post;
import main.util.Paginator;
import main.util.Paginator.Page;

public class PaginatorTest {
    public static void main(String[] args) {
        ArrayList<Post> arr = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            arr.add(new Post(String.valueOf(i), "helloworld", "h", "hello", 99999, "HHHHHHHH"));
        }

        Paginator paginator = new Paginator(arr.toArray());
        Page page = paginator.getPage(501, 1);
        System.out.println(page.data.length);
        System.out.println(page.toJson().toString());
    }
}
