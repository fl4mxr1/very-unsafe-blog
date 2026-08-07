package main.contexts.blog.endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GetAllPostIDs implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        //Should take in query parameters:
        //page?: which page number
        //query?: search query
        //pageSize: how many posts in one page
        //If there is no page number specified it will instead respond with only page data (how many pages, page size etc)
    }
}
