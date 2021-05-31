import java.util.List;

public class AddressBookService {
    public enum IOService {DB_IO}
    private List<AddressBookContacts> addressBookContactsList;

    public AddressBookService() {}

    public List<AddressBookContacts> readAddressBookContacts(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
                this.addressBookContactsList = new  AddressBookServiceDB().readData();
        return this.addressBookContactsList;
    }
}