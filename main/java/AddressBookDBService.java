import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
    private PreparedStatement addressBookDataStatement;
    private static AddressBookDBService addressBookServiceDB;

    private AddressBookDBService() {}

    public static AddressBookDBService getInstance() {
        if (addressBookServiceDB == null)
            addressBookServiceDB = new AddressBookDBService();
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

    public AddressBookContacts addNewContact(String firstName, String lastName, String address, String city, String state, int zip, int phoneNo, String email, String date) {
        int id = -1;
        AddressBookContacts addressBookContacts = null;
        String sql = String.format("insert into addressbook(firstName, lastName, address, city, state, zip, phoneNo, email, date)" + " values ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s','%s');",
                firstName, lastName, address, city, state, zip, phoneNo, email, date);
        try (Connection connection = this.getConnection()) {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            int rowChanged = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowChanged == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    id = resultSet.getInt(1);
                connection.commit();
            }
            addressBookContacts = new AddressBookContacts(id, firstName, lastName, address, city, state, zip, phoneNo, email, date);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return addressBookContacts;
    }

    public List<AddressBookContacts> readData() {
        String sql = "select * from addressbook";
        List<AddressBookContacts> addressBookContactsList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                int zip = resultSet.getInt("zip");
                int phoneNo = resultSet.getInt("phoneNo");
                String email = resultSet.getString("email");
                addressBookContactsList.add(new AddressBookContacts(id, firstName, lastName, address, city, state, zip, phoneNo, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookContactsList;
    }

    public int updateAddressBookData(String firstName, String lastName) {
        String sql = String.format("update addressbook set LastName = '%s' where FirstName = '%s';", lastName, firstName);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<AddressBookContacts> getAddressBookForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format("select * from addressbook where Date between '%s' and '%s';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getAddressBookDataUsingDB(sql);
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

    private List<AddressBookContacts> getAddressBookData(ResultSet resultSet) {
        List<AddressBookContacts> addressBookContactsList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                int zip = resultSet.getInt("zip");
                int phoneNo = resultSet.getInt("phoneNo");
                String email = resultSet.getString("email");
                addressBookContactsList.add(new AddressBookContacts(id, firstName, lastName, address, city, state, zip, phoneNo, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookContactsList;
    }

    private void prepareStatementForAddressBookData() {
        try {
            Connection connection = this.getConnection();
            String sql = "select * from addressbook where firstName = ?";
            addressBookDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}