package PackVESPAP;
import Serveur.*;
public class RequeteGetFacture implements Requete
{

    private int idf;


    public RequeteGetFacture(int idfp) {
        idf = idfp;

    }

    public int getidfac() {
        return idf;
    }
}
