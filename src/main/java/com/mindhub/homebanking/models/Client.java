package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    public Client() { }
    public Client(String fN, String lN, String mail){
        firstName = fN;
        lastName = lN;
        email = mail;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getEmail(){
        return email;
    }
    public void setFirstName(String fN){
        firstName = fN;
    }
    public void setLastName(String lN){
        lastName = lN;
    }
    public void setEmail(String mail){
        email = mail;
    }
}
