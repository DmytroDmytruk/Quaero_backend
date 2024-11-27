package org.startup.quaero.database.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.startup.quaero.enums.Role;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String firstName;

    @Column(nullable = false, length = 30)
    private String lastName;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime dateOfRegistration;

    @Column(length = 15)
    private String phone;

    @Column(length = 50)
    private String country;

    @Column(length = 50)
    private String city;

    @Column(length = 100)
    private String position;

    @Column(length = 999999999)
    private String description;

    @ElementCollection
    @CollectionTable(name = "user_additional_info", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "info", length = 255)
    private List<String> additionalInfo;

    @Column(length = 100)
    private String companyName;

    @Lob
    @Column(name="image_data", columnDefinition="LONGBLOB")
    private byte[] imageData;
}