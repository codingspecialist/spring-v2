package shop.mtcoding.blog.board;

// C -> S -> R

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.error.ex.Exception403;
import shop.mtcoding.blog.user.User;

import java.util.List;


@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public void 게시글수정(int id, BoardRequest.UpdateDTO updateDTO, User sessionUser) {
        // 1. 게시글 조회 (없으면 404)
        Board board = boardRepository.findById(id);

        // 2. 권한체크
        if (board.getUser().getId() != sessionUser.getId()) {
            throw new Exception403("게시글을 수정할 권한이 없습니다");
        }

        // 3. 게시글 수정
        board.setTitle(updateDTO.getTitle());
        board.setContent(updateDTO.getContent());
    } // flush() 자동 호출됨 (더티체킹)

    public Board 게시글수정화면가기(int id, User sessionUser) {
        Board board = boardRepository.findById(id);

        if (board.getUser().getId() != sessionUser.getId()) {
            throw new Exception403("게시글을 수정할 권한이 없습니다");
        }

        return board;
    }

    public List<Board> 게시글목록보기() {
        List<Board> boardList = boardRepository.findAll();
        return boardList;
    }

    // 트랜잭션
    @Transactional
    public void 게시글삭제(int id, User sessionUser) {
        // 1. 컨트롤러에서 게시글 id를 받기

        // 2. 게시글 존재 여부 확인 (404)
        Board board = boardRepository.findById(id); // 10

        // 3. 내가 쓴글인지 확인하기 (403)
        if (sessionUser.getId() != board.getUser().getId()) {
            throw new Exception403("권한이 없습니다");
        }

        // 4. 게시글 삭제
        boardRepository.deleteById(id);
    }

    @Transactional
    public void 게시글쓰기(BoardRequest.SaveDTO saveDTO, User sessionUser) {
        boardRepository.save(saveDTO.toEntity(sessionUser));
    }

    public BoardResponse.DetailDTO 상세보기(int id, User sessionUser) {
        Board board = boardRepository.findById(id); // 조인 (Board - User)
        return new BoardResponse.DetailDTO(board, sessionUser);
    }
}
