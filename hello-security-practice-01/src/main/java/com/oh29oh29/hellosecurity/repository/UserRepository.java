package com.oh29oh29.hellosecurity.repository;

import com.oh29oh29.hellosecurity.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {
}
