package hello.Member_Management.User.Repository;

import hello.Member_Management.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> { //Spring Data JPA 사용
    User findByUsername(String name); //이름으로 유저 찾기
}
