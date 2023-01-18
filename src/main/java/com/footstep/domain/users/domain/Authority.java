package com.footstep.domain.users.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "users_id")
    private Users users;

    private String role;

    public static Authority ofUser(Users users) {
        return Authority.builder()
                .role("ROLE_USER")
                .users(users)
                .build();
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
