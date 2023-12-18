package PackVESPAP;

import Donnee.Article;
import Donnee.Facture;
import PackJDBC.*;
import PackJDBC.DatabaseBean;
import Serveur.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VESPAP implements Protocole
{
    private String password;
    private DatabaseBean databaseBean;

    private BeanJDBC BeanJdbc;

    public VESPAP()
    {
        try {
            // Create a single instance of DatabaseBean for the entire server
            Class leDriver = Class.forName("com.mysql.cj.jdbc.Driver");

            databaseBean = new DatabaseBean("jdbc:mysql://192.168.161.161:3306/PourStudent", "Student", "PassStudent1_");

            // Create instances of PaymentBean for each thread or use a connection pool

            // Example usage
            BeanJdbc = new BeanJDBC(databaseBean);



            // Close the database connection when the server is shutting down

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getNom()
    {
        return "VESPAP";
    }
    @Override
    public synchronized Reponse TraiteRequete(Requete requete) throws FinConnexionException
    {

        if (requete instanceof RequeteLOGIN) return TraiteRequeteLOGIN((RequeteLOGIN) requete);
        if (requete instanceof RequeteLOGOUT) TraiteRequeteLOGOUT((RequeteLOGOUT) requete);
        if (requete instanceof RequeteGetFactures) return TraiteRequeteGetFactures((RequeteGetFactures) requete);
        if (requete instanceof RequetePayFacture) return TraiteRequetePayFacture((RequetePayFacture) requete);
        if (requete instanceof RequeteGetFacture) return TraiteRequeteGetFacture((RequeteGetFacture) requete);

        return null;
    }
    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete) throws FinConnexionException
    {
        int trv =0;
        try
        {
            trv = BeanJdbc.performSelect(requete.getLogin(),requete.getPassword());
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        if (trv == 1)
        {
            return new ReponseLOGIN(true);
        }
        else if (trv == 0)
        {
            return new ReponseLOGIN(false);
        }
        throw new FinConnexionException(new ReponseLOGIN(false));
    }
    private synchronized void TraiteRequeteLOGOUT(RequeteLOGOUT requete) throws FinConnexionException
    {

    }

    private synchronized ReponseGetFactures TraiteRequeteGetFactures(RequeteGetFactures requete) throws FinConnexionException
    {
        List <Facture> listFacBD;
        try
        {
            listFacBD = new ArrayList<>();
            listFacBD = BeanJdbc.performSelectFactureRetList(requete.getidcli());

            for (Facture f : listFacBD) {
                System.out.println(f.toString());
            }
            return new ReponseGetFactures(listFacBD);
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }


    }

    private synchronized ReponseGetFacture TraiteRequeteGetFacture(RequeteGetFacture requete) throws FinConnexionException
    {
        List <Article> listartBD;
        try
        {
            listartBD = new ArrayList<>();
            listartBD = BeanJdbc.performSelectArticleRetList(requete.getidfac());

            for (Article a : listartBD) {
                System.out.println(a.toString());
            }
            return new ReponseGetFacture(listartBD);
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }


    }
    private synchronized ReponsePayFacture TraiteRequetePayFacture(RequetePayFacture requete) throws FinConnexionException
    {
        boolean valid;
        int validint;


        valid = isValidVisa(requete.getnumVisaClient());

        if (valid == true)
        {

            try {
               validint = BeanJdbc.performUpdateF(String.valueOf(requete.getIdFac()));
               if(validint == 1)
               {
                   System.out.println("Update reussit");
               }
               else if (validint == 0)
               {
                   System.out.println("Update ratée");
               }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return new ReponsePayFacture(true);

        }
        else if (valid == false)
        {
            return new ReponsePayFacture(false);
        }
        throw new FinConnexionException(new ReponsePayFacture(false));

    }

    public static boolean isValidVisa(String visaNumber) {
        // Vérifie si la longueur du numéro de carte Visa est correcte (généralement 16 chiffres)
        if (visaNumber.length() != 16) {
            return false;
        }

        int sum = 0;
        boolean alternate = false;

        for (int i = visaNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(visaNumber.substring(i, i + 1));

            if (alternate) {
                n *= 2;

                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }

            sum += n;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }

}