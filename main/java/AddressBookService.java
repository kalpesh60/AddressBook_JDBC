import java.util.List;

public class AddressBookService {

    public enum IOService {DB_IO}

    private List<AddressBookContacts> addressBookContactsList;
    private AddressBookServiceDB addressBookServiceDB;

    public AddressBookService() {
        addressBookServiceDB = AddressBookServiceDB.getInstance();
    }

    public void updateLastName(String firstName, String lastName) {
        int result = addressBookServiceDB.updateAddressBookData(firstName, lastName);
        if (result == 0) return;
        AddressBookContacts addressBookContacts = this.getAddressBookData(firstName);
        if (addressBookContacts != null) addressBookContacts.lastName = lastName;
    }

    public boolean AddressBookInSyncWithDB(String firstName) {
        List<AddressBookContacts> addressBookContactsList = addressBookServiceDB.getAddressBookData(firstName);
        return addressBookContactsList.get(0).equals(getAddressBookData(firstName));
    }

    private AddressBookContacts getAddressBookData(String firstName) {
        return this.addressBookContactsList.stream()
                .filter(addressBookDataItem -> addressBookDataItem.firstName.equals(firstName))
                .findFirst()
                .orElse(null);
    }

    public List<AddressBookContacts> readAddressBookData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
                this.addressBookContactsList = addressBookServiceDB.readData();
        return this.addressBookContactsList;
    }
}