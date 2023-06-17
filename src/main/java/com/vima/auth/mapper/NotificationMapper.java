package com.vima.auth.mapper;

import com.vima.auth.model.NotificationOptions;
import com.vima.auth.model.User;

import communication.EditNotificationRequest;
import communication.NotificationOptionsResponse;

public class NotificationMapper {

	public static NotificationOptionsResponse convertEntityToGrpc(User user) {
		return NotificationOptionsResponse.newBuilder()
			.setId(user.getNotificationOptions().getId())
			.setAccommodationRating(user.getNotificationOptions().isAccommodationRating())
			.setDistinguishedHostStatus(user.getNotificationOptions().isDistinguishedHostStatus())
			.setProfileRating(user.getNotificationOptions().isProfileRating())
			.setHostsReservationAnswer(user.getNotificationOptions().isHostsReservationAnswer())
			.setReservationRequest(user.getNotificationOptions().isReservationRequest())
			.setReservationCancellation(user.getNotificationOptions().isReservationCancellation())
			.build();
	}

	public static NotificationOptions convertEditRequestToEntity(User user, EditNotificationRequest request) {
		return user.getNotificationOptions().toBuilder()
			.reservationCancellation(request.getReservationCancellation())
			.reservationRequest(request.getReservationRequest())
			.profileRating(request.getProfileRating())
			.distinguishedHostStatus(request.getDistinguishedHostStatus())
			.hostsReservationAnswer(request.getHostsReservationAnswer())
			.accommodationRating(request.getAccommodationRating())
			.build();
	}
}
