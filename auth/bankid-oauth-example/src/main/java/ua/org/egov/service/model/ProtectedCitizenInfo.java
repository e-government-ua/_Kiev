package ua.org.egov.service.model;

/**
 * @author zora.borys
 */
public class ProtectedCitizenInfo extends PublicCitizenInfo {

    private String phone;

    private String inn;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }
}
