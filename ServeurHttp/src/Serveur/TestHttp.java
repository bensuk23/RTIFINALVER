package Serveur;

import com.sun.net.httpserver.*;
import java.io.*;
import java.util.*;
import java.net.*;
public class TestHttp
{
    private static List<String> tasks = new ArrayList<>();
    public static void main(String[] args) throws IOException
    {
        System.out.println("API Rest demarree...");
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/tasks", new TaskHandler());

        server.start();
    }
    static class TaskHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange exchange) throws IOException
        {
            String requestMethod = exchange.getRequestMethod();
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin","*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods","GET,POST,DELETE,PUT");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers","ContentType");
            System.out.println(requestMethod);


            if (requestMethod.equalsIgnoreCase("GET"))
            {
                System.out.println("--- Requête GET reçue (obtenir la liste) ---");
                // Récupérer la liste des tâches au format JSON
                String response = convertTasksToJson();
                sendResponse(exchange, 200, response);
            }
            else if (requestMethod.equalsIgnoreCase("POST"))
            {
                System.out.println("--- Requête POST reçue (ajout) ---");
                // Ajouter une nouvelle tâche
                String requestBody = readRequestBody(exchange);
                System.out.println("requestNody = " + requestBody);
                addTask(requestBody);
                sendResponse(exchange, 201, "Tache ajoutee avec succes");
            }
            else if (requestMethod.equalsIgnoreCase("PUT"))
            {
                System.out.println("--- Requête PUT reçue (mise a jour) ---");
                // Mettre à jour une tâche existante
                Map<String, String> queryParams =
                        parseQueryParams(exchange.getRequestURI().getQuery());
                if (queryParams.containsKey("id"))
                {
                    int taskId = Integer.parseInt(queryParams.get("id"));
                    System.out.println("Mise a jour tache id=" + taskId);
                    String requestBody = readRequestBody(exchange);
                    System.out.println("requestNody = " + requestBody);
                    updateTask(taskId, requestBody);
                    sendResponse(exchange, 200, "Tache mise a jour avec succes");
                }
                else sendResponse(exchange, 400, "ID de tache manquant dans les parametres");
            }
            else if (requestMethod.equalsIgnoreCase("DELETE"))
            {
                System.out.println("--- Requête DELETE reçue (suppression) ---");// Supprimer une tâche
                Map<String, String> queryParams =
                        parseQueryParams(exchange.getRequestURI().getQuery());
                if (queryParams.containsKey("id"))
                {
                    int taskId = Integer.parseInt(queryParams.get("id"));
                    System.out.println("Suppression tache id=" + taskId);
                    deleteTask(taskId);
                    sendResponse(exchange, 200, "Tache supprimee avec succes");
                }
                else sendResponse(exchange, 400, "ID de tache manquant dans les parametres");
            }
            else sendResponse(exchange, 405, "Methode non autorisee");
        }
    }
    private static void sendResponse(HttpExchange exchange, int statusCode, String
            response) throws IOException
    {
        System.out.println("Envoi de la réponse (" + statusCode + ") : --" + response + "--");
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static String readRequestBody(HttpExchange exchange) throws IOException
    {
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            requestBody.append(line);
        }
        reader.close();
        return requestBody.toString();
    }
    private static Map<String, String> parseQueryParams(String query)
    {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null)
        {
            String[] params = query.split("&");
            for (String param : params)
            {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2)
                {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }
    private static String convertTasksToJson()
    {
        // Convertir la liste des tâches en format JSON
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < tasks.size(); i++)
        {
            json.append("{\"id\": ").append(i + 1).append(", \"task\":\"").append(tasks.get(i)).append("\"}");
            if (i < tasks.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
    private static void addTask(String task)
    {
        tasks.add(task);
    }
    private static void updateTask(int taskId, String updatedTask)
    {
        if (taskId >= 1 && taskId <= tasks.size())
        {
            tasks.set(taskId - 1, updatedTask);
        }
    }
    private static void deleteTask(int taskId)
    {
        if (taskId >= 1 && taskId <= tasks.size())
        {
            tasks.remove(taskId - 1);
        }
    }
}