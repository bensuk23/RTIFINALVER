package Serveur;
import java.io.IOException;

import java.net.Socket;
public class ThreadClientDemande extends ThreadClient
{
    public ThreadClientDemande(Protocole protocole, Socket csocket)
            throws IOException
    {
        super(protocole, csocket);
    }
    @Override
    public void run()
    {
        System.out.println("Thread Démmarer ");
        super.run();
        System.out.println("Thread Terminer");
    }
}