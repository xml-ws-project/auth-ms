package com.vima.auth.dto;

import com.vima.gateway.RecommendationServiceGrpc;
import com.vima.gateway.ReservationServiceGrpc;

import io.grpc.ManagedChannel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class gRPCReservationObject {

	ManagedChannel channel;
	ReservationServiceGrpc.ReservationServiceBlockingStub stub;
}
