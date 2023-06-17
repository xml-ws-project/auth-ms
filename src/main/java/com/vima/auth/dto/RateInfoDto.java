package com.vima.auth.dto;

import com.vima.auth.model.Rating;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateInfoDto {
    Rating rating;
    String username;
}
