package com.sms.hrsam.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Deneme {

    @Id
    @GeneratedValue
    private long id;

    String name;
}
