import java.util.ArrayList;
import java.util.List;

public class RestApi {
    List<AddressBookContacts> contactList;
    public RestApi(List<AddressBookContacts> contactList) {
        this.contactList = new ArrayList<>(contactList);
    }

    public long countEntries() {
        return this.contactList.size();
    }

    public void addContactToList(AddressBookContacts addressBookContacts) {
        this.contactList.add(addressBookContacts);
    }

    public AddressBookContacts getContactData(String firstName) {
        return this.contactList.stream()
                               .filter(contactItem -> contactItem.firstName.equals(firstName))
                               .findFirst()
                               .orElse(null);
    }

    public void updateContactInfo(String firstName, int zip) {
        AddressBookContacts addressBookContacts = this.getContactData(firstName);
        if (addressBookContacts != null) {
            addressBookContacts.zip = zip;
        }
    }
}
