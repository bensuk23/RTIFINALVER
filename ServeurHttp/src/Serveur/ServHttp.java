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

    public static BeanJDBC beanJDBC;

    public static void main(String[] args) throws IOException, SQLException {
        DatabaseBean databaseBean = new DatabaseBean("jdbc:mysql://192.168.161.161:3306/PourStudent", "Student", "PassStudent1_");
        beanJDBC = new BeanJDBC(databaseBean);

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/stock", new HandlerStock());
        server.createContext("/", new HandlerHtml());
        server.createContext("/css",new HandlerCss());
        server.createContext("/js",new HandlerJs());
        server.createContext("/images",new HandlerImages());
        server.setExecutor(null);
        server.start();

        System.out.println("Serveur démarré sur le port 8080");
    }



}
