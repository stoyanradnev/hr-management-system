package com.emerchantpay.hr.managementsystem.domain;

import javax.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "address")
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String country;

    private String city;

    private String street;

    private String streetNumber;

    private String postCode;

}
