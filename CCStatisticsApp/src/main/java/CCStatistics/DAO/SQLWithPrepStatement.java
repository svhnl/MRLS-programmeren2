package CCStatistics.DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLWithPrepStatement {
    
    public Connection getConnection(){
        //pakt login
        Login login = new Login();
        String connectionUrl = null;
        try {
            if(login.getLogin().equals(null)){
             throw new NullPointerException();
            } else{
             connectionUrl = login.getLogin();
            }
         } catch (IOException ioe) {
             ioe.printStackTrace();
         } catch(NullPointerException npe){
             npe.printStackTrace();
         }
         //TODO: handle exception op een goed manier

         //probeert met logingegevens een verbinding te maken
         try {
             Connection connection = DriverManager.getConnection(connectionUrl);
        return connection;
    } catch (SQLException e) {

        // print SQL exception information
        printSQLException(e);
    }
    //Dit zou nooit moeten kunnen. Een verbinding gaat óf goed óf throwt een exception
    return null;
    }

    public ArrayList<ArrayList<String>> readQuery(String table,PreparedStatement preparedStatement){
        //Pakt de columns van getColumns om typefouten te voorkomen
        ArrayList<String> columns = this.getColumns(table);
        
        // ResultSet is de tabel die we van de database terugkrijgen.
        // We kunnen door de rows heen stappen en iedere kolom lezen.
        ResultSet rs = null;

        ArrayList<ArrayList<String>> tableList = new ArrayList<>();
        try {
            // 'Importeer' de driver die je gedownload hebt.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Pak de prepared statement en voert hem uit
            rs = preparedStatement.executeQuery();

            // Als de resultset waarden bevat dan lopen we hier door deze waarden en printen ze.
            // ArrayList 1 is voor de rij, de geneste ArrayList is voor de kolom in de rij.
            
            while (rs.next()) {
                //row (i)
                ArrayList<String> strings = new ArrayList<>();
                for(String column : columns){
                    //column j in row i wordt toegevoegd.
                    strings.add(rs.getString(column));
                }
                tableList.add(strings);
            }
        }

        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (rs != null) try { rs.close(); } catch(Exception e) {}
        }

        return tableList;
    }

    public ArrayList<String> getColumns(String table){
        // ResultSet is de tabel die we van de database terugkrijgen.
        // We kunnen door de rows heen stappen en iedere kolom lezen.
        ResultSet rs = null;

        ArrayList<String> columns = new ArrayList<>();
        try {
            // 'Importeer' de driver die je gedownload hebt.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String oldquery = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + table + "' ORDER BY ORDINAL_POSITION";
            String rawquery = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? ORDER BY ORDINAL_POSITION";
            Connection connection = this.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(rawquery);
            preparedStatement.setString(1, table);
            // Pak de prepared statement en voer hem uit
            rs = preparedStatement.executeQuery();
            // Als de resultset waarden bevat dan lopen we hier door deze waarden en printen ze.
            // ArrayList 1 is voor de rij, de geneste ArrayList is voor de kolom in de rij.

            while (rs.next()) {
                columns.add(rs.getString("COLUMN_NAME"));
            }
        }

        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (rs != null) try { rs.close(); } catch(Exception e) {}
        }

        return columns;
    }

	public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

}