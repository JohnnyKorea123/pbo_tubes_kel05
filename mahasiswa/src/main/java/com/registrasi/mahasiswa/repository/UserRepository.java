package com.registrasi.mahasiswa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registrasi.mahasiswa.model.User;
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    List<User> findByRole(String role);
}
