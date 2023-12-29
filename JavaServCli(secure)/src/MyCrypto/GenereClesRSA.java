package MyCrypto;

import java.io.*;
import java.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class GenereClesRSA
{
    public static void main(String args[])
    {
        try {
            Security.addProvider(new BouncyCastleProvider());// Génération des clés
            KeyPairGenerator genCles = KeyPairGenerator.getInstance("RSA","BC");
            genCles.initialize(512,new SecureRandom()); // 512 par exemple
            KeyPair deuxCles = genCles.generateKeyPair();
            PublicKey clePublique = deuxCles.getPublic();
            PrivateKey clePrivee = deuxCles.getPrivate();
            System.out.println(" *** Cle publique generee = " + clePublique);
            System.out.println(" *** Cle privee generee = " + clePrivee);
            // Sérialisation des clés dans des fichiers différents
            ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream("clePubliqueServeur.ser"));
            oos1.writeObject(clePublique);
            oos1.close();
            System.out.println("Sérialisation de la clé publique dans le fichier clePubliqueClient.ser");
            ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream("clePriveeServeur.ser"));
            oos2.writeObject(clePrivee);
            oos2.close();
            System.out.println("Sérialisation de la clé privée dans le fichier clePriveeClient.ser");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }

    }
}
