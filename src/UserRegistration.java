import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserRegistration {
    public boolean register(String username, String password) {
        try {
            Connection conn = DatabaseConnection.connect();
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password); 

            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
