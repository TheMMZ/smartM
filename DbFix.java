import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbFix {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/smartmaintain_db", "karima", "karima123");
            Statement stmt = conn.createStatement();
            stmt.execute("ALTER TABLE rapport_attachments ALTER COLUMN attachment_url TYPE TEXT");
            System.out.println("Column type changed successfully.");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
