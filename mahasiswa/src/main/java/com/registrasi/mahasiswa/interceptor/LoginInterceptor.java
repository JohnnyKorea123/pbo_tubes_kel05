package com.registrasi.mahasiswa.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.registrasi.mahasiswa.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login");
            return false;
        }

        // Mendapatkan peran pengguna dari session
        User user = (User) session.getAttribute("user");
        String role = user.getRole();

        // Mendapatkan URL yang diminta
        String requestURI = request.getRequestURI();

        // Memeriksa apakah URL yang diminta adalah milik CalonMahasiswaController
        if (requestURI.startsWith("/calonMahasiswa") && "ADMIN".equals(role)) {
            response.sendRedirect("/accessDenied");
            return false;
        }

        return true;
    }
}
