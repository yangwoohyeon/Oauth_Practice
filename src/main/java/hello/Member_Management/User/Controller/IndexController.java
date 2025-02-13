package hello.Member_Management.User.Controller;


import hello.Member_Management.User.Entity.User;
import hello.Member_Management.User.PrincipalDetails;
import hello.Member_Management.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping({"","/"})
    public String index(){
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails = "+principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager") //@ResponseBody ==> 자바객체를 HTTP요청의 바디 내용으로 매핑하여 클라이언트로 전송
    public @ResponseBody String manager(){
        return "manager";
    }

    @GetMapping("/loginForm") //로그인 폼
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm") //회원가입 폼
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join") //회원가입
    public String join(User user) {
        System.out.println("user = "+user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword); // 입력받은 평문 비밀번호 인코딩
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

}
