package dao;


import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeQuery("select * from bank_client");
        ResultSet result = stmt.getResultSet();
        List<BankClient> clients = new ArrayList<>();
        while (result.next()) {
            long id = result.getLong(1);
            String clientName = result.getString(2);
            String password = result.getString(3);
            long money = result.getLong(4);
            clients.add(new BankClient(id, clientName, password, money));
        }
        stmt.close();
        return clients;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("select * from bank_client where name=? and password=?");
        pstmt.setString(1, name);
        pstmt.setString(2, password);
        ResultSet res = pstmt.executeQuery();
        res.next();
        return res.getString(2).equals(name) && res.getString(3).equals(password);
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        if (validateClient(name, password)) {
            PreparedStatement pstmt1 = connection.prepareStatement("update bank_client set money=? where name=? and password=?");

            PreparedStatement pstmt2 = connection.prepareStatement("select * from bank_client where name=?");
            pstmt2.setString(1, name);
            ResultSet res = pstmt2.executeQuery();
            res.next();
            long cash = res.getLong(4);
            pstmt1.setLong(1, (cash + transactValue));
            pstmt1.setString(2, name);
            pstmt1.setString(3, password);
            pstmt1.executeUpdate();
        }
    }

    public BankClient getClientById(long id) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeQuery("select * from bank_client where id=" + id);
        ResultSet result = stmt.getResultSet();
        result.next();
        long clientId = result.getLong(1);
        String clientName = result.getString(2);
        String password = result.getString(3);
        long money = result.getLong(4);
        stmt.close();
        return new BankClient(clientId, clientName, password, money);
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("select * from bank_client where name=?");
        pstmt.setString(1, name);
        ResultSet result = pstmt.executeQuery();
        result.next();
        long res = result.getLong(4);
        pstmt.close();
        return expectedSum <= res;
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeQuery("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        long id = result.getLong(1);
        String clientName = result.getString(2);
        String password = result.getString(3);
        long money = result.getLong(4);
        stmt.close();
        return new BankClient(id, clientName, password, money);
    }

    public void addClient(BankClient client) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("insert into bank_client (id, name, password, money) values (" + client.getId() + ", '" + client.getName() + "'," + " '" + client.getPassword() + "'," + client.getMoney() + ")");
        stmt.close();
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id), unique (name))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("drop table if exists bank_client");
        stmt.close();
    }

    public boolean deleteClient(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        boolean res = 1 == stmt.executeUpdate("delete from bank_client where name='" + name + "'");
        stmt.close();
        return res;
    }
}
