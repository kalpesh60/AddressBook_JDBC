import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressBookServiceDB {

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/addressbookservice?useSSL=false";
        String userName = "root";
        String password = "root";
        Connection connection;
        System.out.println("connecting to db" + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("connection is successful:" + connection);
        return connection;
    }

    public List<AddressBookContacts> readData() {
        String sql = "select * from addressbook;";
        List<AddressBookContacts> addressBookContactsList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = null;
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String FirstName = resultSet.getString("FirstName");
                String LastName = resultSet.getString("LastName");
                String Address = resultSet.getString("Address");
                String City = resultSet.getString("City");
                String State = resultSet.getString("State");
                int Zip = resultSet.getInt("Zip");
                int PhoneNo = resultSet.getInt("PhoneNo");
                String Email = resultSet.getString("Email");
                String Type = resultSet.getString("Type");
                addressBookContactsList.add(new AddressBookContacts(id, FirstName, LastName, Address, City, State, Zip, PhoneNo, Email, Type));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookContactsList;
    }
}