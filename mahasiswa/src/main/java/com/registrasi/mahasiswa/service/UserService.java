package com.registrasi.mahasiswa.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registrasi.mahasiswa.dto.UserDTO;
import com.registrasi.mahasiswa.model.User;
import com.registrasi.mahasiswa.repository.UserRepository;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt()));
        user.setRole(userDTO.getRole());
        return userRepository.save(user);
    }

    public List<User> findByRole(String role) { 
        return userRepository.findByRole(role); 
    }


    public User validateUser(String username, String password) {
        // Cari pengguna berdasarkan username
        User user = userRepository.findByUsername(username);

        // Periksa apakah pengguna ditemukan dan password cocok
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        // Jika tidak cocok, kembalikan null
        return null;
    }
}
