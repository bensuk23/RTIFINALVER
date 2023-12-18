package PackVESPAP;
import Serveur.*;
public class RequeteGetFactures implements Requete
{

    private int idcli;


    public RequeteGetFactures(int idclip) {
        idcli = idclip;

    }

    public int getidcli() {
        return idcli;
    }
}
