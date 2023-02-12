package mk.finki.ukim.mk.lab.model;


import lombok.Data;
import mk.finki.ukim.mk.lab.model.enumerations.Role;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name = "korisnici")
public class User implements UserDetails {
    @Id
    private String username;

    private String password;

    private String name;

    private String surname;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;


    @Enumerated(value = EnumType.STRING)
    private Role role;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;

    public User(String username, String password, String name, String surname, LocalDate dateOfBirth, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
    }

    public User() {
    }

    public static Role getRoleUser() {
        return Role.ROLE_USER;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
