package PackVESPAP;

import Donnee.*;
import Serveur.*;

import java.util.ArrayList;
import java.util.List;

public class ReponseGetFactures implements Reponse
{


    public List<Facture> getFacList() {
        return FacList;


    }



    private List<Facture> FacList;


    ReponseGetFactures(List<Facture> FacListp)
    {
        FacList = FacListp;
    }

}
