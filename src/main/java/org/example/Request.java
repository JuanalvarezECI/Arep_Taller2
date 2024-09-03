package org.example;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private final String path;
    private final Map<String, String> queryParams = new HashMap<>();

    public Request(String path) {
        String[] parts = path.split("\\?");
        this.path = parts[0];
        if (parts.length > 1) {
            String[] params = parts[1].split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length > 1) {
                    queryParams.put(keyValue[0], keyValue[1]);
                } else {
                    queryParams.put(keyValue[0], "");
                }
            }
        }
    }

    public String getPath() {
        return path;
    }

    public String getValue(String key) {
        return queryParams.get(key);
    }
}