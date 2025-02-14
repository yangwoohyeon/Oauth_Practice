package hello.Member_Management.User;

public interface OAuth2UserInfo {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();
}
