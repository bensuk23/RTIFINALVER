package PackVESPAPS;
import Serveur.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;


public class RequeteGetFactures implements Requete
{

    private int idcli;
    private byte[] signature;



    public RequeteGetFactures(int idclip, PrivateKey clePriveeClient) throws InvalidKeyException, SignatureException, IOException, NoSuchAlgorithmException, NoSuchProviderException {
        idcli = idclip;

        // Construction de la signature
        Signature s = Signature.getInstance("SHA1withRSA","BC");
        s.initSign(clePriveeClient);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(String.valueOf(idcli));
        s.update(baos.toByteArray());
        signature = s.sign();

    }

    public boolean VerifySignature(PublicKey clePubliqueClient) throws InvalidKeyException, SignatureException, IOException, NoSuchAlgorithmException, NoSuchProviderException {
        // Construction de l'objet Signature
        Signature s = Signature.getInstance("SHA1withRSA","BC");
        s.initVerify(clePubliqueClient);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(String.valueOf(idcli));
        s.update(baos.toByteArray());
        // Vérification de la signature reçue
        return s.verify(signature);
    }

    public int getidcli() {
        return idcli;
    }
}
