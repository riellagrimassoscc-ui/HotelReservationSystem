/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sms.hotelreservationsystem;

/**
 *
 * @author Riel
 */
public class Guest{
    
    private int guestId;
    private String name;
    private String phone;
    private String email;
    
    public Guest(int guestId, String name, String phone, String email){
        this.guestId = guestId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    
    }
    
    //Getters
    public int getGuestId(){
        return guestId;
    
    }
    public String getName(){
        return name;
    
    }
    public String getPhone(){
        return phone;
    
    }
    public String getEmail(){
        return email;
    }
    
    //Setters
    public void setName(String name){
        if(!name.trim().isEmpty()){
            this.name = name;
        }else{
            System.out.println("Invalid! Name cannot be empty");
        
        }
    }
    public void setPhone(String phone){
        if(!phone.trim().isEmpty()){
            this.phone = phone;
        
        }else{
            System.out.println("Invalid! Phone cannot be empty! ");
        }
        
    
    
    }
    public void setEmail(String email){
        if(!email.trim().isEmpty()){
            this.email = email;
        
        
        }else{
            System.out.println("Invalid! Email cannot be empty");
        
        }
        
    
    }
    @Override
    public String toString(){
        return "Guest ID: " + guestId + "\n" + 
                "Guest's Name: " + name + "\n" + 
                "Phone number: " + phone + 
                "Email: " + email ;
    }

}



