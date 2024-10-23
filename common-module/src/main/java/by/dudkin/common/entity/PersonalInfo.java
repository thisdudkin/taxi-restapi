package by.dudkin.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;

/**
 * @author Alexander Dudkin
 */
@Embeddable
public class PersonalInfo {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phone;

    @Column
    private LocalDate dateOfBirth;

}
