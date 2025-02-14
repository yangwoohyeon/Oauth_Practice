package hello.Member_Management.User.Service;


import hello.Member_Management.User.Entity.User;
import hello.Member_Management.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registraion(String username, String password, String email){
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole("ROLE_USER");
        user.setProvider("Web");
        user.setProviderId("Web");
        userRepository.save(user);
        return user;
    }
}
