package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbService {

    private static DbService INSTANCE = null;
    private String dbName = null;
    private String dbUser = null;
    private String dbPass = null;

    private DbService() {
    }

    public static DbService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DbService();
        }
        return INSTANCE;
    }

    public void setDb(String dbName, String dbUser, String dbPass) {
        if (INSTANCE.dbName == null && INSTANCE.dbUser == null && INSTANCE.dbPass == null) {
            INSTANCE.dbName = dbName;
            INSTANCE.dbUser = dbUser;
            INSTANCE.dbPass = dbPass;
        } else {
            throw new RuntimeException("Cannot change database settings");
        }
    }

    private Connection createConn() throws SQLException {
        String connUrl = "jdbc:mysql://localhost:3306/" + dbName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
        return DriverManager.getConnection(connUrl, dbUser, dbPass);
    }

    /**
     * Execute insert query and return id of created element, if not then null
     *
     * @param query
     * @param params
     * @return id or null
     * @throws SQLException
     */
    public Integer insertIntoDatabase(String query, String... params) throws SQLException {

        try (Connection conn = createConn()) {

            String[] generatedColumns = {"id"};
            PreparedStatement pst = conn.prepareStatement(query, generatedColumns);
            if (params != null) {
                int i = 1;
                for (String param : params) {
                    pst.setString(i++, param);
                }
            }

            pst.executeUpdate();

            ResultSet res = pst.getGeneratedKeys();

            if (res.next())
                return res.getInt(1);
            else
                return null;
        } catch (SQLException e) {
            throw e;
        }

    }

    public Result execute(String query, String... args) throws SQLException {
        Result result = new Result();
        if (query.trim().toUpperCase().startsWith("SELECT")) {
            result.rows = getData(query, args);
        } else if (query.trim().toUpperCase().startsWith("UPDATE") || query.trim().toUpperCase().startsWith("DELETE")
                || query.trim().toUpperCase().startsWith("DROP") || query.trim().toUpperCase().startsWith("INSERT") || query.trim().toUpperCase().startsWith("ALTER")
                || query.trim().toUpperCase().startsWith("CREATE")) {
            result.affectedRowsCount = executeUpdate(query, args);
        } else {
            throw new RuntimeException("Wrong STATEMENT");
        }
        return result;
    }

    public List<String[]> getData(String query, String... params) throws SQLException {

        try (Connection conn = createConn()) {

            //prepare query
            PreparedStatement st = getPreparedStatement(query, conn, params);
            //execute and get results
            ResultSet rs = st.executeQuery();

            //get columns from query
            ResultSetMetaData columns = rs.getMetaData();

            //prepare list of results
            List<String[]> result = new ArrayList<>();

            while (rs.next()) {

                //New String array for row data
                String[] row = new String[columns.getColumnCount()];

                for (int j = 1; j <= columns.getColumnCount(); j++) {
                    row[j - 1] = rs.getString(columns.getColumnName(j));
                }

                result.add(row);
            }

            return result;

        } catch (SQLException e) {
            throw e;
        }

    }

    private PreparedStatement getPreparedStatement(String query, Connection conn, String... params) throws SQLException {
        PreparedStatement st = conn.prepareStatement(query);
        if (params != null) {
            int i = 1;
            for (String p : params) {
                st.setString(i++, p);
            }
        }
        return st;
    }

    public int executeUpdate(String query, String... params)
            throws SQLException {
        try (Connection conn = createConn()) {
            PreparedStatement st = getPreparedStatement(query, conn, params);
            return st.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public class Result {
        private Integer affectedRowsCount;
        private List<String[]> rows;

        public Integer getAffectedRowsCount() {
            return affectedRowsCount;
        }

        public List<String[]> getRows() {
            return rows;
        }

        public void displayRowsResult(List<String[]> list) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).length; j++) {
                    System.out.print(list.get(i)[j] + " ");
                }
                System.out.println("\n");
            }
        }
    }

}

