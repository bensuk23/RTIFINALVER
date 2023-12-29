package Serveur;

import java.io.*;
import java.net.Socket;

public abstract class ThreadClient extends Thread
{
    protected Protocole protocole;
    protected Socket Csocket;
    private int numero;
    private static int numCourant = 1;
    public ThreadClient(Protocole protocole,Socket csocket) throws IOException
    {
        super("Th Client" + numCourant + "(protocole= " + protocole.getNom() + ")" );
        this.protocole= protocole;
        this.Csocket = csocket;
        this.numero = numCourant;
    }
    public ThreadClient(Protocole protocole,ThreadGroup groupe) throws  IOException
    {
        super(groupe,"TH Client " + numCourant + " (protocole= " + protocole.getNom() + ")");
        this.protocole = protocole;
        this.Csocket = null;
        this.numero = numCourant++;
    }
    @Override
    public void run()
    {
        try
        {


            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            try
            {
                ois = new ObjectInputStream(Csocket.getInputStream());
                oos = new ObjectOutputStream(Csocket.getOutputStream());

                while(true)
                {
                    Requete requete = (Requete) ois.readObject();
                    Reponse reponse = protocole.TraiteRequete(requete);
                    oos.writeObject(reponse);

                }

            }
            catch (FinConnexionException ex)
            {
                if(oos!=null && ex.getReponse() !=null)
                {
                    System.out.println("Thread Démmarer attente d'une requette");
                    oos.writeObject(ex.getReponse());
                }
            }
        }
        catch (IOException ex){}
        catch (ClassNotFoundException ex) {}
        finally
        {
            try
            {
                System.out.println("Thread Démmarer attente d'une requette");
                Csocket.close();
            }
            catch (IOException ex )
            {
            }
        }
    }
}
