package com.phantasment.uiautomationtest2.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class ImageChecker
{
    private ImageChecker() { }

    public static boolean isValidImage(String url) throws IOException, InterruptedException
    {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url)).build();
        HttpResponse response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
        return response.statusCode() == 200;
    }
}
