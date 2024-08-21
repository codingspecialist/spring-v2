package shop.mtcoding.blog.user;

import lombok.Data;

public class UserRequest {

    @Data
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;

        // DTO -> UserObject
        public User toEntity() { // insert 할때만 필요
            return User.builder().username(username).password(password).email(email).build();
        }
    }

    @Data
    public static class LoginDTO {
        private String username;
        private String password;
    }
}
