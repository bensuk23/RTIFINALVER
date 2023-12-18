package PackVESPAP;

import Donnee.*;
import Serveur.*;

import java.util.ArrayList;
import java.util.List;

public class ReponseGetFacture implements Reponse
{


    public List<Article> getartList() {
        return artList;


    }



    private List<Article> artList;


    ReponseGetFacture(List<Article> artListp)
    {
        artList = artListp;
    }

}
