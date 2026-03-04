package com.parkway.demo.dto;

public class UserDTO {
    private Long userID;
    private String firstname;
    private String lastname;
    private String email;
    
    public UserDTO() {
    }
    
    public UserDTO(Long userID, String firstname, String lastname, String email) {
        this.userID = userID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }
    
    public Long getUserID() {
        return userID;
    }
    
    public void setUserID(Long userID) {
        this.userID = userID;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
