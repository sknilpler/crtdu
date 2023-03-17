package com.diplom.crtdu.models;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

/**
 * Сущность пользователь
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String type;

    private String surname;
    private String name;
    private String patronymic;
    @Transient
    private String passwordConfirm;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;


//    @OneToOne(mappedBy = "user")
//    private Kid kid;

    public User(String username, String password, String passwordConfirm) {
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public User(String username, String password, String passwordConfirm, String type) {
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.type = type;
    }

    public User(String username, String password, String type, String surname, String name, String patronymic, String passwordConfirm) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.passwordConfirm = passwordConfirm;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", passwordConfirm='" + passwordConfirm + '\'' +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}