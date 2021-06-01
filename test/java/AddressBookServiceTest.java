import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        List<AddressBookContacts> employeePayrollData = addressBookService.readAddressBookForDateRange(AddressBookService
                                                         .IOService.DB_IO,startDate, endDate);
        Assertions.assertEquals(4, employeePayrollData.size());
    }

    @Test
    public void givenAddressBook_WhenRetrieved_ShouldReturnCountOfCity() {
        AddressBookService addressBookService = new AddressBookService();
        int NumberOfContactWithCity = addressBookService.countAddressbookByCity("Panvel");
        Assertions.assertEquals(3,NumberOfContactWithCity);
    }
}
