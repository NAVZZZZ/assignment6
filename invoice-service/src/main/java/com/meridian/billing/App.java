package com.meridian.billing;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class App {

    public static void main(String[] args) throws IOException {

        // Docker runs this mode to check application health.
        if (args.length == 1 && "--healthcheck".equals(args[0])) {
            System.exit(checkHealth() ? 0 : 1);
        }

        String customerName = "  Naveen  ";
        String cleanedName = StringUtils.trim(customerName);

        System.out.println("Customer name: " + cleanedName);
        System.out.println("Is name empty? " + StringUtils.isEmpty(cleanedName));

        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", 8080), 0);

        server.createContext("/health", exchange ->
                sendResponse(exchange, 200, "OK\n"));

        server.createContext("/", exchange ->
                sendResponse(exchange, 200,
                        "Invoice service is running\n"
                        + "Customer name: " + cleanedName + "\n"));

        server.start();
        System.out.println("Invoice service ready on port 8080");
    }

    private static void sendResponse(
            HttpExchange exchange, int status, String body) throws IOException {

        byte[] response = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set(
                "Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(status, response.length);

        try (OutputStream output = exchange.getResponseBody()) {
            output.write(response);
        }
    }

    private static boolean checkHealth() {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection)
                    new URL("http://127.0.0.1:8080/health").openConnection();

            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);

            return connection.getResponseCode() == 200;

        } catch (IOException e) {
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
