package Donnee;

import java.io.Serializable;

public class Vente implements Serializable {
    private int idFacture;
    private int idArticle;
    private int quantite;



    // Constructeur
    public Vente(int idf, int idA,int idF) {
        this.idFacture = idf;
        this.idArticle = idA;
        this.quantite = idF;


    }

    // Méthodes 'get' pour récupérer les valeurs des propriétés
    public int getId() {
        return idFacture;
    }

    public int getidArticle() {
        return idArticle;
    }

    public int getquantite() {
        return quantite;
    }




    // Méthodes 'set' pour mettre à jour les valeurs des propriétés

    public void setIdArticle(int ida) {
        this.idArticle = ida;
    }
    public void setIdFacture(int idf) {
        this.idFacture = idf;
    }

    public void setQuantite(int q) {
        this.quantite = q;
    }




}