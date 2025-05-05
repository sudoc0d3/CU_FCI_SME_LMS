package LMS.LearningManagementSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.net.ProtocolFamily;
import java.util.List;
import java.util.Collection;

@Entity
@Table(name = "instructors")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Instructor extends User{
    @OneToMany(mappedBy = "instructor")
    private List<Course> createdCourses;
    @Enumerated(EnumType.STRING)
    protected Role role;

    public Instructor(String name, String email, String password,Role role) {
        super(name, email, password);
        this.role = Role.Instructor;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}