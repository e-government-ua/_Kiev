package org.wf.dp.dniprorada.model;

import java.util.List;

public class OtpCreate<T> {
    private String from;
    private String phone;
    private String category;
    private List<T> sms_template;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<T> getSms_template() {
        return sms_template;
    }

    public void setSms_template(List<T> sms_template) {
        this.sms_template = sms_template;
    }

    @Override
    public String toString() {
        return "{" + "from:" + from + ", phone:" + phone + "category:" + category + ", sms_template:" + sms_template
                + "}";
    }
}
