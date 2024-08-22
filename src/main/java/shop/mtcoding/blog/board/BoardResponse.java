package shop.mtcoding.blog.board;

import lombok.Data;
import shop.mtcoding.blog.user.User;

public class BoardResponse {

    @Data
    public static class DetailDTOV2 {
        private Integer id;
        private String title;
        private String content;
        private Boolean isOwner;
        private UserDTO user;

        @Data
        public class UserDTO {
            private Integer id;
            private String username;
        }

    }


    @Data
    public static class DetailDTO {
        private Integer boardId;
        private String title;
        private String content;
        private Boolean isOwner;
        private Integer userId;
        private String username;

        public DetailDTO(Board board, User sessionUser) {
            this.boardId = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.isOwner = false;

            if (board.getUser().getId() == sessionUser.getId()) {
                isOwner = true;
            }

            this.userId = board.getUser().getId();
            this.username = board.getUser().getUsername();
        }
    }
}
