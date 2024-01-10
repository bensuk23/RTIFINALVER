package Serveur;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HandlerJs implements HttpHandler {
    @Override public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content- Type");
        // Lecture de la requete
        String requestPath = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();
        System.out.print("HandlerJS (methode " + requestMethod + ") = " + requestPath + " --> ");
        // Ecriture de la reponse
        if (requestPath.endsWith(".js")) {
            String fichier = "C:\\Users\\benja\\RtiProjotSave\\RTIFINALVER\\ServeurHttp\\src\\Web\\" + requestPath;

            File file = new File(fichier);
            if (file.exists()) {
                exchange.sendResponseHeaders(200, file.length());
                exchange.getResponseHeaders().set("Content-Type", "text/javascript");
                OutputStream os = exchange.getResponseBody();
                Files.copy(file.toPath(), os);
                os.close();
                System.out.println("OK");
            }
            else
                Erreur404(exchange);
        } else
            Erreur404(exchange);
    }
    private void Erreur404(HttpExchange exchange) throws IOException {
        String reponse = "Fichier CSS introuvable !!!";
        exchange.sendResponseHeaders(404, reponse.length());
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        OutputStream os = exchange.getResponseBody();
        os.write(reponse.getBytes());
    }
}

