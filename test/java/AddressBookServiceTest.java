import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class AddressBookServiceTest {
    @Test
    void givenContactsWhenAdded_ShouldReturnTheTotalCount() {
        AddressBookService addressBookService = new AddressBookService();
        List<AddressBookContacts> addressBookContacts = addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        Assertions.assertEquals(4, addressBookContacts.size());
    }

    @Test
    void givenContactWhenUpdated_ShouldSyncWithDB() {
        AddressBookService addressBookService = new AddressBookService();
        List<AddressBookContacts> addressBookContacts = addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        addressBookService.updateLastName("Ajay", "dhu");
        boolean result = addressBookService.AddressBookInSyncWithDB("Ajay");
        Assertions.assertTrue(result);
    }

    @Test
    void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        LocalDate startDate = LocalDate.of(2020, 01, 01);
        LocalDate endDate = LocalDate.now();
        List<AddressBookContacts> addressBookContacts = addressBookService.readAddressBookForDateRange(AddressBookService
                                                         .IOService.DB_IO,startDate, endDate);
        Assertions.assertEquals(4, addressBookContacts.size());
    }

    @Test
    public void givenAddressBook_WhenRetrieved_ShouldReturnCountOfCity() {
        AddressBookService addressBookService = new AddressBookService();
        int NumberOfContactWithCity = addressBookService.countAddressbookByCity("Panvel");
        Assertions.assertEquals(3,NumberOfContactWithCity);
    }

    @Test
    public void givenAddresBookDetails_WhenAdded_ShouldSyncWithDB() throws SQLException {
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        addressBookService.addNewContact("vikas", "Mane", "pune", "pune", "Mah", 1234, 156262, "sap@gal.com", "2020-01-02");
        boolean result = addressBookService.AddressBookInSyncWithDB("vikas");
        Assertions.assertTrue(result);
    }

    @Test
    public void givenMultipleContact_WhenAdded_ShouldSyncWithDB() throws SQLException, InterruptedException {
        AddressBookService addressBookService = new AddressBookService();
        List<AddressBookContacts> addressBookContacts = addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        AddressBookContacts[] addressBookContact1 = { new AddressBookContacts(5,"Kapil","Mane","Navi Mumbai","Navi Mumbai","Mah",
                12984,526341,"kapil@gmail.com","2020-05-18"),
                new AddressBookContacts(6,"Vishesh","Tane","Nagpur","Nagpur","Mah",
                        66833,852963,"Vishesh@gmail.com","2019-07-13"),
                new AddressBookContacts(7,"Manoj","dholi","Mumbai","Mumbai","Mah",
                        977653,755889,"manoj@gmail.com","2019-09-06"),};
        addressBookService.addMultipleContactsToDBUsingThreads(Arrays.asList(addressBookContact1));
        boolean result1 = addressBookService.AddressBookInSyncWithDB("Kapil");
        boolean result2 = addressBookService.AddressBookInSyncWithDB("Vishesh");
        boolean result3 = addressBookService.AddressBookInSyncWithDB("Manoj");
        System.out.println(addressBookContacts);
        Assertions.assertTrue(result1);
        Assertions.assertTrue(result2);
        Assertions.assertTrue(result3);
    }

    private AddressBookContacts[] getContactList() {
        Response response = RestAssured.get(RestAssured.baseURI +"/contacts");
        System.out.println("Contact entries in JSONServer:\n" + response.asString());
        String responseBody = response.getBody().asString();
        System.out.println("Response Body is =>  " + responseBody);
        AddressBookContacts[] arrayOfContact = new Gson().fromJson(response.asString(), AddressBookContacts[].class);
        return arrayOfContact;
    }

    @Test
    public void givenContactDataInJSONServer_WhenRetrieved_ShouldMatchTheCount() {
        AddressBookContacts[] arrayOfContact = getContactList();
        RestApi restApi = new RestApi(Arrays.asList(arrayOfContact));
        long entries = restApi.countEntries();
        Assertions.assertEquals(2, entries);
    }

    public Response addContactToJsonServer(AddressBookContacts addressBookContacts) {
        String personJson = new Gson().toJson(addressBookContacts);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(personJson);
        return request.post("/contacts");
    }

    @Test
    public void givenNewContact_WhenAdded_ShouldMatch201ResponseAndCount() {
        RestApi restApi;
        AddressBookContacts[] arrayOfContact = getContactList();
        restApi = new RestApi(Arrays.asList(arrayOfContact));
        AddressBookContacts addressBookContacts = new AddressBookContacts(3,"visa","musa","PP","PP","Mah",786,765,"vishal@gmail.com","2-2=2020");
        Response response = addContactToJsonServer(addressBookContacts);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(201, statusCode);
        addressBookContacts = new Gson().fromJson(response.asString(), AddressBookContacts.class);
        restApi.addContactToList(addressBookContacts);
        long entries = restApi.countEntries();
        Assertions.assertEquals(3, entries);
    }

    @Test
    public void givenNewDataToContact_WhenUpdated_ShouldMatch200Response() {
        RestApi restApi;
        AddressBookContacts[] arrayOfContact = getContactList();
        restApi = new RestApi(Arrays.asList(arrayOfContact));
        restApi.updateContactInfo("visa", 356);
        AddressBookContacts addressBookContacts = restApi.getContactData("visa");
        String personJson = new Gson().toJson(addressBookContacts);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(personJson);
        Response response = request.put(RestAssured.baseURI + "/contacts/" +addressBookContacts.id);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }
}