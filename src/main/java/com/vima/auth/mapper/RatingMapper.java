package com.vima.auth.mapper;

import com.vima.auth.converter.LocalDateConverter;
import com.vima.auth.dto.RateInfoDto;
import com.vima.auth.model.Rating;
import com.vima.gateway.RatingServiceOuterClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public static RatingServiceOuterClass.RatingByHostId convertRatingInfoToRatingResponse(RateInfoDto rating){
        var response = RatingServiceOuterClass.RatingByHostId.newBuilder()
                .setId(rating.getRating().getId())
                .setValue(rating.getRating().getValue())
                .setUsername(rating.getUsername())
                .setGuestId(rating.getRating().getGuestId())
                .setDate(LocalDateConverter.convertLocalDateToGoogleTimestamp(rating.getRating().getDate()))
                .build();

        return response;

    }
    public static List<RatingServiceOuterClass.RatingByHostId> convertEntitytoDtoList(List<RateInfoDto> ratingList){
        List<RatingServiceOuterClass.RatingByHostId> responseList = new ArrayList<>();
            ratingList.forEach(rating ->{
                var ratingRes = convertRatingInfoToRatingResponse(rating);
                responseList.add(ratingRes);
            });
            return responseList;
    }
}
