package CCStatistics.DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MixedSQL {
    private SQL sql = new SQL();

    public Connection getConnection() {
        return sql.getConnection();
    }

    // Read query voor alle items uit MixedDAO
    public ArrayList<ArrayList<String>> readQuery(String[] columns, PreparedStatement preparedStatement) {
        // ResultSet is de tabel die we van de database terugkrijgen.
        // We kunnen door de rows heen stappen en iedere kolom lezen.
        ResultSet rs = null;

        ArrayList<ArrayList<String>> tableList = new ArrayList<>();
        try {
            // 'Importeer' de driver die je gedownload hebt.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Pak de prepared statement en voert hem uit
            rs = preparedStatement.executeQuery();

            // Als de resultset waarden bevat dan lopen we hier door deze waarden en printen
            // ze.
            // ArrayList 1 is voor de rij, de geneste ArrayList is voor de kolom in de rij.

            while (rs.next()) {
                // row (i)
                ArrayList<String> strings = new ArrayList<>();
                for (String column : columns) {
                    // column j in row i wordt toegevoegd.
                    strings.add(rs.getString(column));
                }
                tableList.add(strings);
            }
        }

        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (Exception e) {
                }
        }

        return tableList;
    }

    public static Boolean printSQLException(SQLException ex) {
        return SQL.printSQLException(ex);
    }

}
