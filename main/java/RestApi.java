import java.util.ArrayList;
import java.util.List;

public class RestApi {
    List<AddressBookContacts> dataList;
    public RestApi(List<AddressBookContacts> dataList) {
        this.dataList = new ArrayList<>(dataList);
    }

    public long countEntries() {
        return this.dataList.size();
    }
}