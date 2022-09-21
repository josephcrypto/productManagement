package cn.coding.com.productmanagement.service;

import cn.coding.com.productmanagement.dto.UserRegistrationDTO;
import cn.coding.com.productmanagement.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


public interface UserService extends UserDetailsService {

    User findByEmail(String email);
    User save(UserRegistrationDTO userRegistrationDTO);
}
