package PackJDBC;


import java.sql.*;
public class MainJDBCTEST {
    public static void main(String[] args) {
        try {
            // Create a single instance of DatabaseBean for the entire server
            Class leDriver = Class.forName("com.mysql.cj.jdbc.Driver");

            DatabaseBean databaseBean = new DatabaseBean("jdbc:mysql://192.168.161.161:3306/PourStudent", "Student", "PassStudent1_");

            // Create instances of PaymentBean for each thread or use a connection pool

            // Example usage
            BeanJDBC ClientBean = new BeanJDBC(databaseBean);
            //ClientBean.performSelect();


            // Close the database connection when the server is shutting down
            databaseBean.closeConnection();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
