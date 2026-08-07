package main.contexts;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TestContext implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        System.out.println(IOUtils.toString(input, StandardCharsets.UTF_8));
        input.close();

        String response = "Hello from the server";
        exchange.sendResponseHeaders(200, response.length());

        OutputStream output = exchange.getResponseBody();
        output.write(response.getBytes());
        output.close();
    }
}
