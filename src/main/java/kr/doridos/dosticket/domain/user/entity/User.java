package kr.doridos.dosticket.domain.user.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "`user`")
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Column(nullable = true)
    private LocalDateTime deleteAt;

    public User(final String email, final String password, final String nickname,
                final String phoneNumber, final UserType userType) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }

    public User(final String email, final String nickname, final String phoneNumber) {
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    public User(final String email, final String nickname, final UserType userType) {
        this.email = email;
        this.nickname = nickname;
        this.userType = userType;
    }

    public static User of(final String email, final String password, final String nickname,
                          final String phoneNumber, final UserType userType) {
        return new User(email, password, nickname, phoneNumber, userType);
    }

    public static User ofSocial(final String email, final String nickname) {
        return new User(email, nickname, UserType.SOCIAL);
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }
}
