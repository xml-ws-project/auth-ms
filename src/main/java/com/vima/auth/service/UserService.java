package com.vima.auth.service;

import com.vima.auth.dto.EditUserHttpRequest;
import com.vima.auth.dto.gRPCReservationObject;
import com.vima.auth.mapper.NotificationMapper;
import com.vima.auth.model.NotificationOptions;
import com.vima.auth.model.User;
import com.vima.auth.repository.UserRepository;
import com.vima.gateway.HostDistinguishedRequest;
import com.vima.gateway.ReservationServiceGrpc;

import communication.EditNotificationRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Value("${channel.address.reservation-ms}")
    private String channelReservationAddress;

    public List<User> findAll() {
        return userRepository.findAll();
    };
    public User registerUser(User user){
        user.setPenalties(0);
        return userRepository.save(user);
    }
    public User changeUserInfo(User newUserInfo){
        return userRepository.save(newUserInfo);
    }
    public void deleteUser(Long id){
        userRepository.deleteById(id);
      /*  if(user.getRole().equals("Guest")){
            //treba dodati uslov ako Guest nema rezervacija
            userRepository.deleteById(id);
        } else
        {
            //treba dodati uslov ako Host nema zakazanih termina u svom smestaju, i brisu mu se i smestaji
            userRepository.deleteById(user.getId());
        } */
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User editNotificationOptions(EditNotificationRequest request) {
        User user = userRepository.findById(request.getId()).orElseThrow(EntityNotFoundException::new);
        user.setNotificationOptions(NotificationMapper.convertEditRequestToEntity(user, request));
        return userRepository.save(user);
    }

    public User edit(EditUserHttpRequest request){
        User user = loadUserByUsername(request.getCurrentUsername());
        if(!request.getEmail().equals("") ){
            user.setEmail(request.getEmail());
        }
        if(!request.getFirstName().equals("")){
            user.setFirstName(request.getFirstName());
        }
        if(!request.getLastName().equals("")){
            user.setLastName(request.getLastName());
        }
        if(!request.getPassword().equals("")){
            user.setPassword(request.getPassword());
        }
        if(!request.getLocation().equals("")){
            user.setLocation(request.getLocation());
        }
        if(!request.getUsername().equals("")){
            user.setUsername(request.getUsername());
        }
        if(!request.getPhoneNumber().equals("")){
            user.setPhoneNumber(request.getPhoneNumber());
        }
        userRepository.save(user);
        return user;
    }

    public void checkIfHostIsDistinguished(Long hostId) {
        var hostOpt = userRepository.findById(hostId);
        if (hostOpt.isEmpty()) {
            return;
        }
        var host = hostOpt.get();
        host.setDistinguished(hasSatisfyingAvgRating(host) && retrieveCriteriaAnswer(host));
        userRepository.save(host);
    }

    private boolean retrieveCriteriaAnswer(User host) {
        var reservationBlockingStub = getBlockingReservationStub();
        var isAcceptingCriteria = reservationBlockingStub
            .getStub()
            .isHostDistinguished(HostDistinguishedRequest.newBuilder().setHostId(host.getId()).build());
        host.setDistinguished(hasSatisfyingAvgRating(host));
        reservationBlockingStub.getChannel().shutdown();
        return isAcceptingCriteria.getAnswer();
    }

    private boolean hasSatisfyingAvgRating(User host) {
        return host.getAvgRating() > 4.7;
    }

    public void save(User user){
        userRepository.save(user);
    }

    private gRPCReservationObject getBlockingReservationStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(channelReservationAddress, 9094)
            .usePlaintext()
            .build();
        return gRPCReservationObject.builder()
            .channel(channel)
            .stub(com.vima.gateway.ReservationServiceGrpc.newBlockingStub(channel))
            .build();
    }
}
