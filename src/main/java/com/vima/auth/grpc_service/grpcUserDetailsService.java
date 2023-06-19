package com.vima.auth.grpc_service;

import com.vima.auth.mapper.NotificationMapper;
import com.vima.auth.dto.gRPCObjectRec;
import com.vima.auth.mapper.UserMapper;
import com.vima.auth.model.User;
import com.vima.auth.service.UserService;
import com.vima.gateway.RecommendationServiceGrpc;
import com.vima.gateway.Uuid;
import communication.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import org.springframework.beans.factory.annotation.Value;

@GrpcService
@RequiredArgsConstructor
public class grpcUserDetailsService extends userDetailsServiceGrpc.userDetailsServiceImplBase {

    private final UserService userService;
    @Value("${channel.address.recommendation-ms}")
    private String channelRecommendationAddress;

    @Override
    public void getUserDetails(communication.UserDetailsRequest request,
                               io.grpc.stub.StreamObserver<communication.UserDetailsResponse> responseObserver){

        User user = userService.loadUserByUsername(request.getUsername());
        UserDetailsResponse response = UserMapper.convertUserToUserDetailsResponse(user);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(FindUserRequest request, StreamObserver<UserDetailsResponse> responseObserver) {

        User user = userService.findById(Long.valueOf(request.getId()));
        UserDetailsResponse response = UserMapper.convertUserToUserDetailsResponse(user);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void findNotificationOptionsByUserId(UserId request, StreamObserver<NotificationOptionsResponse> responseObserver) {
        User user = userService.findById(request.getId());
        responseObserver.onNext(NotificationMapper.convertEntityToGrpc(user));
        responseObserver.onCompleted();
    }

    @Override
    public void editNotificationOptions(EditNotificationRequest request, StreamObserver<NotificationOptionsResponse> responseObserver) {
        User user = userService.editNotificationOptions(request);
        responseObserver.onNext(NotificationMapper.convertEntityToGrpc(user));
        responseObserver.onCompleted();
    }

    @Override
    public void register(RegistrationRequest request, StreamObserver<RegistrationResponse> responseObserver) {
        User user = userService.registerUser(UserMapper.covertRegisterRequestToEntity(request));
        RegistrationResponse response = UserMapper.convertUserToRegistrationResponse(user);
        createUserNode(user.getId().toString());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private void createUserNode(String userId){
        getBlockingStub().getStub().createUserNode(Uuid.newBuilder().setValue(userId).build());
        getBlockingStub().getChannel().shutdown();
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

    private gRPCObjectRec getBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(channelRecommendationAddress, 9095)
                .usePlaintext()
                .build();
        return gRPCObjectRec.builder()
                .channel(channel)
                .stub(RecommendationServiceGrpc.newBlockingStub(channel))
                .build();
    }

    @Override
    public void getByEmail(communication.email email, StreamObserver<hostId> responseObserver){
        User user = userService.loadUserByUsername(email.getValue());
        hostId response = hostId.newBuilder()
                .setValue(user.getId().toString())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
