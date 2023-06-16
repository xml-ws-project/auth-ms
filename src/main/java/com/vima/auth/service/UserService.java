package com.vima.auth.service;

import com.vima.auth.dto.EditUserHttpRequest;
import com.vima.auth.mapper.NotificationMapper;
import com.vima.auth.model.NotificationOptions;
import com.vima.auth.model.User;
import com.vima.auth.repository.UserRepository;

import communication.EditNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    UserRepository userRepository;

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


}
