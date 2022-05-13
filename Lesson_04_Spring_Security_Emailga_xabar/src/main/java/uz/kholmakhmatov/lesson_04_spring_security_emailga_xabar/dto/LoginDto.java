package uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginDto {
    @NotNull
    @Email
    private String userName;

    @NotNull
    private String password;
}
