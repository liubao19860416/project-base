package com.saike.grape.entity;

import java.io.Serializable;

import com.saike.grape.util.DateUtil;

/**
 * 实体bean（对返回值进行了非空判断）
 */
public class Coupon implements Serializable{
    
    private static final long serialVersionUID = 6355947057760005058L;
    
    private int id;                 // 保养券ID
    private String code;            // 业务主键uuid
    private double amount;          // 保养券金额
    private String states;          // 保养券状态  0，1，2，3
    private String valid_days;      // 保养券有效期
    private String expired_date;    // 保养券截止日期
    private String modify_date;     // 保养券修改时间
    private String description;     // 保养券备注

    public String getExpired_date() {
        return (String) (expired_date == null ? "" : DateUtil.parse(expired_date, DateUtil.FORMAT_LONG));
    }

    public void setExpired_date(String expired_date) {
        this.expired_date = expired_date;
    }

    public String getModify_date() {
        return (String) (modify_date == null ? "" : DateUtil.parse(modify_date,DateUtil.FORMAT_LONG));
    }

    public void setModify_date(String modify_date) {
        this.modify_date = modify_date;
    }
    
    public String getStates() {
        return states == null ? "" : states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValid_days() {
        return valid_days == null ? "" : valid_days;
    }

    public void setValid_days(String valid_days) {
        this.valid_days = valid_days;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Coupon [id=" + id + ", code=" + code + ", amount=" + amount
                + ", states=" + states + ", valid_days=" + valid_days
                + ", expired_date=" + expired_date + ", modify_date="
                + modify_date + ", description=" + description + "]";
    }

}
