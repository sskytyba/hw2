package com.homework.supperapp.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document(collection = "customers")
public class Customer {

    @Transient
    public static final String SEQUENCE_NAME = "customers_sequence";

    @Id
    public long id;
    public String firstName;
    public String lastName;
}
