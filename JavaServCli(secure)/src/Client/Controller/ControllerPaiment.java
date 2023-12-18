package Client.Controller;

import Donnee.Article;
import Donnee.Facture;
import Client.GUIEmploye.IntefacePaiment;
import PackVESPAP.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Vector;

public class ControllerPaiment {
    private static IntefacePaiment fenetreClient;

    public static Socket csocket;
    private String login;
    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;
    public ControllerPaiment(IntefacePaiment frame) throws IOException
    {
        this.fenetreClient = frame;

        fenetreClient.setTitle("Mon Interface Graphique");
        fenetreClient.setSize(1000,500);
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

                if (fenetreClient.getTextLogin().getText().isEmpty() )
                {
                    JOptionPane.showMessageDialog(null, "Veuillez entrée un LOGIN ");
                    return;
                }
                if (fenetreClient.getTextPassword().getText().isEmpty() )
                {
                    JOptionPane.showMessageDialog(null, "Veuillez entrée un Mot De Passe ");
                    return;
                }



                String login = fenetreClient.getTextLogin().getText();
                String password = fenetreClient.getTextPassword().getText();


                try
                {

                    RequeteLOGIN requete = new RequeteLOGIN(login,password);

                    oos.writeObject(requete);
                    ReponseLOGIN reponse = (ReponseLOGIN) ois.readObject();
                    if (reponse.isValide())
                    {
                        JOptionPane.showMessageDialog(null, " Client Connecter ");
                        LOGINOK();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, " Probleme de connexion  ");
                    }
                }
                catch (IOException | ClassNotFoundException ex)
                {
                    JOptionPane.showMessageDialog(null,"Problème de connexion !","Erreur...",JOptionPane.ERROR_MESSAGE);
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

        fenetreClient.getSelectionButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (fenetreClient.getTextClinum().getText().isEmpty() )
                {
                    JOptionPane.showMessageDialog(null, "Veuillez entrée un Numero de Client ");
                    return;
                }

                int idcli = Integer.parseInt(fenetreClient.getTextClinum().getText());


                RequeteGetFactures requete = new RequeteGetFactures(idcli);

                try {
                    oos.writeObject(requete);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    ReponseGetFactures reponse = (ReponseGetFactures) ois.readObject();

                    if (reponse.getFacList().isEmpty())
                    {
                        JOptionPane.showMessageDialog(null, "Pas de facture pour ce client");
                        return;
                    }

                    DefaultTableModel model = (DefaultTableModel) fenetreClient.getTableFacture().getModel();
                    int taille = fenetreClient.getTableFacture().getRowCount();

                    for (int i=0;i<taille;i++)
                    {
                        model.removeRow(0);
                        System.out.println("Remove pass = " );
                    }

                    DefaultTableModel model1 = (DefaultTableModel) fenetreClient.getTableArticles().getModel();
                    int taille2 = fenetreClient.getTableArticles().getRowCount();

                    for (int i=0;i<taille2;i++)
                    {
                        model1.removeRow(0);
                        System.out.println("Remove pass = " );
                    }








                    for (Facture f : reponse.getFacList())
                    {


                        Vector ligne = new Vector();

                        ligne.add(f.getId());
                        ligne.add(f.getIdClient());
                        ligne.add(f.getDate());
                        ligne.add(f.getMontant());
                        ligne.add(f.isPaye());

                        model.addRow(ligne);
                    }

                } catch (IOException ex ) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });

        fenetreClient.getPaiementButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Paiement.");
                int idf = 0;
                boolean test = false;

                if (fenetreClient.getTextnomcarte().getText().isEmpty() )
                {
                    JOptionPane.showMessageDialog(null, "Veuillez entrée un nom ");
                    return;
                }

                int ligneSelectionnee = fenetreClient.getTableFacture().getSelectedRow();
                if (ligneSelectionnee != -1)
                {
                    DefaultTableModel model = (DefaultTableModel) fenetreClient.getTableFacture().getModel();
                    // Récupérer les valeurs des colonnes de la ligne sélectionnée
                    idf = Integer.parseInt(String.valueOf(model.getValueAt(ligneSelectionnee, 0)));
                    test = Boolean.parseBoolean(String.valueOf(model.getValueAt(ligneSelectionnee, 4)));
                    System.out.println("Colonne 1 : " + idf);

                    if (test == true)
                    {
                        JOptionPane.showMessageDialog(null, "Facture Déja payé ");
                        return;
                    }


                }

                String nomC = fenetreClient.getTextnomcarte().getText();
                String numcarte = fenetreClient.getTextnumcarte().getText();

                if (numcarte.length() < 16 || numcarte.length() > 22)
                {
                    JOptionPane.showMessageDialog(null, " Code visa ne contient pas assez ou trop de caractère (entre 16 et 22) ");
                    return;
                }




                try
                {

                    RequetePayFacture requete = new RequetePayFacture(idf,nomC,numcarte);

                    oos.writeObject(requete);
                    ReponsePayFacture reponse = (ReponsePayFacture) ois.readObject();
                    if (reponse.isValide())
                    {
                        JOptionPane.showMessageDialog(null, " Facture Payé et table facture mise a jour  ");
                        fenetreClient.getSelectionButton().doClick();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, " Probleme de carte visa ");
                    }
                }
                catch (IOException | ClassNotFoundException ex)
                {
                    JOptionPane.showMessageDialog(null,"Problème de connexion !","Erreur...",JOptionPane.ERROR_MESSAGE);
                }
            }





        });


        fenetreClient.getAfficherButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Afficher.");
                int idf = 0;
                int ligneSelectionnee = fenetreClient.getTableFacture().getSelectedRow();
                if (ligneSelectionnee != -1)
                {
                    DefaultTableModel model = (DefaultTableModel) fenetreClient.getTableFacture().getModel();
                    // Récupérer les valeurs des colonnes de la ligne sélectionnée
                    idf = Integer.parseInt(String.valueOf(model.getValueAt(ligneSelectionnee, 0)));
                    System.out.println("Colonne 1 : " + idf);


                }



                try
                {

                    RequeteGetFacture requete = new RequeteGetFacture(idf);

                    oos.writeObject(requete);

                    ReponseGetFacture reponse = (ReponseGetFacture) ois.readObject();

                    DefaultTableModel model = (DefaultTableModel) fenetreClient.getTableArticles().getModel();
                    int taille = fenetreClient.getTableArticles().getRowCount();

                    for (int i=0;i<taille;i++)
                    {
                        model.removeRow(0);
                        System.out.println("Remove pass = " );
                    }





                    float stock = 0.0f;


                    for (Article a : reponse.getartList())
                    {
                        Vector ligne = new Vector();

                        ligne.add(a.getNom());
                        ligne.add(a.getPrix());
                        ligne.add(a.getQuantite());
                        stock = stock + a.calculerTotal();

                        model.addRow(ligne);
                    }
                    fenetreClient.getTextTotal().setText(String.valueOf(stock));


                } catch (IOException ex ) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

            }

        });
    }

    public static void connexionserv() {
        try {
            csocket = new Socket("127.0.0.1", 50001);
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
        fenetreClient.getTextClinum().setEnabled(true);
        fenetreClient.getTextnumcarte().setEnabled(true);
        fenetreClient.getPaiementButton().setEnabled(true);
        fenetreClient.getSelectionButton().setEnabled(true);
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
        fenetreClient.getTextClinum().setEnabled(false);
        fenetreClient.getTextnumcarte().setEnabled(false);
        fenetreClient.getPaiementButton().setEnabled(false);
        fenetreClient.getSelectionButton().setEnabled(false);
        fenetreClient.getAfficherButton().setEnabled(false);
        fenetreClient.getTableArticles().setEnabled(false);
        fenetreClient.getTableFacture().setEnabled(false);
        fenetreClient.getTextTotal().setEnabled(false);


    }

}

