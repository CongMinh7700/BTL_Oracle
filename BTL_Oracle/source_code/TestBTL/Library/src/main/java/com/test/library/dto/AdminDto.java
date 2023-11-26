package com.test.library.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class AdminDto {
    @Size(min =3,max =10,message = "Invalid first name !(3-10 characters)")
    String firstName;
    @Size(min =3,max =10,message = "Invalid last name !(3-10 characters)")
    String lastName;
    String userName;
    @Size(min =5,max =10,message = "Invalid password !(3-10 characters)")
    String password;
    String repeatPassword;
}
