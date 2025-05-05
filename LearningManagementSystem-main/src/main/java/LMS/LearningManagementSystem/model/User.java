package LMS.LearningManagementSystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@MappedSuperclass
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class User implements UserDetails {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Setter
    @Column(nullable = true)
    private String name;
    @Setter
    @Column(nullable = false, unique = true)
    private String email;
    @Setter
    @Column(nullable = false)
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public abstract Collection<? extends GrantedAuthority> getAuthorities();


}