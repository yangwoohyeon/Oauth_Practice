package hello.Member_Management.User.Controller;


import hello.Member_Management.User.Entity.User;
import hello.Member_Management.User.Jwt.JwtTokenProvider;
import hello.Member_Management.User.PrincipalDetails;
import hello.Member_Management.User.Repository.UserRepository;
import hello.Member_Management.User.Service.UserService;
import hello.Member_Management.User.UserCreateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;


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
    public String joinForm(Model model) {
        model.addAttribute("userCreateForm",new UserCreateForm()); //객체 생성후 모델에 담아준다.
        return "joinForm";
    }

    @PostMapping("/joinForm") //회원가입
    public String join(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
      if(bindingResult.hasErrors()){
          return "joinForm";
      }
      if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())){
          bindingResult.rejectValue("passwerd2","passwordInCorrect","2개의 패스워드가 일치하지 않습니다.");
          return "joinForm";
      }
      userService.registraion(userCreateForm.getUsername(),userCreateForm.getPassword1(),userCreateForm.getEmail(),userCreateForm.getId());
      return "redirect:/";
    }


}

