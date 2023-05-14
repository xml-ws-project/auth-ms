package com.vima.auth.grpcService;

import com.vima.auth.mapper.UserMapper;
import com.vima.auth.model.User;
import com.vima.auth.service.UserService;
import communication.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class grpcUserDetailsService extends userDetailsServiceGrpc.userDetailsServiceImplBase {

    private final UserService userService;

    @Override
    public void getUserDetails(communication.UserDetailsRequest request,
                               io.grpc.stub.StreamObserver<communication.UserDetailsResponse> responseObserver){

        User user = userService.loadUserByUsername(request.getUsername());
        UserDetailsResponse response = UserMapper.convertUserToUserDetailsResponse(user);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void register(communication.RegistrationRequest request,
                         io.grpc.stub.StreamObserver<communication.RegistrationResponse> responseObserver) {
        User user = userService.registerUser(UserMapper.covertRegisterRequestToEntity(request));
        RegistrationResponse response = UserMapper.convertUserToRegistrationResponse(user);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void edit(communication.EditUserRequest request,
                     io.grpc.stub.StreamObserver<communication.EditUserResponse> responseObserver){
        if(userService.loadUserByUsername(request.getCurrentUsername())  == null){
            EditUserResponse response = EditUserResponse.newBuilder()
                    .setMessage("Ne postoji takav user")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        User user = userService.edit(UserMapper.convertGrpcToHttpRequest(request));


        EditUserResponse response = EditUserResponse.newBuilder()
                        .setMessage("Kredencijali uspesno izmenjeni")
                                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(communication.DeleteUserRequest request,
                       io.grpc.stub.StreamObserver<communication.DeleteUserResponse> responseObserver){
        User user = userService.loadUserByUsername(request.getUsername());
        if(user == null){
             DeleteUserResponse response = DeleteUserResponse.newBuilder()
                    .setMessage("Ne postoji user sa tim usernamemom!")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        userService.deleteUser(user.getId());
        DeleteUserResponse response = DeleteUserResponse.newBuilder()
                .setMessage("User "+ user.getFirstName()+ " "+user.getLastName()+" obrisan")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
