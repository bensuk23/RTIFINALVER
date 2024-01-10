package ControllerGuiClient;

import java.io.IOException;
import java.net.Socket;

public class Tcp {
    public static Socket csocket;
    public static void Connexion()
    {
        try {
            csocket = new Socket("192.168.161.161", 50000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Connexion établie.");

        // Caractéristiques de la socket
        System.out.println("--- Socket ---");
        System.out.println("Adresse IP locale : " + csocket.getLocalAddress().getHostAddress());
        System.out.println("Port local : " + csocket.getLocalPort());
        System.out.println("Adresse IP distante : " + csocket.getInetAddress().getHostAddress());
        System.out.println("Port distant : " + csocket.getPort());
    }
}
