package com.census.dashboard;

import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Map;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:5000/api";
    private final OkHttpClient client;
    private final Gson gson;

    public ApiClient() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public JsonObject getAllStatistics() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/statistics")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, JsonObject.class);
        }
    }

    public JsonObject getAgeStatistics() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/statistics/age")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, JsonObject.class);
        }
    }

    public JsonObject getEducationStatistics() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/statistics/education")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, JsonObject.class);
        }
    }

    public JsonObject getIncomeStatistics() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/statistics/income")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, JsonObject.class);
        }
    }

    public JsonObject getWorkHoursStatistics() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/statistics/work-hours")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, JsonObject.class);
        }
    }

    public JsonObject getData(Map<String, Object> filters, int limit) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "/data").newBuilder();
        urlBuilder.addQueryParameter("limit", String.valueOf(limit));

        Request.Builder requestBuilder = new Request.Builder().url(urlBuilder.build());

        if (filters != null && !filters.isEmpty()) {
            JsonObject json = new JsonObject();
            json.add("filters", gson.toJsonTree(filters));
            
            RequestBody body = RequestBody.create(
                gson.toJson(json),
                MediaType.parse("application/json")
            );
            
            requestBuilder.post(body);
        }

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, JsonObject.class);
        }
    }

    public boolean exportData(String format, Map<String, Object> filters) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("format", format);
        if (filters != null && !filters.isEmpty()) {
            json.add("filters", gson.toJsonTree(filters));
        }

        RequestBody body = RequestBody.create(
            gson.toJson(json),
            MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/export")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }
}
