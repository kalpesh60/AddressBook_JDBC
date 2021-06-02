import java.util.Objects;

public class AddressBookContacts {
    public int id;
    public String firstName;
    public String lastName;
    public String address;
    public String city;
    public String state;
    public int zip;
    public int phoneNo;
    public String email;
    public String date;

    public AddressBookContacts(int id, String firstName, String lastName, String address, String city, String state, int zip, int phoneNo, String email, String date) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNo = phoneNo;
        this.email = email;
        this.date = date;
    }

    public AddressBookContacts(int id, String firstName, String lastName, String address, String city, String state, int zip, int phoneNo, String email) {
    }

    @Override
    public String toString() {
        return "AddressBookContacts{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip=" + zip +
                ", phoneNo=" + phoneNo +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressBookContacts that = (AddressBookContacts) o;
        return id == that.id && zip == that.zip && phoneNo == that.phoneNo && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(address, that.address) && Objects.equals(city, that.city) && Objects.equals(state, that.state) && Objects.equals(email, that.email) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, address, city, state, zip, phoneNo, email, date);
    }
}
