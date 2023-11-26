package com.test.library.dto;

import com.test.library.model.City;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    @Column(name="customer_id")
    private Long id;
    @Size(min =3,max=15,message = "First name should have 3- 15 characters")
    private String firstName;
    @Size(min =3,max=15,message = "Last name should have 3- 15 characters")
    private String lastName;
    private String userName;
    @Size(min =3,max=20,message = "Password should have 3- 15 characters")
    private String password;
    @Size(min = 10, max = 15, message = "Phone number contains 10-15 characters")
    private String phoneNumber;
    private String confirmPassword;
    private String address;
    private City city;
    private String image;
    private String country;

}
