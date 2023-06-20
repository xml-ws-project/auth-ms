package com.vima.auth.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AvgRateDto {
    double avgRate;
    String firstName;
    String lastName;

}
