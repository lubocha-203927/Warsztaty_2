import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    private static final String queryCreateTableUser = "CREATE TABLE IF NOT EXISTS user (id INT AUTO_INCREMENT,"
            + " username VARCHAR(255),"
            + " email VARCHAR(255),"
            + " password VARCHAR(60),"
            + " PRIMARY KEY(id))";

    private static final String queryCreateTableSolution = "CREATE TABLE IF NOT EXISTS solution (id INT AUTO_INCREMENT,"
            + " created date,"
            + " updated date,"
            + " description text,"
            + " user_id int,"
            + " exercise_id int,"
            + " PRIMARY KEY(id),"
            + " FOREIGN KEY(exercise_id) REFERENCES exercise(id),"
            + " FOREIGN KEY(user_id) REFERENCES user(id))";

    private static final String queryCreateTableExercise = "CREATE TABLE IF NOT EXISTS exercise (id INT AUTO_INCREMENT,"
            + " title VARCHAR(255),"
            + " description text,"
            + " PRIMARY KEY(id))";

    private static final String queryCreateTableGroup = "CREATE TABLE IF NOT EXISTS " +
            "group_(id INT AUTO_INCREMENT,"
            + " name VARCHAR(255),"
            + " PRIMARY KEY(id))";

    private static final String queryCreateTableUser_Group = "CREATE TABLE IF NOT EXISTS user_group (id INT AUTO_INCREMENT,"
            + " name VARCHAR(255),"
            + " user_id INT,"
            + " group_id INT,"
            + "PRIMARY KEY(id)," +
            "FOREIGN KEY(user_id) REFERENCES user(id)," +
            "FOREIGN KEY(group_id) REFERENCES group_(id)" +
            ");";


    public static void main(String[] args) {


        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/warsztaty2?useSSL=false&characterEncoding=utf8&serverTimezone=UTC",
                "root", "mysql")) {

            Statement statement = conn.createStatement();
            statement.executeUpdate(queryCreateTableUser);
            statement.executeUpdate(queryCreateTableExercise);
            statement.executeUpdate(queryCreateTableSolution);
            statement.executeUpdate(queryCreateTableGroup);
            statement.executeUpdate(queryCreateTableUser_Group);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
