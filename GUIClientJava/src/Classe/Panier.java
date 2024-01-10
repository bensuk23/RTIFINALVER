package Classe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class Panier {
    private List<Article> panier;
    private int nbArticles;

    // Constructeur
    public Panier() {
        this.panier = new ArrayList<>();
        this.nbArticles = 0;
    }

    // Méthode pour ajouter un article au panier
    public void ajouterArticle(Article article) {
        panier.add(article);
        nbArticles++;
    }

    // Méthode pour retirer un article du panier
    public void retirerArticle(Article article) {
        panier.remove(article);
        nbArticles--;
    }

    public void retirerArticleindice(int i) {
        panier.remove(i);
        nbArticles--;
    }

    // Méthode pour vider le panier
    public void viderPanier() {
        panier.clear();
        nbArticles = 0;
    }

    // Méthode pour calculer le total du panier
    public double calculerTotal() {
        double total = 0;
        for (Article article : panier) {
            total += article.getPrix()* article.getStock();
        }
        return total;
    }

    // Méthode pour afficher le contenu du panier
    public void afficherContenu() {
        System.out.println("Contenu du panier :");
        for (Article article : panier) {
            System.out.println(article);
        }
        System.out.println("Total du panier : " + calculerTotal());
        System.out.println("Nombre d'articles dans le panier : " + nbArticles);
    }

    // Getter pour nbArticles
    public int getNbArticles()
    {
        return nbArticles;
    }

    // Setter pour nbArticles
    public void setNbArticles(int nbArticles)
    {
        this.nbArticles = nbArticles;
    }

    public void setPanier(List<Article> panier) {
        this.panier = panier;
    }

    // Getter pour panier
    public List<Article> getPanier() {
        return panier;
    }

    // Exemple d'utilisation
    public static void main(String[] args) {
        // Création d'un panier
        Panier panier = new Panier();

        // Ajout d'articles au panier
        Article article1 = new Article(1, "Article A", 19.99F, 5, "image_articleA.jpg");
        Article article2 = new Article(2, "Article B", 29.99F, 3, "image_articleB.jpg");

        panier.ajouterArticle(article1);
        panier.ajouterArticle(article2);

        // Affichage du contenu du panier
        panier.afficherContenu();

        // Retrait d'un article du panier
        panier.retirerArticle(article1);

        // Affichage mis à jour du contenu du panier
        panier.afficherContenu();

        // Vidage du panier
        panier.viderPanier();

        // Affichage du panier vide
        panier.afficherContenu();
    }
}
