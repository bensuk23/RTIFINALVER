package PackVESPAP;


import Serveur.*;
public class RequeteLOGIN implements Requete {
    private String login;
    private String password;
    private double alea;
    private byte[] digest;

    public RequeteLOGIN(String l, String p) {
        login = l;
        password = p;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}