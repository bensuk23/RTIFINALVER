package PackVESPAPS;


import MyCrypto.MyCrypto;
import Donnee.Article;
import Donnee.Facture;
import PackJDBC.*;
import PackJDBC.DatabaseBean;
import Serveur.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static MyCrypto.MyCrypto.*;

public class VESPAPS implements Protocole
{
    private String password;
    private DatabaseBean databaseBean;

    private BeanJDBC BeanJdbc;

    private static SecretKey cleSession;

    public VESPAPS()
    {
        try {
            // Create a single instance of DatabaseBean for the entire server
            Class leDriver = Class.forName("com.mysql.cj.jdbc.Driver");

            databaseBean = new DatabaseBean("jdbc:mysql://192.168.161.161:3306/PourStudent", "Student", "PassStudent1_");

            // Create instances of PaymentBean for each thread or use a connection pool

            // Example usage
            BeanJdbc = new BeanJDBC(databaseBean);



            // Close the database connection when the server is shutting down

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getNom()
    {
        return "VESPAPS";
    }
    @Override
    public synchronized Reponse TraiteRequete(Requete requete) throws FinConnexionException
    {

        if (requete instanceof RequeteLOGIN) return TraiteRequeteLOGIN((RequeteLOGIN) requete);
        if (requete instanceof RequeteLOGOUT) TraiteRequeteLOGOUT((RequeteLOGOUT) requete);
        if (requete instanceof RequeteGetFactures) return TraiteRequeteGetFactures((RequeteGetFactures) requete);
        if (requete instanceof RequetePayFacture) return TraiteRequetePayFacture((RequetePayFacture) requete);
        if (requete instanceof RequeteGetFacture) return TraiteRequeteGetFacture((RequeteGetFacture) requete);

        return null;
    }
    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete) throws FinConnexionException
    {
        int trv =0;
        int IDclient =0;
        String MDP= null;
        byte[] cleSessionCrypte = new byte[0];
        try
        {
            IDclient = BeanJdbc.performSelectCID(requete.getLogin());
            MDP = BeanJdbc.performPassFinder(requete.getLogin());
            if (requete.VerifyPassword(MDP))
            {
                System.out.println("Bienvenue " + requete.getLogin() + " !");

                cleSession = generateSessionKey();

                if (cleSession != null) {
                    System.out.println("Clé de session générée : " + cleSession);



                    cleSessionCrypte = MyCrypto.CryptAsymRSA(RecupereClePubliqueClient(),cleSession.getEncoded());
                    System.out.println("Cryptage asymétrique de la clé de session : " + new String(cleSessionCrypte));
                } else {
                    System.out.println("Erreur lors de la génération de la clé de session.");
                }

                return new ReponseLOGIN(true,IDclient,cleSessionCrypte);
            }
            else
            {
                System.out.println("Mauvais mot de passe pour " + requete.getLogin() + "...");
                return new ReponseLOGIN(false,IDclient);
            }

        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    private synchronized void TraiteRequeteLOGOUT(RequeteLOGOUT requete) throws FinConnexionException
    {

    }


    private synchronized ReponseGetFactures TraiteRequeteGetFactures(RequeteGetFactures requete) throws FinConnexionException
    {
        List <Facture> listFacBD;

        byte[] listFacBDCrypte = new byte[0];
        try
        {
            if (requete.VerifySignature(RecupereClePubliqueClient()))
            {
                System.out.println("Signature validée !");
                listFacBD = new ArrayList<>();
                listFacBD = BeanJdbc.performSelectFactureRetList(requete.getidcli());

                for (Facture f : listFacBD)
                {
                    System.out.println(f.toString());
                }
                listFacBDCrypte = CryptSymDES(cleSession,convertListToBytesF(listFacBD));
                return new ReponseGetFactures(listFacBDCrypte);
            }
            else
            {
                System.out.println("Signature invalide...");
            }

        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }


        return null;
    }

    private synchronized ReponseGetFacture TraiteRequeteGetFacture(RequeteGetFacture requete) throws FinConnexionException
    {
        List <Article> listartBD;

        byte[] listArtBDCrypte = new byte[0];


        try
        {
            if (requete.VerifySignature(RecupereClePubliqueClient()))
            {
                System.out.println("Signature validée !");
                listartBD = new ArrayList<>();
                listartBD = BeanJdbc.performSelectArticleRetList(requete.getidfac());

                for (Article a : listartBD) {
                    System.out.println(a.toString());
                }

                listArtBDCrypte = CryptSymDES(cleSession,convertListToBytesA(listartBD));
                return new ReponseGetFacture(listArtBDCrypte);
            }
            else
            {
                System.out.println("Signature invalide...");
            }

        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }


        return null;
    }
    private synchronized ReponsePayFacture TraiteRequetePayFacture(RequetePayFacture requete) throws FinConnexionException
    {
        boolean valid;
        int validint;
        int idfac;
        String nomcarte;
        String numcarte;
        Object[] fields = new Object[0];

        fields = convertFromBytes(MyCrypto.DecryptSymDES(cleSession,requete.getData()));

        // Afficher les champs extraits
        idfac = (int) fields[0];
        nomcarte = (String) fields[1];
        numcarte = (String) fields[2];



        valid = isValidVisa(numcarte);

        if (valid == true)
        {

            try {
                validint = BeanJdbc.performUpdateF(String.valueOf(idfac));
                if(validint == 1)
                {
                    System.out.println("Update reussit");
                }
                else if (validint == 0)
                {
                    System.out.println("Update ratée");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                return new ReponsePayFacture(true,cleSession);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (NoSuchProviderException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        else if (valid == false)
        {
            return new ReponsePayFacture(false);
        }
        throw new FinConnexionException(new ReponsePayFacture(false));

    }

    public static boolean isValidVisa(String visaNumber) {
        // Vérifie si la longueur du numéro de carte Visa est correcte (généralement 16 chiffres)
        if (visaNumber.length() != 16) {
            return false;
        }

        int sum = 0;
        boolean alternate = false;

        for (int i = visaNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(visaNumber.substring(i, i + 1));

            if (alternate) {
                n *= 2;

                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }

            sum += n;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }

    public static PrivateKey RecupereClePriveeServeur()
    {
        // Désérialisation de la clé privée du serveur

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("clePriveeServeur.ser"));
            PrivateKey cle = (PrivateKey) ois.readObject();
            ois.close();
            return cle;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public static PublicKey RecupereClePubliqueClient()
    {

        try {
            // Désérialisation de la clé publique
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("clePubliqueClient.ser"));
            PublicKey cle = (PublicKey) ois.readObject();
            ois.close();
            return cle;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static SecretKey generateSessionKey() {
        try {
            // Initialiser le KeyGenerator avec l'algorithme DES et utiliser Bouncy Castle ("BC")
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES", "BC");

            // Initialiser avec un objet SecureRandom
            keyGenerator.init(new SecureRandom());

            // Générer la clé de session
            SecretKey sessionKey = keyGenerator.generateKey();

            return sessionKey;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithme non supporté : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération de la clé : " + e.getMessage());
        }

        return null; // En cas d'erreur
    }

    private static byte[] convertListToBytesF(List<Facture> factures)
    {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {


            oos.writeObject(factures);


            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] convertListToBytesA(List<Article> articles)
    {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {


            oos.writeObject(articles);


            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object[] convertFromBytes(byte[] bytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream)) {

            // Lire les champs depuis le flux
            int intValue = dataInputStream.readInt();
            String stringValue1 = dataInputStream.readUTF();
            String stringValue2 = dataInputStream.readUTF();

            // Retourner les champs sous forme de tableau
            return new Object[]{intValue, stringValue1, stringValue2};
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
