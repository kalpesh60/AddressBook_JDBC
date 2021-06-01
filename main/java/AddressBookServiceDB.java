import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookServiceDB {
    private PreparedStatement addressBookDataStatement;
    private static AddressBookServiceDB addressBookServiceDB;
    private List<AddressBookContacts> addressBookContacts;

    private AddressBookServiceDB() {}

    public static AddressBookServiceDB getInstance() {
        if (addressBookServiceDB == null)
            addressBookServiceDB = new AddressBookServiceDB();
        return addressBookServiceDB;
    }

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
            ResultSet resultSet = statement.executeQuery(sql);
            addressBookContactsList = this.getAddressBookData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookContactsList;
    }

    public int updateAddressBookData(String firstName, String LastName) {
        String sql = String.format("update addressbook set LastName = '%s' where FirstName = '%s';",LastName, firstName);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<AddressBookContacts> getAddressBookData(String firstName) {
        List<AddressBookContacts> addressBookContactsList = null;
        if (this.addressBookDataStatement == null)
            this.prepareStatementForAddressBookData();
        try {
            addressBookDataStatement.setString(1, firstName);
            ResultSet resultSet = addressBookDataStatement.executeQuery();
            addressBookContactsList = this.getAddressBookData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookContactsList;
    }

    private void prepareStatementForAddressBookData() {
        try {
            Connection connection = this.getConnection();
            String sql = "select * from addressbook where FirstName = ?";
            addressBookDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<AddressBookContacts> getAddressBookData(ResultSet resultSet) {
        List<AddressBookContacts> addressBookContactsList = new ArrayList<>();
        try {
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

    public List<AddressBookContacts> getAddressBookForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format("select * from addressbook where Date between '%s' and '%s';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getAddressBookDataUsingDB(sql);
    }

    private List<AddressBookContacts> getAddressBookDataUsingDB(String sql) {
        List<AddressBookContacts> addressBookContactsList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            addressBookContactsList = this.getAddressBookData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookContactsList;
    }

    public int getContactByCity(String city) {
        int cityCount = 0;
        String sql = String.format("select City, count(FirstName) from addressbook where City = '%s';", city);
        try (Connection connection = addressBookServiceDB.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                cityCount = resultSet.getInt("count(FirstName)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityCount;
    }
}