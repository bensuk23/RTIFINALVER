package Donnee;

import java.io.Serializable;

public class Article implements Serializable {
    private String nom;
    private float prix;
    private int quantite;

    // Constructeur par défaut
    public Article() {
    }

    // Constructeur avec paramètres
    public Article(String nom, float prix, int quantite) {
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
    }

    // Méthodes Getter
    public String getNom() {
        return nom;
    }

    public float getPrix() {
        return prix;
    }

    public int getQuantite() {
        return quantite;
    }

    // Méthodes Setter
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public float calculerTotal() {
        return prix * quantite;
    }

    // Méthode toString pour afficher les informations de l'article
    @Override
    public String toString() {
        return "Article{" +
                "nom='" + nom + '\'' +
                ", prix=" + prix +
                ", quantite=" + quantite +
                '}';
    }

}
