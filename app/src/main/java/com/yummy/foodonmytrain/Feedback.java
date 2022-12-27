package com.yummy.foodonmytrain;

import java.io.Serializable;

public class Feedback implements Serializable {

    String customerName, customerPhone, feedback;

    public Feedback(String customerName, String customerPhone, String feedback) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.feedback = feedback;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
