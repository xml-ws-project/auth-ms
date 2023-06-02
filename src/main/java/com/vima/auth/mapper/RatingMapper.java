package com.vima.auth.mapper;

import com.vima.auth.converter.LocalDateConverter;
import com.vima.auth.model.Rating;
import com.vima.gateway.RatingServiceOuterClass;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingMapper {

    public static RatingServiceOuterClass.RatingResponse convertRatingToRatingResponse(Rating rating){
        var response = RatingServiceOuterClass.RatingResponse.newBuilder()
                .setId(rating.getId())
                .setValue(rating.getValue())
                .setHostId(rating.getHostId())
                .setGuestId(rating.getGuestId())
                .setDate(LocalDateConverter.convertLocalDateToGoogleTimestamp(rating.getDate()))
                .build();

        return response;
    }
}
