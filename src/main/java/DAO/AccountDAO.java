package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

//insert account

public class AccountDAO {
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "insert into account(username,password) values(?,?)";
            // generate primary key
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // write prapared setString methods
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            // execute a query
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int generated_id = (int) resultSet.getLong(1);
                return new Account(generated_id, account.getUsername(), account.getPassword());
            }

        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return null;

    }

    /*
     * return the database from the account table where username is provided
     */
    public Account geAccountByUserName(String username) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            // get account by username
            String sql = "select * from account where username = ?";
            // self generated key
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                int accId = resultSet.getInt("account_id");
                String user = resultSet.getString("username");
                String pass = resultSet.getString("password");
                return new Account(accId, user, pass);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // TODO: handle exception
        }
        return null;

    }
    public Account getAccounId(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "select * from account where account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int getId = resultSet.getInt("account_id");
                String username = resultSet.getString("username");
                String pass = resultSet.getString("password");
                Account account = new Account(getId, username, pass);
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // TODO: handle exception
        }
        return null;

    }

}
