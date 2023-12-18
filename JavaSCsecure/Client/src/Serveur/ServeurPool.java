package Serveur;
import PackVESPAP.*;
import PackVESPAP.VESPAP;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.util.Properties;

public class ServeurPool extends ThreadServeur
{
    private FIleAttente  connexionEnAttente;
    private ThreadGroup pool;
    private int taillePool;

    private static int PORT_PAIEMENT;

    public ServeurPool(int port, Protocole protocole, int taillePool) throws IOException
    {
        super(port,protocole);
        connexionEnAttente = new FIleAttente();
        pool = new ThreadGroup("POOL");
        this.taillePool = taillePool;
    }
    @Override
    public void run()
    {
        System.out.println("Serveur Démmarer");
        for(int i=0;i<taillePool;i++)
        {
            try {
                new ThreadClientPool(protocole,connexionEnAttente,pool).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        while(!this.isInterrupted())
        {

            Socket Csocket;
            try
            {
                Ssocket.setSoTimeout(20000);
                Csocket = Ssocket.accept();

                connexionEnAttente.addConnexion(Csocket);
            }
            catch (IOException ex)
            {
            }
        }
        pool.interrupt();
    }

    public static void main(String[] args) throws IOException {

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
        int taillePool = Integer.parseInt(properties.getProperty("taillePool", "5"));
        PORT_PAIEMENT = Integer.parseInt(properties.getProperty("port", "50001"));
       // Remplacez par le port que vous souhaitez utiliser
        Protocole protocole = null;
        protocole = new VESPAP(); // Remplacez VotreProtocole par votre implémentation de Protocole

        // Remplacez VotreLogger par votre implémentation de Logger

        try
        {
            ServeurPool serveurPool = new ServeurPool(PORT_PAIEMENT, protocole, taillePool);

            serveurPool.start();

        } catch (IOException e) {
            e.printStackTrace(); // Gérez l'exception de manière appropriée
        }



    }
}
