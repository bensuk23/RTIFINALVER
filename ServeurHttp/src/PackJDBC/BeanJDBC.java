package PackJDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Donnee.Article;
import Donnee.Facture;
import Donnee.Vente;

import java.sql.Date;

import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;

public class BeanJDBC  {
    private DatabaseBean databaseBean;

    // Constructor taking a DatabaseBean for database access
    public BeanJDBC(DatabaseBean databaseBean) {
        this.databaseBean = databaseBean;
    }

    // Example method for performing a SELECT operation
    public int performSelect(String loginP,String passWP) throws SQLException
    {
        Connection connection = databaseBean.getConnection();
        int trouve = 0;
        String sql = "SELECT * FROM employes ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Process each row of the result set

                int id = resultSet.getInt("id");
                String login = resultSet.getString("login");
                String mdp = resultSet.getString("mot_de_passe");


                // Comparaison du contenu des chaînes

                if (loginP.equals(login)&& passWP.equals(mdp))
                {
                    trouve =1;
                }



            }
        }
        return trouve;
    }

    public int performUpdateF(String idFF) throws SQLException
    {
        Connection connection = databaseBean.getConnection();

        Statement stmt = connection.createStatement();
        int trouve = 0;

        String sqlU;
        sqlU = "update Facture set paye = true where Id = "+ idFF+";";

        int nbLignes = stmt.executeUpdate(sqlU);

        if(nbLignes != 0)
        {
            trouve = 1;
        }

        return trouve;
    }
    public List<Article> performSelectFactureRetList(int idc) throws SQLException {
        Connection connection = databaseBean.getConnection();

        String sql = "SELECT * FROM articles";

        System.out.println(sql);

        List<Article> articleList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Process each row of the result set
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("intitule");
                float prix = resultSet.getFloat("prix");
                int stock = resultSet.getInt("stock");
                String image = resultSet.getString("image");
                // Create a User object and add it to the list
                Article article = new Article(id,nom, prix, stock,image);
                articleList.add(article);
            }
        }
        for (Article article : articleList) {
            System.out.println(article.toString());
            System.out.println("test");
        }


        return articleList;
    }
    public int performUpdateArticle(Article updatedArticle) throws SQLException {
        Connection connection = databaseBean.getConnection();
        int rowsAffected = 0;
        System.out.println("coucou2");

        // Écrivez la requête SQL pour mettre à jour le stock et le prix
        String sql = "UPDATE articles SET prix = ?, stock = ? WHERE id = ?";
        System.out.println("coucou3");

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setFloat(1, updatedArticle.getPrix());
            preparedStatement.setInt(2, updatedArticle.getQuantite());
            preparedStatement.setInt(3, updatedArticle.getId());

            rowsAffected = preparedStatement.executeUpdate();
            System.out.println("coucou4");
        }

        return rowsAffected;
    }



    // Add other methods for UPDATE, DELETE, and additional business logic as needed
}
