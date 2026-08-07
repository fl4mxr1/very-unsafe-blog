package main;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    public static Map<String, String> queryToMap(String query) {
        if (query == null) {
            return null;
        }
        HashMap<String, String> map = new HashMap<>();
        for (String param: query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
            map.put(
                URLDecoder.decode(entry[0], StandardCharsets.UTF_8), 
                URLDecoder.decode(entry[1], StandardCharsets.UTF_8)
            );
        } else {
            map.put(
                URLDecoder.decode(entry[0], StandardCharsets.UTF_8),
                ""
            );
        }
        }
        return map;
    }
}