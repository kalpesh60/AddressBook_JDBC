import java.util.ArrayList;
import java.util.List;

public class RestApi {
    List<AddressBookContacts> contactList;
    public RestApi(List<AddressBookContacts> dataList) {
        this.contactList = new ArrayList<>(contactList);
    }

    public long countEntries() {
        return this.contactList.size();
    }

    public void addContactToList(AddressBookContacts addressBookContacts) {
        this.contactList.add(addressBookContacts);
    }
}
