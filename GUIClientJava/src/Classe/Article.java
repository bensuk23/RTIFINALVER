package Classe;

public class Article {
    private int id;
    private String intitule;
    private float prix;
    private int stock;
    private String image;

    // Constructeur
    public Article(int id, String intitule, float prix, int stock, String image) {
        this.id = id;
        this.intitule = intitule;
        this.prix = prix;
        this.stock = stock;
        this.image = image;
    }

    public Article() {
        this.id = 1;
        this.intitule = null;
        this.prix = 0;
        this.stock = 0;
        this.image = null;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Méthode d'affichage pour faciliter le test
    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", intitule='" + intitule + '\'' +
                ", prix=" + prix +
                ", stock=" + stock +
                ", image='" + image + '\'' +
                '}';
    }

    // Exemple d'utilisation
    public static void main(String[] args) {
        Article article = new Article(1, "Article A", 19.99f, 50, "image_articleA.jpg");
        System.out.println(article);
    }
}
