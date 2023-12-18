package Serveur;
import java.io.IOException;
public class ThreadClientPool extends ThreadClient
{
    private FIleAttente connexionsEnAttente;
    public ThreadClientPool(Protocole protocole,FIleAttente file , ThreadGroup groupe) throws IOException
    {
        super(protocole,groupe);
        connexionsEnAttente = file;
    }
    @Override
    public void run()
    {
        System.out.println("Thread Démmarer attente d'une connexion");
        boolean interrompu = false;
        while(!interrompu)
        {
            try
            {
                System.out.println("Thread Démmarer attente d'une connexion");
                Csocket = connexionsEnAttente.getConnexion();
                super.run();
            }
            catch (InterruptedException ex)
            {
                interrompu = true;
            }
        }
    }
}
