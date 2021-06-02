import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class AddressBookService {
    public enum IOService {DB_IO}

    private List<AddressBookContacts> addressBookContactsList;
    private AddressBookDBService addressBookServiceDB;

    public AddressBookService() {
        addressBookServiceDB = AddressBookDBService.getInstance();
    }

    public List<AddressBookContacts> readAddressBookData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.addressBookContactsList = addressBookServiceDB.readData();
        return this.addressBookContactsList;
    }

    public int countAddressbookByCity(String city) {
        return addressBookServiceDB.getContactByCity(city);
    }

    public void updateLastName(String firstName, String lastName) {
        int result = addressBookServiceDB.updateAddressBookData(firstName, lastName);
        if (result == 0) return;
        AddressBookContacts addressBookContacts = this.getAddressBookData(firstName);
        if (addressBookContacts != null) addressBookContacts.lastName = lastName;
    }

    public boolean AddressBookInSyncWithDB(String firstName) {
        List<AddressBookContacts> addressBookContactsList = addressBookServiceDB.getAddressBookData(firstName);
        return Objects.equals(addressBookContactsList.get(0), getAddressBookData(firstName));
    }

    private AddressBookContacts getAddressBookData(String firstName) {
        AddressBookContacts addressBookContacts = null;
        for (AddressBookContacts addressBookDataItem : addressBookContactsList)
            if (addressBookDataItem.firstName.equals(firstName)) {
                addressBookContacts = addressBookDataItem;
                break;
            }
        return addressBookContacts;

    }

    public List<AddressBookContacts> readAddressBookForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) {
        if (ioService.equals(IOService.DB_IO))
            return addressBookServiceDB.getAddressBookForDateRange(startDate, endDate);
        return null;
    }

    public List<AddressBookContacts> addNewContact(String firstName, String lastName, String address, String city, String state, int zip, int phoneNo, String email, String date) {
        addressBookContactsList.add(addressBookServiceDB.addNewContact(firstName, lastName, address, city, state, zip, phoneNo, email, date));
        return addressBookContactsList;
    }
}
