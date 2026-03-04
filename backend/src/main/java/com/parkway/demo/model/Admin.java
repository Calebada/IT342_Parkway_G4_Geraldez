package com.parkway.demo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
public class Admin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    @JsonProperty("admin_id")
    private Long adminId;
    
    @Column(name = "email", unique = true, nullable = false)
    @JsonProperty("email")
    private String email;
    
    @Column(name = "password", nullable = false)
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    @Column(name = "firstname", nullable = false)
    @JsonProperty("firstname")
    private String firstname;
    
    @Column(name = "lastname", nullable = false)
    @JsonProperty("lastname")
    private String lastname;
    
    @Column(name = "parking_lot_name")
    @JsonProperty("parking_lot_name")
    @JsonAlias("parkingLotName")
    private String parkingLotName;
    
    @Column(name = "capacity")
    @JsonProperty("capacity")
    private Integer capacity;
    
    @Column(name = "price")
    @JsonProperty("price")
    private Double price;
    
    @Column(name = "location")
    @JsonProperty("location")
    private String location;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    // Default constructor
    public Admin() {
    }
    
    // Getters and Setters
    public Long getAdminId() {
        return adminId;
    }
    
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
    
    // Keep old method for backward compatibility
    public Long getStaffID() {
        return adminId;
    }
    
    public void setStaffID(Long staffID) {
        this.adminId = staffID;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFirstname() {
        return firstname;
    }
    
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public String getParkingLotName() {
        return parkingLotName;
    }
    
    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    // Keep old method for backward compatibility
    public Double getRate() {
        return price;
    }
    
    public void setRate(Double rate) {
        this.price = rate;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", parkingLotName='" + parkingLotName + '\'' +
                ", capacity=" + capacity +
                ", price=" + price +
                ", location='" + location + '\'' +
                '}';
    }
}
