import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cinerush_db", 
                "root", 
                ""
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
