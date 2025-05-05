package LMS.LearningManagementSystem.model;

import LMS.LearningManagementSystem.repository.StudentRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.net.ProtocolFamily;
import java.util.Collection;
import java.util.List;
import java.util.Collection;


@Entity
@Table(name = "admins")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Admin extends User {
    @Enumerated(EnumType.STRING)
    private Role role ;

    public Admin(String name, String email, String password,Role role) {
        super( name, email, password);
        this.role = Role.Admin;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

}