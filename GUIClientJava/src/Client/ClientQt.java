package Client;

import javax.swing.*;

import java.io.*;
import java.net.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.formdev.flatlaf.FlatDarculaLaf;


import ControllerGuiClient.*;
public class ClientQt extends JFrame
{
    private JTextField textNom;
    private JTextField textMDP;
    private JCheckBox nouveauClientCheckBox;
    private JButton loginButton;
    private JButton logoutButton;
    private JPanel panelPrincipale;

    private JButton buttonSuivant;
    private JButton buttonPrecedent;
    private JTextField textArticle;
    private JTextField textPrix;


    private JTable tableArticleaff;
    private JButton supprimerArticleButton;
    private JButton viderLePanierButton;
    private JButton confirmerAchatButton;
    private JTextField textTotalPayer;
    private JButton acheterButton;
    private JSpinner spinnerQuantite;
    private JTextField textStock;
    public JLabel labelImage;
    private JScrollPane jScrollPaneArticles;
    private JPanel PanelArticle;



    public JTextField getTextNom() {return textNom;}

    public void setTextNom(JTextField textNom) {this.textNom = textNom;}

    public JTextField getTextMDP() {
        return textMDP;
    }

    public void setTextMDP(JTextField textMDP) {
        this.textMDP = textMDP;
    }

    public JCheckBox getNouveauClientCheckBox() {
        return nouveauClientCheckBox;
    }

    public void setNouveauClientCheckBox(JCheckBox nouveauClientCheckBox) {this.nouveauClientCheckBox = nouveauClientCheckBox;}

    public JButton getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(JButton loginButton) {
        this.loginButton = loginButton;
    }

    public JButton getLogoutButton() {return logoutButton;}

    public void setLogoutButton(JButton logoutButton) {
        this.logoutButton = logoutButton;
    }

    public JButton getButtonSuivant() {return buttonSuivant;}

    public void setButtonSuivant(JButton buttonSuivant) {this.buttonSuivant = buttonSuivant;}

    public JButton getButtonPrecedent() {return buttonPrecedent;}

    public void setButtonPrecedent(JButton buttonPrecedent) {this.buttonPrecedent = buttonPrecedent;}

    public JTextField getTextArticle() {return textArticle;}

    public void setTextArticle(JTextField textArticle) {this.textArticle = textArticle;}

    public JTextField getTextPrix() {return textPrix;}

    public void setTextPrix(JTextField textPrix) {this.textPrix = textPrix;}

    public JButton getSupprimerArticleButton() {return supprimerArticleButton;}

    public void setSupprimerArticleButton(JButton supprimerArticleButton) {this.supprimerArticleButton = supprimerArticleButton;}

    public JButton getViderLePanierButton() {return viderLePanierButton;}

    public void setViderLePanierButton(JButton viderLePanierButton) {this.viderLePanierButton = viderLePanierButton;}

    public JButton getConfirmerAchatButton() {return confirmerAchatButton;}

    public void setConfirmerAchatButton(JButton confirmerAchatButton) {this.confirmerAchatButton = confirmerAchatButton;}

    public JTextField getTextTotalPayer() {return textTotalPayer;}

    public void setTextTotalPayer(JTextField textTotalPayer) {this.textTotalPayer = textTotalPayer;}

    public JButton getAcheterButton() {return acheterButton;}

    public void setAcheterButton(JButton acheterButton) {this.acheterButton = acheterButton;}

    public JSpinner getSpinnerQuantite() {return spinnerQuantite;}

    public void setSpinnerQuantite(JSpinner spinnerQuantite) {this.spinnerQuantite = spinnerQuantite;}

    public JTextField getTextStock() {return textStock;}

    public void setTextStock(JTextField textStock) {this.textStock = textStock;}



    public void setTableArticleaff(JTable tableArticleaff) {
        this.tableArticleaff = tableArticleaff;
    }

    // Getter pour tableArticleaff
    public JTable getTableArticleaff() {
        return tableArticleaff;
    }


    public ClientQt()
    {

        setResizable(false);
        setSize(180, 620);
        setMinimumSize(new Dimension(1280, 720));




        setTitle("Maraicher en ligne");
        setContentPane(panelPrincipale);

        pack();
        //setSize(800,800);



        tableArticleaff = new JTable();
        DefaultTableModel tableModel1 = (DefaultTableModel) tableArticleaff.getModel();



        String[] nomsColonnes1 = { "Article", "Prix a l'unité", "Quantité"};
        tableModel1.setColumnIdentifiers(nomsColonnes1);
        jScrollPaneArticles.setViewportView(tableArticleaff);

    }




        public static void main(String[] args) throws IOException {
            FlatDarculaLaf.setup();

            ClientQt frame = new ClientQt();



            frame.setVisible(true);
            ControllerClient controller = new ControllerClient((ClientQt)frame);

        }



}
