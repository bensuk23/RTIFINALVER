package PackVESPAPS;

import Donnee.*;
import Serveur.*;

import java.util.List;

public class ReponseGetFactures implements Reponse
{

    private byte[] data2; // message crypté symétriquement
    public void setData2(byte[] d) { data2 = d; }
    public byte[] getData2() { return data2; }




    ReponseGetFactures(byte[] d)
    {
        data2 = d;
    }

}
