import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public void addMultipleContactsToDBUsingThreads(List<AddressBookContacts> asList) throws InterruptedException {
        Map<Integer, Boolean> addressAdditionStatus = new HashMap<>();
        asList.forEach(addressBookContacts -> {
            Runnable task = () -> { addressAdditionStatus.put(addressBookContacts.hashCode(), false);
                System.out.println("Multiple Added:" + Thread.currentThread().getName());
                try {
                    this.addNewContact(addressBookContacts.getFirstName(), addressBookContacts.getLastName(),
                            addressBookContacts.getAddress(), addressBookContacts.getCity(), addressBookContacts.getState(),
                            addressBookContacts.getPhoneNo(), addressBookContacts.getZip(), addressBookContacts.getEmail(),
                            addressBookContacts.getDate());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                addressAdditionStatus.put(addressBookContacts.hashCode(), true);
                System.out.println("Multiple Contact Added:" + Thread.currentThread().getName()); };
            Thread thread = new Thread(task, addressBookContacts.getFirstName());
            thread.start();
        });
        while (addressAdditionStatus.containsValue(false))
            Thread.sleep(0);
    }
}
