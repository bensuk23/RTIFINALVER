package ControllerGuiClient;

import Client.ClientQt;
import Classe.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import java.io.*;
import java.net.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class ControllerClient {
    private static ClientQt fenetreClient;
    private static Panier panier ;
    private static int NbrArticles = 0;

    private static float totalCaddie;

    private static Article ArticleEnCours;
    public static Socket csocket;
    public ClientQt getFenetrePrincipale()
    {
        return fenetreClient;
    }



    public ControllerClient(ClientQt fenetreClientParam) throws IOException {
        this.fenetreClient = fenetreClientParam;




        // Fonction de création de la socket et connexion sur le serveur
        Tcp.Connexion();

        DataOutputStream dos = new DataOutputStream(csocket.getOutputStream());
        DataInputStream dis = new DataInputStream(csocket.getInputStream());

        LOGOUTOK();

        panier = new Panier();

        ArticleEnCours = new Article();





        fenetreClient.getTextTotalPayer().setText(String.format("%.2f", panier.calculerTotal()));



        fenetreClient.getSpinnerQuantite().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Si la valeur est inférieure à zéro, réinitialiser à zéro
                if ((int) fenetreClient.getSpinnerQuantite().getValue() < 0) {
                    fenetreClient.getSpinnerQuantite().setValue(0);
                }
            }

        });


        fenetreClient.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Fermeture de la socket

                System.out.println("CANCELALL.");
                String requete = "CANCELALL"+"+)";
                try
                {
                    dos.write(requete.getBytes());
                    dos.flush();
                } catch (IOException ex)
                {
                    throw  new RuntimeException(ex);
                }

                fenetreClient.dispose();
                // Fermeture de la socket
                try {
                    csocket.close();
                } catch (IOException ee) {
                    throw new RuntimeException(ee);
                }
            }
        });

        fenetreClient.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("LOGIN.");
                String LOGOUPASLOG;
                String receivedData = null;

                if (fenetreClient.getNouveauClientCheckBox().isSelected())
                {
                    LOGOUPASLOG = "NC";
                } else {
                    LOGOUPASLOG = "PNC";
                }
                String requete = "LOGIN#" + fenetreClient.getTextNom().getText()+"#"+ fenetreClient.getTextMDP().getText()+"#"+LOGOUPASLOG+"+)";
                System.out.println(requete);

                try {
                    dos.write(requete.getBytes());
                    dos.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


                byte[] buffer = new byte[1024];
                int bytesRead;

                try {
                    if (!((bytesRead = dis.read(buffer)) != -1)) ;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                // Convertir les bytes en une chaîne de caractères en utilisant le jeu de caractères UTF-8

                try {
                    receivedData = new String(buffer, 0, bytesRead, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("Données reçues : " + receivedData);
                // le // permet de joindre les deux symboles
                String[] mots = receivedData.split("[#+\\)]");

                // Parcours des mots

                if(mots[1].equals("ok"))
                {
                    LOGINOK();
                    try {
                        CONSULT(1);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(null, "Connexion réussie");
                }
                else if (mots[1].equals("ko"))
                {
                    JOptionPane.showMessageDialog(null, "Connexion Echouée "+ mots [2]);
                }





            }
        });

        fenetreClient.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String receivedData = null;
                String messagetemp = null;
                String message = null;
                System.out.println("LOGOUT.");
                String requete = "LOGOUT#" + "+)";
                try
                {
                    dos.write(requete.getBytes());
                    dos.flush();
                    System.out.println("test2");
                } catch (IOException ex)
                {
                    throw  new RuntimeException(ex);
                }
                byte[] buffer = new byte[1024];
                int bytesRead;
                try {
                    if (!((bytesRead = dis.read(buffer)) != -1)) ;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    receivedData = new String(buffer, 0, bytesRead, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("Données reçues : " + receivedData);
                String[] mots = receivedData.split("[#+\\)]");
                for (String mot : mots) {
                    System.out.println(mot);

                    messagetemp = messagetemp + mot ;
                }
                message = messagetemp.substring(4);

                LOGOUTOK();
                JOptionPane.showMessageDialog(null, message);

            }
        });



        fenetreClient.getButtonPrecedent().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Precedent");

                if((ArticleEnCours.getId()-1) != 0) {

                    try {
                        CONSULT(ArticleEnCours.getId()-1);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Vous etes revenu au debut" );
                }

            }
        });

        fenetreClient.getButtonSuivant().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Suivant");

                if((ArticleEnCours.getId()+ 1) != 22)
                {
                    try {
                        CONSULT(ArticleEnCours.getId()+1);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Vous etes à la fin" );
                }
            }
        });
        fenetreClient.getAcheterButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Acheter");
                int quantitesel = 0;
                int quantitedispo = 0;

                if(panier.getNbArticles() < 10)
                {
                    quantitesel = (int)fenetreClient.getSpinnerQuantite().getValue();
                    quantitedispo = Integer.parseInt(fenetreClient.getTextStock().getText());
                    if(quantitesel == 0)
                    {
                        JOptionPane.showMessageDialog(null, "Vous n'avez pas specifiez une quantité");
                    }
                    else if(quantitesel > quantitedispo)
                    {
                        JOptionPane.showMessageDialog(null, "Pas assez de stock ");
                    }
                    else
                    {

                        String receivedData = null;
                        String messagetemp = null;
                        String message = null;
                        System.out.println("ACHAT.");
                        String requete = "ACHAT#" +ArticleEnCours.getId() +"#"+ quantitesel +"+)";
                        try
                        {
                            dos.write(requete.getBytes());
                            dos.flush();
                        } catch (IOException ex)
                        {
                            throw  new RuntimeException(ex);
                        }
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        try {
                            if (!((bytesRead = dis.read(buffer)) != -1)) ;
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            receivedData = new String(buffer, 0, bytesRead, "UTF-8");
                        } catch (UnsupportedEncodingException ex) {
                            throw new RuntimeException(ex);
                        }
                        System.out.println("Données reçues : " + receivedData);
                        String[] mots = receivedData.split("[#+\\)]");



                        if( Integer.parseInt(mots[1]) == -1)
                        {
                            JOptionPane.showMessageDialog(null, "Erreur article non trouvé");
                        } else if (Integer.parseInt(mots[1]) != 0)
                        {

                            DefaultTableModel model = (DefaultTableModel) fenetreClient.getTableArticleaff().getModel();
                            int taille = fenetreClient.getTableArticleaff().getRowCount();

                            for (int i=0;i<taille;i++)
                            {
                                model.removeRow(0);
                                System.out.println("Remove pass = " );
                            }
                            Article atemp = new Article(ArticleEnCours.getId(), ArticleEnCours.getIntitule(), ArticleEnCours.getPrix(),ArticleEnCours.getStock(),null);

                            atemp.setStock(quantitesel);
                            int trouve =0;
                            for (Article a : panier.getPanier())
                            {
                                if(atemp.getId() == a.getId())
                                {
                                    trouve = 1;
                                    a.setStock(a.getStock()+atemp.getStock());
                                    break;
                                }

                            }
                            if(trouve==0)
                            {
                                panier.ajouterArticle(atemp);
                            }

                            for (Article a : panier.getPanier())
                            {
                                Vector ligne = new Vector();

                                ligne.add(a.getIntitule());
                                ligne.add(a.getPrix());
                                ligne.add(a.getStock());

                                model.addRow(ligne);
                            }
                            fenetreClient.getTextTotalPayer().setText(String.format("%.2f", panier.calculerTotal()));
                            try {
                                CONSULT(ArticleEnCours.getId());
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Pas assez de stock");
                        }
                    }


                }

            }
        });




        fenetreClient.getSupprimerArticleButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Suprimmer.");
                int i;
                i=fenetreClient.getTableArticleaff().getSelectedRow();
                try {
                    Supprimer(i);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                panier.retirerArticleindice(i);
                DefaultTableModel model = (DefaultTableModel) fenetreClient.getTableArticleaff().getModel();
                model.removeRow(i);
                try {
                    CONSULT(ArticleEnCours.getId());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                fenetreClient.getTextTotalPayer().setText(String.format("%.2f", panier.calculerTotal()));
            }
        });
        fenetreClient.getViderLePanierButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Vider.");

                DefaultTableModel model = (DefaultTableModel) fenetreClient.getTableArticleaff().getModel();
                int taille = fenetreClient.getTableArticleaff().getRowCount();

                for (int i=0;i<taille;i++)
                {
                    model.removeRow(0);
                    System.out.println("Remove pass = " );
                }
                panier.viderPanier();
                String requete = "CANCELALL" + "+)";

                try
                {
                    dos.flush();
                    dos.write(requete.getBytes());
                    dos.flush();
                    System.out.println("test4");
                } catch (IOException ex)
                {
                    throw  new RuntimeException(ex);
                }

                try {
                    CONSULT(ArticleEnCours.getId());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                fenetreClient.getTextTotalPayer().setText(String.format("%.2f", panier.calculerTotal()));
            }
        });

        fenetreClient.getConfirmerAchatButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String receivedData = null;
                System.out.println("Confirmer.");
                String requete = "CONFIRMER#" +fenetreClient.getTextNom().getText() +"#"+ fenetreClient.getTextMDP().getText() +"+)";
                try
                {
                    dos.write(requete.getBytes());
                    dos.flush();
                    System.out.println("test5");
                } catch (IOException ex)
                {
                    throw  new RuntimeException(ex);
                }

                byte[] buffer = new byte[1024];
                int bytesRead;
                try {
                    if (!((bytesRead = dis.read(buffer)) != -1)) ;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    receivedData = new String(buffer, 0, bytesRead, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    throw new RuntimeException(ex);
                }


                System.out.println("Données reçues : " + receivedData);
                String[] mots = receivedData.split("[#+\\)]");

                if(mots[1].equals("CONFIRMERALL"))
                {
                    DefaultTableModel model = (DefaultTableModel) fenetreClient.getTableArticleaff().getModel();
                    int taille = fenetreClient.getTableArticleaff().getRowCount();

                    for (int i=0;i<taille;i++)
                    {
                        model.removeRow(0);
                        System.out.println("Remove pass = " );
                    }
                    panier.viderPanier();

                    JOptionPane.showMessageDialog(null, "Pannier bien confirmer");
                    fenetreClient.getTextTotalPayer().setText(String.format("%.2f", panier.calculerTotal()));
                }

            }
        });



    }
    public static void LOGINOK() {


        fenetreClient.getTextNom().setEnabled(false);
        fenetreClient.getTextMDP().setEnabled(false);
        fenetreClient.getNouveauClientCheckBox().setEnabled(false);
        fenetreClient.getLoginButton().setEnabled(false);
        fenetreClient.getLogoutButton().setEnabled(true);
        fenetreClient.getButtonPrecedent().setEnabled(true);
        fenetreClient.getButtonSuivant().setEnabled(true);
        fenetreClient.getTextArticle().setEnabled(true);
        fenetreClient.getTextPrix().setEnabled(true);
        fenetreClient.getSupprimerArticleButton().setEnabled(true);
        fenetreClient.getViderLePanierButton().setEnabled(true);
        fenetreClient.getConfirmerAchatButton().setEnabled(true);
        fenetreClient.getTextTotalPayer().setEnabled(true);
        fenetreClient.getAcheterButton().setEnabled(true);
        fenetreClient.getSpinnerQuantite().setEnabled(true);
        fenetreClient.getTextStock().setEnabled(true);

    }

    public static void LOGOUTOK() {
        fenetreClient.getTextNom().setEnabled(true);
        fenetreClient.getTextMDP().setEnabled(true);
        fenetreClient.getNouveauClientCheckBox().setEnabled(true);
        fenetreClient.getLoginButton().setEnabled(true);
        fenetreClient.getLogoutButton().setEnabled(false);
        fenetreClient.getButtonPrecedent().setEnabled(false);
        fenetreClient.getButtonSuivant().setEnabled(false);
        fenetreClient.getTextArticle().setEnabled(false);
        fenetreClient.getTextPrix().setEnabled(false);
        fenetreClient.getSupprimerArticleButton().setEnabled(false);
        fenetreClient.getViderLePanierButton().setEnabled(false);
        fenetreClient.getConfirmerAchatButton().setEnabled(false);
        fenetreClient.getTextTotalPayer().setEnabled(false);
        fenetreClient.getAcheterButton().setEnabled(false);
        fenetreClient.getSpinnerQuantite().setEnabled(false);
        fenetreClient.getTextStock().setEnabled(false);

    }
    public static void CONSULT(int pos) throws IOException {
        String chemin = null;

        String receivedData = null;


        DataOutputStream dos = new DataOutputStream(csocket.getOutputStream());
        DataInputStream dis = new DataInputStream(csocket.getInputStream());


        System.out.println("CONSULT");
        String requete = "CONSULT#" + pos + "+)";

        try
        {
            dos.write(requete.getBytes());
            dos.flush();
            System.out.println("test2");
        } catch (IOException ex)
        {
            throw  new RuntimeException(ex);
        }

        byte[] buffer = new byte[1024];
        int bytesRead;

        try {
            if (!((bytesRead = dis.read(buffer)) != -1)) ;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        // Convertir les bytes en une chaîne de caractères en utilisant le jeu de caractères UTF-8

        try {
            receivedData = new String(buffer, 0, bytesRead, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("Données reçues : " + receivedData);
        // le // permet de joindre les deux symboles
        String[] mots = receivedData.split("[#+\\)]");

        // Parcours des mots
        ArticleEnCours.setId(Integer.parseInt(mots[1]));
        ArticleEnCours.setIntitule(mots[2]);
        ArticleEnCours.setPrix(Float.parseFloat(mots[4]));
        ArticleEnCours.setStock(Integer.parseInt(mots[3]));


        //System.out.println(mots [0] +mots [1]+mots [2]);
        fenetreClient.getTextArticle().setText(mots[2]);
        fenetreClient.getTextPrix().setText(String.format("%.2f", ArticleEnCours.getPrix()));
        fenetreClient.getTextStock().setText(mots[3]);

        chemin = "C:/Users/delfo/OneDrive/Documents/ecole/b3/RTI/labo/ClientJava/GUIClientJava/src/Client/images/"+ mots[5];

        ArticleEnCours.setImage(chemin);


        ImageIcon icone = new ImageIcon(chemin);

        fenetreClient.labelImage.setIcon(icone);

    }

    public void Supprimer(int indice) throws IOException
    {
        String chemin = null;

        String receivedData = null;



        DataOutputStream dos = new DataOutputStream(csocket.getOutputStream());
        DataInputStream dis = new DataInputStream(csocket.getInputStream());

        System.out.println("Supprimer");
        String requete = "CANCEL#" + indice +"+)";

        try
        {
            dos.write(requete.getBytes());
            dos.flush();
            System.out.println("test supprimer");
        } catch (IOException ex)
        {
            throw  new RuntimeException(ex);
        }

        byte[] buffer = new byte[1024];
        int bytesRead;
        try {
            if (!((bytesRead = dis.read(buffer)) != -1)) ;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            receivedData = new String(buffer, 0, bytesRead, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("Données reçues : " + receivedData);
        String[] mots = receivedData.split("[#+\\)]");



    }



}
