package hello.Member_Management.User.Service;


import hello.Member_Management.User.Entity.User;
import hello.Member_Management.User.PrincipalDetails;
import hello.Member_Management.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = "+username);
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null){
            return new PrincipalDetails(userEntity);
        }
        else if (userEntity == null) {
            System.out.println("❌ DB에서 사용자를 찾을 수 없습니다. username: " + username);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
        return null;
    }
}
