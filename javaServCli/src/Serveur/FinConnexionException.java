package Serveur;

public class FinConnexionException  extends Exception
{
    private Reponse reponse;
    public FinConnexionException (Reponse reponse)
    {
        super("Fin de connexion decide par le protocole");
        this.reponse = reponse;
    }
    public Reponse getReponse()
    {
        return  reponse;
    }
}
