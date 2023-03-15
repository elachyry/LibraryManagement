
package Data_Base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySql {
    public static Connection con = null;


    static {
        GetConnection();
    }
    
    public static Connection GetConnection(){
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gestion_biblio", "root", "");
        } catch (SQLException ex) {
        }
        return con;
    }

}
