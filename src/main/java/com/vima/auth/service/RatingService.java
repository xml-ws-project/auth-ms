package com.vima.auth.service;

import com.vima.auth.model.Rating;
import com.vima.auth.model.User;
import com.vima.auth.repository.RatingRepository;
import com.vima.auth.util.email.EmailService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserService userService;
    private final EmailService emailService;

    @Transactional
    public Rating create(int value, Long hostId, Long guestId){
       var newRating = executeRating(value, hostId, guestId);
       calculateHostRating(value, hostId);
       userService.checkIfHostIsDistinguished(hostId);
       notifyHost(newRating);
       return newRating;
    }

    private void notifyHost(Rating rating) {
        User host = userService.findById(rating.getHostId());
        User guest = userService.findById(rating.getGuestId());
        if (host.getNotificationOptions().isProfileRating()) {
            String subject = "Profile rating notification";
            String body = "Dear " + host.getUsername() + ", " +
                "guest " + guest.getUsername() + " rated your host profile with a rating " + rating.getValue() + "." +
                "Best regards," +
                "Admin";
            emailService.sendSimpleMail(host.getUsername(), subject, body);
        }
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

        var avg = ratingRepository.findAvg(hostId);
        host.setAvgRating(avg);

        userService.save(host);
    }

    public boolean delete(Long id){
        var rating = ratingRepository.findById(id).get();
        if(rating == null) return false;
        ratingRepository.delete(rating);
        executeDelete(rating.getHostId());
        return true;
    }
    private void executeDelete(Long hostId){
        var numOfRates = ratingRepository.findNumberOfHostRatings(hostId);
        var host = userService.findById(hostId);
        if(numOfRates == 0){
            host.setAvgRating(0);
            userService.save(host);
        }
        else {
            calculateWhenNotZero(host.getId());
        }
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
