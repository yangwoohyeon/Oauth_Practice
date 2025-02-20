package hello.Member_Management.User.Entity;


import com.nimbusds.oauth2.sdk.TokenIntrospectionSuccessResponse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name="user_id", unique = true)
    private String userId; //유저 식별자
    private String  password; //비밀번호
    private String email; //이메일
    private String role; //유저 신분
    private String name; //이름

    @CreationTimestamp //INSERT 쿼리가 발생할 때, 현재 시간을 값으로 채워서 쿼리를 생성한다.
    private Timestamp timestamp; //가입일 기록

    private String provider;
    private String providerId;

}
