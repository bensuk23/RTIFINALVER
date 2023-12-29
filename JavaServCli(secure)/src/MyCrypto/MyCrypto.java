package MyCrypto;

import java.security.*;
import javax.crypto.*;

public class MyCrypto
{
    public static byte[] CryptSymDES(SecretKey cle,byte[] data)
    {
        Cipher chiffrementE = null;
        try {

            chiffrementE = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");
            chiffrementE.init(Cipher.ENCRYPT_MODE, cle);
            return chiffrementE.doFinal(data);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

    }
    public static byte[] DecryptSymDES(SecretKey cle,byte[] data)
    {
        Cipher chiffrementD = null;
        try {
            chiffrementD = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");
            chiffrementD.init(Cipher.DECRYPT_MODE, cle);
            return chiffrementD.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

    }
    public static byte[] CryptAsymRSA(PublicKey cle,byte[] data)
    {
        Cipher chiffrementE = null;
        try {
            chiffrementE = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
            chiffrementE.init(Cipher.ENCRYPT_MODE, cle);
            return chiffrementE.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

    }
    public static byte[] DecryptAsymRSA(PrivateKey cle,byte[] data)
    {
        Cipher chiffrementD = null;
        try {
            chiffrementD = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
            chiffrementD.init(Cipher.DECRYPT_MODE, cle);
            return chiffrementD.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

    }

    public static void CreatinoCleSess()
    {


    }
}