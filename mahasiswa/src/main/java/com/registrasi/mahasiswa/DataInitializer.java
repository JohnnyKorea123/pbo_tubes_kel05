package com.registrasi.mahasiswa;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.registrasi.mahasiswa.model.User;
import com.registrasi.mahasiswa.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }
    }
}
