package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog.user.User;

import java.util.List;

// 식별자 요청 받기, 응답 하기
@RequiredArgsConstructor
@Controller // 식별자 요청을 받을 수 있다.
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final HttpSession session;


    // url : http://localhost:8080/board/1/update
    // body : title=제목1변경&content=내용1변경
    // content-type : x-www-form-urlencoded
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable("id") int id, @RequestParam("title") String title, @RequestParam("content") String content) {
        boardRepository.updateById(title, content, id);
        return "redirect:/board/" + id;
    }


    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        boardRepository.deleteById(id);
        return "redirect:/board";
    }


    // subtitle=제목1&postContent=내용1
    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO saveDTO) { // 스프링 기본전략 = x-www-form-urlencoded 파싱

        saveDTO.setContent(saveDTO.getContent().replace("<", "&lt"));

        User sessionUser = (User) session.getAttribute("sessionUser");

        // 인증 체크 필요함
        if (sessionUser == null) {
            throw new RuntimeException("로그인이 필요합니다");
        }

        boardRepository.save(saveDTO.toEntity(sessionUser));
        return "redirect:/board";
    }


    // get, post
    @GetMapping("/board")
    public String list(HttpServletRequest request) {
        List<Board> boardList = boardRepository.findAll();
        request.setAttribute("models", boardList);

        return "board/list";
    }

    // 1. 메서드 : Get
    // 2. 주소 : /board/1
    // 3. 응답 : board/detail
    @GetMapping("/board/{id}")
    public String detail(@PathVariable("id") Integer id, HttpServletRequest request) {
//        Board board = boardRepository.findById(id);
//        request.setAttribute("model", board);
//        request.setAttribute("isOwner", false);

        User sessionUser = (User) session.getAttribute("sessionUser");

        BoardResponse.DetailDTO detailDTO = boardService.상세보기(id, sessionUser);
        request.setAttribute("model", detailDTO);

        return "board/detail";
    }


    // 1. 메서드 : Get
    // 2. 주소 : /board/save-form
    // 3. 응답 : board/save-form
    @GetMapping("/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }

    // 1. 메서드 : Get
    // 2. 주소 : /board/1/update-form
    // 3. 응답 : board/update-form
    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable("id") int id, HttpServletRequest request) {
        Board board = boardRepository.findById(id);
        request.setAttribute("model", board);
        return "board/update-form";
    }
}
