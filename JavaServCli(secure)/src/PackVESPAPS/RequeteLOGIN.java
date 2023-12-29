package PackVESPAPS;


import Serveur.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Date;

public class RequeteLOGIN implements Requete {
    private String login;

    private  long temps ;
    private double alea;
    private byte[] digest;

    public RequeteLOGIN(String l, String p)
    {
        login = l;
        this.temps = new Date().getTime();
        this.alea = Math.random();
        Security.addProvider(new BouncyCastleProvider());


        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1","BC");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        md.update(login.getBytes());
        md.update(p.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeLong(temps);
            dos.writeDouble(alea);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        md.update(baos.toByteArray());
        digest = md.digest();
    }

    public String getLogin() {
        return login;
    }
    public boolean VerifyPassword(String p)
    {
        Security.addProvider(new BouncyCastleProvider());
        // Construction du digest local
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1","BC");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        md.update(login.getBytes());
        md.update(p.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeLong(temps);
            dos.writeDouble(alea);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        md.update(baos.toByteArray());
        byte[] digestLocal = md.digest();
        // Comparaison digest reçu et digest local
        return MessageDigest.isEqual(digest,digestLocal);
    }
}