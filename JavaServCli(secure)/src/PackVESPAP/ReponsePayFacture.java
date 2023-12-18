package PackVESPAP;

import Serveur.Reponse;

public class ReponsePayFacture implements Reponse {
    private boolean valide;
    ReponsePayFacture(boolean v) {
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }
}
