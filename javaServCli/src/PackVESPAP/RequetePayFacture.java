package PackVESPAP;

import Serveur.Requete;

public class RequetePayFacture implements Requete {

    private int idFac;
    private String nomClient;
    private String numVisaClient;

    public RequetePayFacture(int idf,String nomC, String numVC) {
        idFac = idf;
        nomClient = nomC;
        numVisaClient = numVC;

    }

    public int getIdFac() {return idFac;}
    public String getnomClient() {
        return nomClient;
    }

    public String getnumVisaClient() {
        return numVisaClient;
    }

}
