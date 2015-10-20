package org.wf.dp.dniprorada.model;

import java.util.List;

public class OtpPassword {
    private String merchant_id;
    private String merchant_password;
    private List<OtpCreate> otp_create;

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_password() {
        return merchant_password;
    }

    public void setMerchant_password(String merchant_password) {
        this.merchant_password = merchant_password;
    }

    public List<OtpCreate> getOtp_create() {
        return otp_create;
    }

    public void setOtp_create(List<OtpCreate> otp_create) {
        this.otp_create = otp_create;
    }

    @Override
    public String toString() {
        return "{" + merchant_id + ":" + merchant_id + ", merchant_password:" + merchant_password + ",otp_create:"
                + otp_create + "}";
    }
}
