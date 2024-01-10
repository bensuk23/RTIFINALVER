package Client.Controller;

import Donnee.Article;

import MyCrypto.MyCrypto;
import Donnee.Facture;
import Client.GUIEmploye.IntefacePaiment;
import PackVESPAPS.*;
import Serveur.Requete;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static MyCrypto.MyCrypto.CryptSymDES;
import static MyCrypto.MyCrypto.DecryptSymDES;

public class ControllerPaiment {
    private static IntefacePaiment fenetreClient;

    private static SecretKey cleSession;

    private static int idcli;

    public static Socket csocket;
    private String login;
    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;

    public ControllerPaiment(IntefacePaiment frame) throws IOException {
        this.fenetreClient = frame;

        fenetreClient.setTitle("Mon Interface Graphique");
        fenetreClient.setSize(1000, 500);
        connexionserv();
        LOGOUTOK();
        oos = new ObjectOutputStream(csocket.getOutputStream());
        ois = new ObjectInputStream(csocket.getInputStream());

        fenetreClient.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {


                fenetreClient.dispose();
                // Fermeture de la socket
                try {
                    csocket.close();
                } catch (IOException ee) {
                    throw new RuntimeException(ee);
                }
            }
        });


        fenetreClient.getButtonLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("LOGIN.");

                if (fenetreClient.getTextLogin().getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veuillez entrée un LOGIN ");
                    return;
                }
                if (fenetreClient.getTextPassword().getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veuillez entrée un Mot De Passe ");
                    return;
                }


                String login = fenetreClient.getTextLogin().getText();
                String password = fenetreClient.getTextPassword().getText();


                try {

                    RequeteLOGIN requete = new RequeteLOGIN(login, password);

                    oos.writeObject(requete);
                    ReponseLOGIN reponse = (ReponseLOGIN) ois.readObject();
                    idcli = reponse.getIdcli();

                    if (reponse.isValide()) {
                        JOptionPane.showMessageDialog(null, " Client Connecter ");
                        byte[] cleSessionDecryptee;
                        cleSessionDecryptee = MyCrypto.DecryptAsymRSA(RecupereClePriveeClientKS(),reponse.getData1());
                        cleSession = new SecretKeySpec(cleSessionDecryptee,"DES");
                        System.out.println("reception + decryptage asymétrique de la clé de session...");

                        GetFacturesFast(idcli,RecupereClePriveeClientKS());

                        LOGINOK();
                    } else {
                        JOptionPane.showMessageDialog(null, " Probleme de connexion  ");
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Problème de connexion !", "Erreur...", JOptionPane.ERROR_MESSAGE);
                } catch (SignatureException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                } catch (InvalidKeyException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchProviderException ex) {
                    throw new RuntimeException(ex);
                } catch (UnrecoverableKeyException ex) {
                    throw new RuntimeException(ex);
                } catch (CertificateException ex) {
                    throw new RuntimeException(ex);
                } catch (KeyStoreException ex) {
                    throw new RuntimeException(ex);
                }

            }



        });

        fenetreClient.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("LOGOUT.");
                JOptionPane.showMessageDialog(null, " Déconnexion ...  ");
                LOGOUTOK();
            }

        });


        fenetreClient.getPaiementButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Paiement.");
                int idf = 0;
                boolean test = false;

                if (fenetreClient.getTextnomcarte().getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veuillez entrée un nom ");
                    return;
                }

                int ligneSelectionnee = fenetreClient.getTableFacture().getSelectedRow();
                if (ligneSelectionnee != -1) {
                    DefaultTableModel model = (DefaultTableModel) fenetreClient.getTableFacture().getModel();
                    // Récupérer les valeurs des colonnes de la ligne sélectionnée
                    idf = Integer.parseInt(String.valueOf(model.getValueAt(ligneSelectionnee, 0)));
                    test = Boolean.parseBoolean(String.valueOf(model.getValueAt(ligneSelectionnee, 4)));
                    System.out.println("Colonne 1 : " + idf);

                    if (test == true) {
                        JOptionPane.showMessageDialog(null, "Facture Déja payé ");
                        return;
                    }


                }

                String nomC = fenetreClient.getTextnomcarte().getText();
                String numcarte = fenetreClient.getTextnumcarte().getText();

                if (numcarte.length() < 16 || numcarte.length() > 22) {
                    JOptionPane.showMessageDialog(null, " Code visa ne contient pas assez ou trop de caractère (entre 16 et 22) ");
                    return;
                }
                try {
                    byte[] bytes = CryptSymDES(cleSession,convertToBytes(idf,nomC,numcarte));





                    RequetePayFacture requete = new RequetePayFacture(bytes);

                    oos.writeObject(requete);
                    ReponsePayFacture reponse = (ReponsePayFacture) ois.readObject();
                    if (reponse.isValide()) {
                        if (reponse.VerifyAuthenticity(cleSession))
                        {
                            System.out.println("Authentification validée !");
                            JOptionPane.showMessageDialog(null, " Facture Payé et table facture mise a jour  ");
                            GetFacturesFast(idcli,RecupereClePriveeClientKS());
                        }
                        else System.out.println("Authentification échouée...");
                    } else {
                        JOptionPane.showMessageDialog(null, " Probleme de carte visa ");
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Problème de connexion !", "Erreur...", JOptionPane.ERROR_MESSAGE);
                } catch (SignatureException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                } catch (InvalidKeyException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchProviderException ex) {
                    throw new RuntimeException(ex);

                } catch (UnrecoverableKeyException ex) {
                    throw new RuntimeException(ex);
                } catch (CertificateException ex) {
                    throw new RuntimeException(ex);
                } catch (KeyStoreException ex) {
                    throw new RuntimeException(ex);
                }
            }


        });


        fenetreClient.getAfficherButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Afficher.");
                int idf = 0;

                byte[] listArtBDCrypte = new byte[0];
                List<Article> listarticles = new ArrayList<>();
                int ligneSelectionnee = fenetreClient.getTableFacture().getSelectedRow();
                if (ligneSelectionnee != -1) {
                    DefaultTableModel model = (DefaultTableModel) fenetreClient.getTableFacture().getModel();
                    // Récupérer les valeurs des colonnes de la ligne sélectionnée
                    idf = Integer.parseInt(String.valueOf(model.getValueAt(ligneSelectionnee, 0)));
                    System.out.println("Colonne 1 : " + idf);


                }


                try {

                    RequeteGetFacture requete = new RequeteGetFacture(idf,RecupereClePriveeClientKS());

                    oos.writeObject(requete);

                    ReponseGetFacture reponse = (ReponseGetFacture) ois.readObject();

                    DefaultTableModel model = (DefaultTableModel) fenetreClient.getTableArticles().getModel();
                    int taille = fenetreClient.getTableArticles().getRowCount();

                    for (int i = 0; i < taille; i++) {
                        model.removeRow(0);
                    }


                    float stock = 0.0f;
                    listArtBDCrypte = reponse.getData2();
                    listarticles = convertBytesToListA(DecryptSymDES(cleSession,listArtBDCrypte));



                    for (Article a : listarticles) {
                        Vector ligne = new Vector();

                        ligne.add(a.getNom());
                        ligne.add(a.getPrix());
                        ligne.add(a.getQuantite());
                        stock = stock + a.calculerTotal();

                        model.addRow(ligne);
                    }
                    fenetreClient.getTextTotal().setText(String.valueOf(stock));


                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (SignatureException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                } catch (InvalidKeyException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchProviderException ex) {
                    throw new RuntimeException(ex);
                } catch (UnrecoverableKeyException ex) {
                    throw new RuntimeException(ex);
                } catch (CertificateException ex) {
                    throw new RuntimeException(ex);
                } catch (KeyStoreException ex) {
                    throw new RuntimeException(ex);
                }

            }

        });
    }

    public static void connexionserv() {
        try {
            csocket = new Socket("127.0.0.1", 50002);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Connexion établie.");
        System.out.println("--- Socket ---");
        System.out.println("Adresse IP locale : " + csocket.getLocalAddress().getHostAddress());
        System.out.println("Port local : " + csocket.getLocalPort());
        System.out.println("Adresse IP distante : " + csocket.getInetAddress().getHostAddress());
        System.out.println("Port distant : " + csocket.getPort());
    }

    public static void LOGINOK() {


        fenetreClient.getTextLogin().setEnabled(false);
        fenetreClient.getTextPassword().setEnabled(false);
        fenetreClient.getButtonLogin().setEnabled(false);
        fenetreClient.getLogoutButton().setEnabled(true);
        fenetreClient.getTableArticles().setEnabled(true);
        fenetreClient.getTableFacture().setEnabled(true);
        fenetreClient.getTextnomcarte().setEnabled(true);
        fenetreClient.getTextnumcarte().setEnabled(true);
        fenetreClient.getPaiementButton().setEnabled(true);
        fenetreClient.getAfficherButton().setEnabled(true);
        fenetreClient.getTableArticles().setEnabled(true);
        fenetreClient.getTableFacture().setEnabled(true);
        fenetreClient.getTextTotal().setEnabled(true);

    }
    public static void LOGOUTOK() {
        fenetreClient.getTextLogin().setEnabled(true);
        fenetreClient.getTextPassword().setEnabled(true);
        fenetreClient.getButtonLogin().setEnabled(true);
        fenetreClient.getLogoutButton().setEnabled(false);
        fenetreClient.getTableArticles().setEnabled(false);
        fenetreClient.getTableFacture().setEnabled(false);
        fenetreClient.getTextnomcarte().setEnabled(false);
        fenetreClient.getTextnumcarte().setEnabled(false);
        fenetreClient.getPaiementButton().setEnabled(false);
        fenetreClient.getAfficherButton().setEnabled(false);
        fenetreClient.getTableArticles().setEnabled(false);
        fenetreClient.getTableFacture().setEnabled(false);
        fenetreClient.getTextTotal().setEnabled(false);

    }

    public void GetFacturesFast(int clientID,PrivateKey clePriveeClientp) throws SignatureException, IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        System.out.println("GetFactures");
        byte[] listFacBDCrypte = new byte[0];
        List<Facture> listarticles = new ArrayList<>();

        RequeteGetFactures requete = new RequeteGetFactures(clientID,clePriveeClientp);

        try {
            oos.writeObject(requete);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        try {
            ReponseGetFactures reponse = (ReponseGetFactures) ois.readObject();

            if (reponse.getData2()== null) {
                JOptionPane.showMessageDialog(null, "Pas de facture pour ce client");
                return;
            }

            // Clear table for factures
            clearTableModel(fenetreClient.getTableFacture().getModel());

            // Clear table for articles
            clearTableModel(fenetreClient.getTableArticles().getModel());

            DefaultTableModel model = (DefaultTableModel) fenetreClient.getTableFacture().getModel();
            DefaultTableModel model1 = (DefaultTableModel) fenetreClient.getTableArticles().getModel();
            listFacBDCrypte = reponse.getData2();
            listarticles = convertBytesToListF(DecryptSymDES(cleSession,listFacBDCrypte));


            for (Facture f : listarticles) {
                Vector ligne = new Vector();

                ligne.add(f.getId());
                ligne.add(f.getIdClient());
                ligne.add(f.getDate());
                ligne.add(f.getMontant());
                ligne.add(f.isPaye());

                model.addRow(ligne);
            }

        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Function to clear the rows in a DefaultTableModel
    private void clearTableModel(TableModel model) {
        int rowCount = model.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            ((DefaultTableModel) model).removeRow(0);
            System.out.println("Remove pass = ");
        }
    }
    public static PublicKey RecupereClePubliqueServeur()
    {

        try {
            // Désérialisation de la clé publique
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("clePubliqueServeur.ser"));
            PublicKey cle = (PublicKey) ois.readObject();
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

    public static PrivateKey RecupereClePriveeClient()
    {
        // Désérialisation de la clé privée du serveur

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("clePriveeClient.ser"));
            PrivateKey cle = (PrivateKey) ois.readObject();
            ois.close();
            return cle;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static PrivateKey RecupereClePriveeClientKS() throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {
        // Récupération de la clé privée de Jean-Marc dans le keystore de Jean-Marc
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("JavaServCli(secure)\\KeystoreClient.jks"),"climdp".toCharArray());
        PrivateKey cle = (PrivateKey) ks.getKey("clientalias","climdp".toCharArray());
        return cle;
    }
    private static List<Facture> convertBytesToListF(byte[] bytes)
    {
        try {

            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);

            System.out.println("Début de la désérialisation");
            List<Facture> listeDeserialisee = (List<Facture>) ois.readObject();
            System.out.println("Fin de la désérialisation");


            return listeDeserialisee;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Article> convertBytesToListA(byte[] bytes)
    {
        try {

            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);

            System.out.println("Début de la désérialisation");
            List<Article> listeDeserialisee = (List<Article>) ois.readObject();
            System.out.println("Fin de la désérialisation");


            return listeDeserialisee;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] convertToBytes(int intValue, String stringValue1, String stringValue2) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {

            // Write the fields to the stream
            dataOutputStream.writeInt(intValue);
            dataOutputStream.writeUTF(stringValue1);
            dataOutputStream.writeUTF(stringValue2);

            // Retrieve the byte array
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




}
