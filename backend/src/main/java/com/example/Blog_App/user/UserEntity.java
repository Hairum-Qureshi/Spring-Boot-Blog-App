package com.example.Blog_App.user;

import com.example.Blog_App.user.enums.Provider;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private String profilePicture = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDVHG83UUjZpWbFG01RcvmHsOYq81sD05yTKbnq5tW6A&s";

    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    @Column(nullable = false, length = 500)
    private String biography = "This user has not set a biography yet.";

    @Builder.Default
    private Instant createdAt = Instant.now(); // time in UTC

    @Builder.Default
    private Instant updatedAt = Instant.now(); // time in UTC

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Provider provider = Provider.LOCAL;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
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
    public boolean isEnabled() {
        return enabled;
    }

}