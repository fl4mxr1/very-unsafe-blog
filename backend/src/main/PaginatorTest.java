package main;

import java.util.ArrayList;

import main.util.Paginator;
import main.util.Paginator.Page;
import main.util.data.Post;

public class PaginatorTest {
    public static void main(String[] args) {
        ArrayList<Post> arr = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            arr.add(new Post(String.valueOf(i), "helloworld", "h", 99999, "HHHHHHHH"));
        }

        Paginator paginator = new Paginator(arr.toArray());
        Page page = paginator.getPage(501, 1);
        System.out.println(page.data.length);
        System.out.println(page.toJson().toString());
    }
}
