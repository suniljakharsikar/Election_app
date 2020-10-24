package b2d.l.mahtmagandhi;

public class AddressData {
    String addressname, address;

    public AddressData(String addressname, String address) {
        this.addressname = addressname;
        this.address = address;
    }

    public AddressData() {
    }

    public String getAddressname() {
        return addressname;
    }

    public void setAddressname(String addressname) {
        this.addressname = addressname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
