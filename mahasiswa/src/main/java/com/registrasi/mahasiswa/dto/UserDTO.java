package com.registrasi.mahasiswa.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 5, message = "Username harus memiliki minimal 5 karakter")
    private String username;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 8, message = "password harus memiliki minimal 5 karakter")
    private String password;
    
    private String role;

    private String profilePicture;

    // Getters and Setters
}
