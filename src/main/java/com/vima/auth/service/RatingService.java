package com.vima.auth.service;

import com.vima.auth.model.Rating;
import com.vima.auth.model.User;
import com.vima.auth.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserService userService;

    public Rating create(int value, Long hostId, Long guestId){
       var newRating = executeRating(value, hostId, guestId);
       calculateHostRating(value, hostId);
       return newRating;
    }

    private Rating executeRating(int value, Long hostId, Long guestId){
        var newRating = Rating.builder()
                .id(Math.abs(new Random().nextLong()))
                .value(value)
                .hostId(hostId)
                .guestId(guestId)
                .date(LocalDate.now())
                .build();

        ratingRepository.save(newRating);
        return newRating;
    }

    private void calculateHostRating(int value, Long hostId){
        var host = userService.findById(hostId);
        if (host.getAvgRating() == 0) {
            calculateWhenZero(host, value);
            return;
        }

        calculateWhenNotZero(host.getId());
    }

    private void calculateWhenZero(User host, int value){
        host.setAvgRating(value);
        userService.save(host);
    }

    private void calculateWhenNotZero(Long hostId){
        var host = userService.findById(hostId);
        var number = ratingRepository.findNumberOfHostRatings(host.getId());
        var sum = ratingRepository.findSumOfHostRatings(host.getId());
        host.setAvgRating((sum * 1.00)/(number));

        userService.save(host);
    }

    public boolean delete(Long id){
        var rating = ratingRepository.findById(id).get();
        if(rating == null) return false;
        ratingRepository.delete(rating);
        calculateWhenNotZero(rating.getHostId());
        return true;
    }

    public boolean edit(Long id, int newValue){
        var rating = ratingRepository.findById(id).get();
        if(rating == null) return false;

        return executeEdit(rating, newValue);
    }

    public boolean executeEdit(Rating rating, int newValue){
        rating.setValue(newValue);
        ratingRepository.save(rating);
        calculateWhenNotZero(rating.getHostId());
        return true;
    }

}
