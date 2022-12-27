package com.yummy.foodonmytrain;

import java.io.Serializable;

public class Order implements Serializable {


    String orderId;
    int tea_coffee;
    int breakfast_veg;
    int breakfast_nonveg;
    int meal_veg;
    int meal_egg;
    int meal_chicken;
    int biryani_veg;
    int biryani_egg;
    int biryani_chicken;
    String customername;
    String customer_seatno;
    String customer_phone;
    boolean orderStatus;
    String timestamp;
    String feedback;
    double rating;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomer_trainno() {
        return customer_trainno;
    }

    public void setCustomer_trainno(String customer_trainno) {
        this.customer_trainno = customer_trainno;
    }

    String customer_trainno;

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCustomer_seatno() {
        return customer_seatno;
    }

    public void setCustomer_seatno(String customer_seatno) {
        this.customer_seatno = customer_seatno;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public int getTea_coffee() {
        return tea_coffee;
    }

    public void setTea_coffee(int tea_coffee) {
        this.tea_coffee = tea_coffee;
    }

    public int getBreakfast_veg() {
        return breakfast_veg;
    }

    public void setBreakfast_veg(int breakfast_veg) {
        this.breakfast_veg = breakfast_veg;
    }

    public int getBreakfast_nonveg() {
        return breakfast_nonveg;
    }

    public void setBreakfast_nonveg(int breakfast_nonveg) {
        this.breakfast_nonveg = breakfast_nonveg;
    }

    public int getMeal_veg() {
        return meal_veg;
    }

    public void setMeal_veg(int meal_veg) {
        this.meal_veg = meal_veg;
    }

    public int getMeal_egg() {
        return meal_egg;
    }

    public void setMeal_egg(int meal_egg) {
        this.meal_egg = meal_egg;
    }

    public int getMeal_chicken() {
        return meal_chicken;
    }

    public void setMeal_chicken(int meal_chicken) {
        this.meal_chicken = meal_chicken;
    }

    public int getBiryani_veg() {
        return biryani_veg;
    }

    public void setBiryani_veg(int biryani_veg) {
        this.biryani_veg = biryani_veg;
    }

    public int getBiryani_egg() {
        return biryani_egg;
    }

    public void setBiryani_egg(int biryani_egg) {
        this.biryani_egg = biryani_egg;
    }

    public int getBiryani_chicken() {
        return biryani_chicken;
    }

    public void setBiryani_chicken(int biryani_chicken) {
        this.biryani_chicken = biryani_chicken;
    }

    public boolean isOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
