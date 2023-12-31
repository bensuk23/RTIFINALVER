package Donnee;

import java.io.Serializable;

public class Article implements Serializable {
    private int id;
    private String nom;
    private float prix;
    private int quantite;
    private String image;

    // Constructeur par défaut
    public Article(String inti, float prix, int getquantite) {
    }

    // Constructeur avec paramètres
    public Article(int id,String nom, float prix, int quantite,String image) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
        this.image = image;
    }
    public Article(int id,String nom, float prix, int quantite) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;

    }

    // Méthodes Getter
    public int getId(){return id;}
    public String getNom() {
        return nom;
    }

    public float getPrix() {
        return prix;
    }

    public int getQuantite() {
        return quantite;
    }
    public String getImage(){return image;}

    // Méthodes Setter
    public void setId(int id){this.id=id;}
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    public void setImage(String image){this.image = image;}

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
