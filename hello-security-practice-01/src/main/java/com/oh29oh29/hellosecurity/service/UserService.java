package com.oh29oh29.hellosecurity.service;

import com.oh29oh29.hellosecurity.domain.Account;
import com.oh29oh29.hellosecurity.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void createUser(Account account) {
        userRepository.save(account);
    }
}
