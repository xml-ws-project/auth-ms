package com.vima.auth.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class NotificationOptions {

	@Id
	@Column(nullable = false, updatable = false, unique = true)
	Long id;

	@Column(nullable = false)
	boolean reservationRequest;

	@Column(nullable = false)
	boolean reservationCancellation;

	@Column(nullable = false)
	boolean profileRating;

	@Column(nullable = false)
	boolean accommodationRating;

	@Column(nullable = false)
	boolean distinguishedHostStatus;

	@Column(nullable = false)
	boolean hostsReservationAnswer;
}
