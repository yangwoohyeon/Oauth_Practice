package hello.Member_Management.User.UserInfo;

import java.util.LinkedHashMap;
import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes; // getAttributes()
    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        Object object = attributes.get("kakao_account");
        LinkedHashMap accountMap = (LinkedHashMap) object;
        return (String) accountMap.get("email");
    }

    @Override
    public String getName() {
        LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) attributes.get("kakao_account");
        LinkedHashMap<String, Object> profileMap = (LinkedHashMap<String, Object>) accountMap.get("profile");
        return (String) profileMap.get("nickname");
    }

}
