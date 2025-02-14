package hello.Member_Management.User.UserInfo;

public interface OAuth2UserInfo {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();
}
