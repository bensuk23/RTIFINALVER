package Serveur;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject; // Importez la classe JSONObject

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.List;

import Donnee.Article;
import PackJDBC.BeanJDBC;
import PackJDBC.DatabaseBean;

public class ServHttp {

    private static BeanJDBC beanJDBC;

    public static void main(String[] args) throws IOException, SQLException {
        DatabaseBean databaseBean = new DatabaseBean("jdbc:mysql://192.168.126.128:3306/PourStudent", "Student", "PassStudent1_");
        beanJDBC = new BeanJDBC(databaseBean);

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/stock", new StockHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("Serveur démarré sur le port 8080");
    }

    // Gestionnaire pour les requêtes HTTP
    static class StockHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Récupérer la méthode de la requête (GET, POST, etc.)
            String requestMethod = exchange.getRequestMethod();

            if ("GET".equals(requestMethod)) {
                try {
                    List<Article> articles = beanJDBC.performSelectFactureRetList(1);
                    String articlesJson = convertArticlesToJson(articles);
                    sendResponse(exchange, articlesJson);
                } catch (SQLException e) {
                    e.printStackTrace();
                    sendResponse(exchange, "Erreur lors de la récupération des articles.");
                }
            } else if ("POST".equals(requestMethod)) {
                try {
                    System.out.println("coucou");

                    String requestBody = new String(exchange.getRequestBody().readAllBytes());

                    // Convertir les données JSON en objet Article à l'aide de JSONObject
                    Article updatedArticle = convertJsonToArticle(requestBody);
                    //System.out.println(requestBody);
                    int rowsAffected = beanJDBC.performUpdateArticle(updatedArticle);
                    if (rowsAffected > 0) {
                        System.out.println("Rows affected: " + rowsAffected);
                        sendResponse(exchange, "Stock mis à jour avec succès.");
                        System.out.println("coucou5");
                    } else {
                        sendResponse(exchange, "Échec de la mise à jour du stock.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    sendResponse(exchange, "Erreur lors de la mise à jour du stock.");
                }
            }
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
            if (articles.size() > 0) {
                // Supprimer la virgule finale
                jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
            }
            jsonBuilder.append("]");
            return jsonBuilder.toString();
        }

        private void sendResponse(HttpExchange exchange, String response) {
            try {
                byte[] responseBytes = response.getBytes("UTF-8");
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.getResponseHeaders().set("Content-Length", String.valueOf(responseBytes.length));
                exchange.sendResponseHeaders(200, responseBytes.length);
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
            String image = jsonObject.getString("image");
            return new Article(id, nom, prix, stock, image);
        }
    }
}
