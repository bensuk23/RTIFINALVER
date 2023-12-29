package PackVESPAPS;

import Serveur.Requete;

public class RequetePayFacture implements Requete {



    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    private byte[] data;

    public RequetePayFacture(byte[] datap) {

        data = datap;

    }



}
