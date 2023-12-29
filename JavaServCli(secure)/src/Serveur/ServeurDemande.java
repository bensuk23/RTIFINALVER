package Serveur;

import PackVESPAPS.VESPAPS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;


import java.security.Security;
import java.util.Properties;

public class ServeurDemande extends ThreadServeur
{


    private static int PORT_PAIEMENT_SECURE;
    public ServeurDemande(int port, Protocole protocole) throws IOException
    {
        super(port, protocole);
    }
    @Override
    public void run()
    {

        while(!this.isInterrupted())
        {
            Socket csocket;
            try
            {

                Ssocket.setSoTimeout(2000);
                csocket = Ssocket.accept();

                Thread th = new ThreadClientDemande(protocole,csocket);


                th.start();

            }
            catch (SocketTimeoutException ex)
            {
                // Pour vérifier si le thread a été interrompu
            }
            catch (IOException ex)
            {
            }
        }

        try { Ssocket.close(); }
        catch (IOException ex) { }
    }


    public static void main(String[] args) throws IOException
    {


        Properties properties = new Properties();
        FileInputStream input = null;
        try {
            input = new FileInputStream("C:/Users/benja/TEST/config.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PORT_PAIEMENT_SECURE = Integer.parseInt(properties.getProperty("PORT_PAIEMENT_SECURE", "50002"));
        // Remplacez par le port que vous souhaitez utiliser
        Protocole protocole = null;
        protocole = new VESPAPS(); // Remplacez VotreProtocole par votre implémentation de Protocole

        // Remplacez VotreLogger par votre implémentation de Logger

        try
        {
            ServeurDemande serveurDemande = new ServeurDemande(PORT_PAIEMENT_SECURE, protocole);
            System.out.println("Thread Démmarer attente d'une requette");

            serveurDemande.run();

        } catch (IOException e) {
            e.printStackTrace(); // Gérez l'exception de manière appropriée
        }



    }
}