package com.gop.society.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * @author GhostOfPQ
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Document
public class Person {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String surname;
    private String email;
}
