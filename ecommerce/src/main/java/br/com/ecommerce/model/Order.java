package br.com.ecommerce.model;

import java.math.BigDecimal;

public class Order {

    private String nameUser;
    private String userId;
    private BigDecimal amount;

    public Order(String nameUser, String userId, BigDecimal amount) {
        this.nameUser = nameUser;
        this.userId = userId;
        this.amount = amount;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
