package Serveur;

import Donnee.Article;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static Serveur.ServHttp.beanJDBC;


// Gestionnaire pour les requêtes HTTP
public class HandlerStock implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Récupérer la méthode de la requête (GET, POST, etc.)
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin","*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods","GET,POST");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers","ContentType");

        System.out.println("handlers.HandlerStock (methode " + requestMethod + ") = " + requestPath + " --> ");

        if ("GET".equals(requestMethod)) {
            try {
                List<Article> articles = beanJDBC.performSelectArticlesRetList();
                String articlesJson = convertArticlesToJson(articles);
                sendResponse(exchange,200, articlesJson);
            } catch (SQLException e) {
                e.printStackTrace();
                sendResponse(exchange,400, "Erreur lors de la récupération des articles.");
            }
        } else if ("POST".equals(requestMethod)) {
            try {

                String requestBody = new String(exchange.getRequestBody().readAllBytes());

                // Convertir les données JSON en objet Article à l'aide de JSONObject
                Article updatedArticle = convertJsonToArticle(requestBody);

                int rowsAffected = beanJDBC.performUpdateArticle(updatedArticle);
                if (rowsAffected > 0) {
                    sendResponse(exchange,201, "Stock mis à jour avec succès.");
                } else {
                    sendResponse(exchange,400, "Échec de la mise à jour du stock.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                sendResponse(exchange,400, "Erreur lors de la mise à jour du stock.");
            }
        } else if ("OPTIONS".equals(requestMethod)) {
            // Respond to the OPTIONS request with CORS headers
            handleOptionsRequest(exchange);
        }
        else sendResponse(exchange, 405, "Methode non autorisee");

    }

    private void handleOptionsRequest(HttpExchange exchange) throws IOException {
        // Set CORS headers for OPTIONS request
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        // Respond with a 200 OK status
        exchange.sendResponseHeaders(200, -1);

        // Close the response stream
        OutputStream os = exchange.getResponseBody();
        os.close();
    }

    private String convertArticlesToJson(List<Article> articles) {
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (Article article : articles) {
            jsonBuilder.append("{\"id\":").append(article.getId())
                    .append(",\"intitule\":\"").append(article.getNom())
                    .append("\",\"prix\":").append(article.getPrix())
                    .append(",\"stock\":").append(article.getQuantite())
                    .append(",\"image\":\"").append(article.getImage()).append("\"},");
        }
        if (articles.size() > 0)
        {
            // Supprimer la virgule finale
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response)
    {
        System.out.println("Envoi de la Réponse : " + response + " code : " + statusCode);
        try {

            byte[] responseBytes = response.getBytes("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Content-Length", String.valueOf(responseBytes.length));
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    private Article convertJsonToArticle(String json) {
        JSONObject jsonObject = new JSONObject(json);
        int id = jsonObject.getInt("id");
        String nom = jsonObject.getString("intitule");
        float prix = (float) jsonObject.getDouble("prix");
        int stock = jsonObject.getInt("stock");
        return new Article(id, nom, prix, stock);
    }


}
