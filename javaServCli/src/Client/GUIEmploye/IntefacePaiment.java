package Client.GUIEmploye;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;

import Client.Controller.ControllerPaiment;
import com.formdev.flatlaf.FlatDarculaLaf;


public class IntefacePaiment extends JFrame {
    private JPanel PanelPricipal;

    public JTextField getTextLogin() {
        return textLogin;
    }

    public void setTextLogin(JTextField textLogin) {
        this.textLogin = textLogin;
    }

    private JTextField textLogin;

    public JTextField getTextPassword() {
        return textPassword;
    }

    public void setTextPassword(JTextField textPassword) {
        this.textPassword = textPassword;
    }

    private JTextField textPassword;

    public JScrollPane getJscrollPaneFacture() {
        return JscrollPaneFacture;
    }

    public void setJscrollPaneFacture(JScrollPane jscrollPaneFacture) {
        JscrollPaneFacture = jscrollPaneFacture;
    }

    public JTable getTableFacture() {
        return tableFacture;
    }

    public void setTableFacture(JTable tableFacture) {
        this.tableFacture = tableFacture;
    }

    public JTable getTableArticles() {
        return tableArticles;
    }

    public void setTableArticles(JTable tableArticles) {
        this.tableArticles = tableArticles;
    }

    public JTextField getTextnumcarte() {
        return textnumcarte;
    }

    public void setTextnumcarte(JTextField textnumcarte) {
        this.textnumcarte = textnumcarte;
    }

    public JTextField getTextClinum() {
        return textClinum;
    }

    public void setTextClinum(JTextField textClinum) {
        this.textClinum = textClinum;
    }

    public JButton getSelectionButton() {
        return selectionButton;
    }

    public void setSelectionButton(JButton selectionButton) {
        this.selectionButton = selectionButton;
    }

    public JTextField getTextnomcarte() {
        return textnomcarte;
    }

    public void setTextnomcarte(JTextField textnomcarte) {
        this.textnomcarte = textnomcarte;
    }

    public JButton getPaiementButton() {
        return paiementButton;
    }

    public void setPaiementButton(JButton paiementButton) {
        this.paiementButton = paiementButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public void setLogoutButton(JButton logoutButton) {
        this.logoutButton = logoutButton;
    }

    private JScrollPane JscrollPaneFacture;

    public JButton getAfficherButton() {
        return afficherButton;
    }

    public void setAfficherButton(JButton afficherButton) {
        this.afficherButton = afficherButton;
    }

    private JButton afficherButton;
    private JScrollPane JscrollPaneAchat;
    private JTable tableFacture;
    private JTable tableArticles;

    private JTextField textnumcarte;
    private JTextField textClinum;
    private JButton selectionButton;
    private JTextField textnomcarte;
    private JButton paiementButton;

    public JButton getButtonLogin() {
        return buttonLogin;
    }

    public void setButtonLogin(JButton buttonLogin) {
        this.buttonLogin = buttonLogin;
    }

    private JButton buttonLogin;
    private JButton logoutButton;

    public JTextField getTextTotal() {
        return textTotal;
    }

    public void setTextTotal(JTextField textTotal) {
        this.textTotal = textTotal;
    }

    private JTextField textTotal;

    public IntefacePaiment()
    {
        // Définir les propriétés de la fenêtre
        setContentPane(PanelPricipal);
        tableFacture = new JTable();
        DefaultTableModel tableModel1 = (DefaultTableModel) tableFacture.getModel();



        String[] nomsColonnes1 = { "ID", "IdClient", "date", "montant", "paye"};
        tableModel1.setColumnIdentifiers(nomsColonnes1);
        JscrollPaneFacture.setViewportView(tableFacture);


        tableArticles = new JTable();
        DefaultTableModel tableModel2 = (DefaultTableModel) tableArticles.getModel();



        String[] nomsColonnes2 = { "Article", "Prix a l'unité", "Quantité"};
        tableModel2.setColumnIdentifiers(nomsColonnes2);
        JscrollPaneAchat.setViewportView(tableArticles);

    }



    public static void main(String[] args) throws IOException {
        FlatDarculaLaf.setup();

        IntefacePaiment frame = new IntefacePaiment();



        frame.setVisible(true);

        ControllerPaiment controllerPaiment = new ControllerPaiment((IntefacePaiment)frame);


    }



}
