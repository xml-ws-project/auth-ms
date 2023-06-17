package com.vima.auth.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rating", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rating {

    @Id
    @Column(name ="id",nullable = false , unique = true)
    Long id;

    @Column(nullable = false)
    int value;

    @Column(nullable = false)
    Long hostId;

    @Column(nullable = false)
    Long guestId;

    @Column(nullable = false)
    LocalDate date;
}
