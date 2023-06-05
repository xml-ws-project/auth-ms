package com.vima.auth.grpc_service;

import com.vima.auth.mapper.RatingMapper;
import com.vima.auth.service.RatingService;
import com.vima.gateway.RatingServiceGrpc;
import com.vima.gateway.RatingServiceOuterClass;
import com.vima.gateway.TextMessage;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class RatingGrpcService extends RatingServiceGrpc.RatingServiceImplBase {

    private final RatingService ratingService;

    @Override
    public void create(RatingServiceOuterClass.RatingRequest request, StreamObserver<RatingServiceOuterClass.RatingResponse> responseObserver){
        var result = ratingService.create(request.getValue(), request.getHostId(), request.getGuestId());
        responseObserver.onNext(RatingMapper.convertRatingToRatingResponse(result));
        responseObserver.onCompleted();
    }

    @Override
    public void delete(RatingServiceOuterClass.LONG request, StreamObserver<TextMessage> responseObserver){
        var result = ratingService.delete(request.getValue());
        responseObserver.onNext(TextMessage.newBuilder().setValue(result ? "Rating deleted." : "Error!").build());
        responseObserver.onCompleted();
    }

    @Override
    public void edit(RatingServiceOuterClass.EditRatingRequest request, StreamObserver<TextMessage> responseObserver){
        var result = ratingService.edit(request.getId(), request.getNewValue());
        responseObserver.onNext(TextMessage.newBuilder().setValue(result ? "Rating edited." : "Error!").build());
        responseObserver.onCompleted();
    }

}
