package com.vima.auth.mapper;

import com.vima.auth.dto.EditUserHttpRequest;
import com.vima.auth.model.NotificationOptions;
import com.vima.auth.model.User;
import communication.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public static UserDetailsResponse convertUserToUserDetailsResponse(final User user) {
        return UserDetailsResponse.newBuilder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setRole(convertToMessageRole(user.getRole()))
                .setPenalties(user.getPenalties())
                .setNotificationOptions(NotificationMapper.convertEntityToGrpc(user))
                .build();
    }

    private static com.vima.auth.model.enums.Role convertToEntityRole(Role role) {
        return role.equals(Role.GUEST) ? com.vima.auth.model.enums.Role.GUEST : com.vima.auth.model.enums.Role.HOST;
    }

    private static Role convertToMessageRole(com.vima.auth.model.enums.Role role) {
        return role.equals(com.vima.auth.model.enums.Role.GUEST) ?  Role.GUEST: Role.HOST;
    }

    public static User covertRegisterRequestToEntity(final RegistrationRequest request) {
        NotificationOptions notificationOptions = NotificationOptions.builder()
            .id(Math.abs(new Random().nextLong()))
            .accommodationRating(false)
            .distinguishedHostStatus(false)
            .hostsReservationAnswer(false)
            .profileRating(false)
            .reservationRequest(false)
            .reservationCancellation(false)
            .build();

        return User.builder()
                .firstName(request.getFirstName())
                .id(Math.abs(generateRandomLongWithMaxDigits(16)))
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .role(convertGrpcRoleToEntityRole(request.getRole()))
                .phoneNumber(request.getPhoneNumber())
                .password(request.getPassword())
                .location("Location")
                .notificationOptions(notificationOptions)
                .build();
    }

    public static RegistrationResponse convertUserToRegistrationResponse(final User user){
        return RegistrationResponse.newBuilder()
                .setMessage("Kreiran user " + user.getFirstName() + " " + user.getLastName() )
                .build();

    }

    public static com.vima.auth.model.enums.Role convertGrpcRoleToEntityRole(Role role){
        String roleValue = role.name();
        com.vima.auth.model.enums.Role entityRole = com.vima.auth.model.enums.Role.valueOf(roleValue);
        return entityRole;
    }

    public static EditUserHttpRequest convertGrpcToHttpRequest(EditUserRequest request){
        return EditUserHttpRequest.builder()
                .currentUsername(request.getCurrentUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .location(request.getLocation())
                .username(request.getUsername())
                .build();

    }

    public static long generateRandomLongWithMaxDigits(int maxDigits) {
        Random random = new Random();
        long max = (long) Math.pow(10, maxDigits) - 1;

        long randomLong;
        do {
            randomLong = Math.abs(random.nextLong());
        } while (randomLong > max);

        return randomLong;
    }

}
