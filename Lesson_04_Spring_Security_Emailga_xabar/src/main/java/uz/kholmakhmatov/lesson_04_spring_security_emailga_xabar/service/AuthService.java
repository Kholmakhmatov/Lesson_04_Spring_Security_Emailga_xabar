package uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.dto.ApiResponse;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.dto.LoginDto;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.dto.RegisterDto;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.entity.User;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.entity.enums.RoleName;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.repository.RoleRepository;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.repository.UserRepository;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.security.JwtPovider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AuthenticationManager authenticationManager;
//
    @Autowired
    JwtPovider jwtPovider;

    public ApiResponse registerUser(RegisterDto dto) {
        boolean existsByEmail = userRepository.existsByEmail(dto.getEmail());
        if (existsByEmail) {
            return new ApiResponse("This email already exist", false);
        }
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);
        sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("Muvafaqqiyatli royxatdan otdingiz! Akkountni aktivlashtirish uchun  emailda habarni tekshiring ", true);
    }

    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Anvar@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Account verify");
            mailMessage.setText("<a href = 'http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail + ">Tasdiqlang</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Akkount tasdiqlandi", true);
        }
        return new ApiResponse("Akkount allaqachon tasdiqlangan", false);

    }

    public ApiResponse login(LoginDto loginDto) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUserName(),
                            loginDto.getPassword()));
            User user = (User) authentication.getPrincipal();
            String token = jwtPovider.generateToken(loginDto.getUserName(), user.getRoles());
            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException badCredentialsException) {
            return new ApiResponse("Parol yoki username xato", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> optionalUser = userRepository.findByEmail(username);
//        if (optionalUser.isPresent())
//            return optionalUser.get();
//        throw new UsernameNotFoundException(username + " topilmadi");
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + "topilmadi"));
    }
}
