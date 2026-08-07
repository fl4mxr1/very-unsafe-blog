package main.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

// Parses a URL search query string to make it easier to access.
public class SearchQuery {
    private Map<String, String> queryMap = new HashMap<>();

    public SearchQuery(String query) {
        if (query == null) {
            return;
        }
        for (String param: query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
            queryMap.put(
                URLDecoder.decode(entry[0], StandardCharsets.UTF_8), 
                URLDecoder.decode(entry[1], StandardCharsets.UTF_8)
            );
        } else {
            queryMap.put(
                URLDecoder.decode(entry[0], StandardCharsets.UTF_8),
                ""
            );
        }
        }
    }

    // Simply gets the parameter of the specified key.
    public String get(String key) {
        return queryMap.get(key);
    }

    // Gets the parameter of the specified key and parses it as an integer.
    public int getAsInt(String key) throws NumberFormatException {
        return Integer.parseInt(queryMap.get(key));
    }

    // Returns true if the parameter of the specified key exists.
    // Useful for checking for "flags" (as in, an empty parameter that acts as a truthy value).
    public boolean exists(String key) {
        return queryMap.containsKey(key);
    }
}
