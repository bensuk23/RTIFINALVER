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
    public String performPassFinder(String loginP) throws SQLException
    {
        Connection connection = databaseBean.getConnection();

        String passW = null;
        String sql = "SELECT * FROM clients ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Process each row of the result set


                String login = resultSet.getString("login");
                String mdp = resultSet.getString("mot_de_passe");
                // Comparaison du contenu des chaînes

                if (loginP.equals(login))
                {
                    return mdp;
                }
            }
        }

        return passW;
    }

    public int performSelectCID(String loginP) throws SQLException
    {
        Connection connection = databaseBean.getConnection();
        int trouve = 0;
        String sql = "SELECT * FROM clients ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Process each row of the result set
                String login = resultSet.getString("login");
                if (loginP.equals(login))
                {
                    trouve = resultSet.getInt("id");
                    return trouve ;
                }


            }
        }
        return trouve;
    }

    public List<Facture> performSelectFactureRetList(int idc) throws SQLException {
        Connection connection = databaseBean.getConnection();

        String sql = "SELECT * FROM Facture WHERE idClient = "+idc;

        System.out.println(sql);

        List<Facture> userList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Process each row of the result set
                int id = resultSet.getInt("id");
                int idClient = resultSet.getInt("idClient");
                Date date = resultSet.getDate("date");
                double montant = resultSet.getDouble("montant");
                boolean paye = resultSet.getBoolean("paye");

                // Create a User object and add it to the list
                Facture facture = new Facture(id, idClient, date, montant, paye);
                userList.add(facture);
            }
        }
        for (Facture f : userList) {
            System.out.println(f.toString());
            System.out.println("test");
        }
        return userList;
    }

    public List<Article> performSelectArticleRetList(int idf) throws SQLException {
        Connection connection = databaseBean.getConnection();

        String sql = "SELECT * FROM Vente WHERE idFacture = "+idf;

        System.out.println(sql);

        List<Vente> venteList = new ArrayList<>();
        List<Article> artList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Process each row of the result set
                int idfac = resultSet.getInt("idFacture");
                int idA = resultSet.getInt("idArticle");
                int qua = resultSet.getInt("quantite");

                // Create a User object and add it to the list
                Vente vente = new Vente(idfac, idA ,qua);
                venteList.add(vente);
            }
        }
        for (Vente v : venteList)
        {
            System.out.println(v.toString());
            String sql1 = "SELECT * FROM articles WHERE id = "+v.getidArticle();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql1))
            {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    // Process each row of the result set
                    String inti = resultSet.getString("intitule");
                    float prix = resultSet.getFloat("prix");

                    // Create a User object and add it to the list
                    Article article = new Article(inti, prix ,v.getquantite());
                    artList.add(article);
                }
            }
        }
        return artList;
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



    // Add other methods for UPDATE, DELETE, and additional business logic as needed
}
