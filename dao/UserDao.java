package dao;

import model.User;
import service.DbService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements BaseDao<User> {

    private static final String dBName = "warsztaty2";
    private static final String userName = "root";
    private static final String password = "root";

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {

            String sql = "SELECT * FROM USERS";
            DbService db = DbService.getInstance();
            db.setDb(dBName, userName, password);
            List<String[]> _users = db.execute(sql).getRows();

            _users.forEach(item -> {
                User user = new User();
                user.setId(Integer.parseInt(item[0]));
                user.setUsername(item[1]);
                user.setEmail(item[2]);
                user.setPassword(item[3]);
                users.add(user);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User findById(int id) {
        User user = null;

        try {
            String sql = "SELECT * FROM users WHERE id=?";
            DbService db = DbService.getInstance();
            db.setDb(dBName, userName, password);

            //to będzie zawsze jednoelementowa tablica
            String[] _user = db.execute(sql, Integer.toString(id)).getRows().get(0);

            user = new User();
            user.setId(Integer.parseInt((_user[0])));
            user.setUsername(_user[1]);
            user.setEmail(_user[2]);
            user.setPassword(_user[3]);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;

    }

    @Override
    public User create(User user) {
        try {
            DbService db = DbService.getInstance();
            db.setDb(dBName, userName, password);
            if (user.getId() == 0) {
                String sql = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
                int id = db.insertIntoDatabase(sql, user.getUsername(), user.getEmail(), user.getPassword());
                user.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User update(User user) {
        try {
            DbService db = DbService.getInstance();
            db.setDb(dBName, userName, password);
            if (user.getId() == 0) {
                user = this.create(user);
            } else {
                String sql = "UPDATE users SET username=?, email=?, password=? WHERE id = ?";
                db.executeUpdate(sql, user.getUsername(), user.getEmail(), user.getPassword(), Integer.toString(user.getId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public boolean delete(int id) {
        try {
            if (id != 0) {
                DbService db = DbService.getInstance();
                db.setDb(dBName, userName, password);
                String sql = "DELETE FROM users WHERE id=?";
                int removedRows =  db.executeUpdate(sql, Integer.toString(id));

                if(removedRows == 1) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
