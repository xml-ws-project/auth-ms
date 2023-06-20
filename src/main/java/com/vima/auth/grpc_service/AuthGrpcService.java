package com.vima.auth.grpc_service;

import com.vima.auth.mapper.UserMapper;
import com.vima.auth.service.UserService;
import com.vima.gateway.AuthServiceGrpc;
import com.vima.gateway.AuthServiceOuterClass;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {
    private final UserService userService;
    @Override
    public void getUsername(AuthServiceOuterClass.GuestId request, StreamObserver<AuthServiceOuterClass.Username> responseObserver){
        var result = userService.findById(request.getId());
        responseObserver.onNext(UserMapper.convertEntityToUsernameResponse(result));
        responseObserver.onCompleted();

    }
}
