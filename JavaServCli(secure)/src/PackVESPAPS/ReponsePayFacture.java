package PackVESPAPS;

import Serveur.Reponse;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class ReponsePayFacture implements Reponse {
    private boolean valide;

    private byte[] hmac; // hmac envoyé
    ReponsePayFacture(boolean v, SecretKey cleSession) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException {
        valide = v;
        Mac hm = Mac.getInstance("HMAC-MD5","BC"); // ligne importante
        hm.init(cleSession);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeBoolean(valide);
        hm.update(baos.toByteArray());
        hmac = hm.doFinal();


    }

    ReponsePayFacture(boolean v) {
        valide = v;

    }

    public boolean VerifyAuthenticity(SecretKey cleSession) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, IOException
    {
        // Construction du HMAC local
        Mac hm = Mac.getInstance("HMAC-MD5","BC");    // ligne importante
        hm.init(cleSession);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeBoolean(valide);
        hm.update(baos.toByteArray());
        byte[] hmacLocal = hm.doFinal();
        // Comparaison HMAC reçu et HMAC local
        return MessageDigest.isEqual(hmac,hmacLocal);
    }
    public boolean isValide() {
        return valide;
    }


    // Construction du HMAC

}
