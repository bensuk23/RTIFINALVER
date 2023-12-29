package PackVESPAPS;

import Serveur.*;
public class ReponseLOGIN implements Reponse
{
    private boolean valide;
    private int idcli;

    private byte[] data1; // clé de session cryptée asymétriquement
    public void setData1(byte[] d) { data1 = d; }
    public byte[] getData1() { return data1; }

    public int getIdcli()
    {
        return idcli;
    }

    public void setIdcli(int idcli)
    {
        this.idcli = idcli;
    }


    ReponseLOGIN(boolean v,int Id,byte[] data)
    {
        valide = v;
        idcli = Id;
        data1 = data;
    }
    ReponseLOGIN(boolean v,int Id)
    {
        valide = v;
        idcli = Id;

    }
    public boolean isValide() {
        return valide;
    }
}