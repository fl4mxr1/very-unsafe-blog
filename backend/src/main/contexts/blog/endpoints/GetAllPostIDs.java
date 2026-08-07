package main.contexts.blog.endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GetAllPostIDs implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        //Should take in query parameters:
        //page?: which page number
        //query?: search query
        //If there is no page number specified it will instead respond with page data (how many pages, page size etc)
    }
}
