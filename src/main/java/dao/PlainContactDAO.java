package dao;

import dao.entities.Contact;
import dao.interfaces.ContactDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PlainContactDAO implements ContactDAO {
    private static final String USER = "root";
    private static final String PASSWORD = "root";


    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }

    }

    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" +
                        "ch6", USER, PASSWORD
        );
    }

    private void closeConnection(Connection connection){
        if (connection == null)
            return;

        try{
            connection.close();
        } catch (SQLException e){
            System.err.println("Cannot close connection");
            e.printStackTrace();
        }
    }

    @Override
    public List<Contact> findAll() {
        List<Contact> result = new ArrayList<>();
        Connection connection = null;

        try{
            connection = getConnection();
            PreparedStatement statement =
                    connection.prepareStatement("select * from contact");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Contact contact = new Contact();
                contact.setId(resultSet.getLong("id"));
                contact.setFirst_name(resultSet.getString("first_name"));
                contact.setSecond_name(resultSet.getString("second_name"));
                contact.setBirth_date(resultSet.getDate("birth_date"));

                result.add(contact);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Contact> findByFirstName(String firstName) {
        List<Contact> result = new ArrayList<>();
        Connection connection = null;

        try {
            connection = getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(
                            "SELECT * FROM contact WHERE contact.first_name=?");

            statement.setString(1,firstName);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Contact contact = new Contact();
                contact.setId(resultSet.getLong("id"));
                contact.setFirst_name(firstName);
                contact.setSecond_name(resultSet.getString("last_name"));
                contact.setBirth_date(resultSet.getDate("birth_date"));

                result.add(contact);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Contact> findAllWithDetails() {
        return null;
    }

    @Override
    public String findFirstNameById(Long id) {
        return null;
    }

    @Override
    public String findLastNameById(Long id) {
        return null;
    }

    @Override
    public void insert(Contact contact) {
        Connection connection = null;

        try {
            connection = getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(
                            "INSERT INTO ch6.contact(first_name, second_name, birth_date) " +
                                    "VALUES (?,?,?)");
            statement.setString(1, contact.getFirst_name());
            statement.setString(2, contact.getSecond_name());
            statement.setDate(3, contact.getBirth_date());
            statement.execute();

        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Contact contact) {

    }

    @Override
    public void delete(Long id) {
        Connection connection = null;

        try{
            connection = getConnection();
            PreparedStatement statement =
                    connection.prepareStatement("DELETE FROM contact WHERE id=?");

            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void insertWithDetails(Contact contact) {

    }
}
