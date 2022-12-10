package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.LoginUser;
import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.user.User;
import com.jojoldu.book.springboot.service.file.FileService;
import com.jojoldu.book.springboot.service.posts.PostsService;
import com.jojoldu.book.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final FileService fileService;

    private final HttpSession httpSession;


    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts",postsService.findAllDesc());
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }

    /* 글 상세보기 */
    @GetMapping("/posts/read/{id}")
    public String read(@PathVariable Long id, @LoginUser SessionUser user, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        List<CommentResponseDto> comments = dto.getComments();

        /* 댓글 관련 */
        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        /* 사용자 관련 */
        if (user != null) {
            model.addAttribute("user", user.getName());

            /*게시글 작성자 본인인지 확인*/
            if (dto.getId().equals(user.getName())) {
                model.addAttribute("writer", true);
            }
        }
        //postsService.update(id, requestDto); // views ++
        model.addAttribute("posts", dto);

        PostsSaveRequestDto dto2 = postsService.getPost(id);
        model.addAttribute("post", dto2);

        //만약 fileId가 존재하면 f_id==id인 fileDto도 함께 넘겨준다
        Long f_id = dto2.getFileId();
        if (f_id != null) {
            FileDto f_dto = fileService.getFile(f_id);
            model.addAttribute("file", f_dto);
        }

        return "posts-read";
    }

    //게시글 조회
    @GetMapping("/posts/{id}")
    public String detail(@PathVariable Long id, Model model) {
        PostsSaveRequestDto dto = postsService.getPost(id);
        model.addAttribute("post", dto);

        //만약 fileId가 존재하면 f_id==id인 fileDto도 함께 넘겨준다
        Long f_id = dto.getFileId();
        if (f_id != null) {
            FileDto f_dto = fileService.getFile(f_id);
            model.addAttribute("file", f_dto);
        }
        return "posts-detail";
    }


    //파일 다운로드
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@ModelAttribute FileDto dto) throws IOException {
        String filePath = "C:\\Temp\\upload";
        Path path = Paths.get(filePath + File.separator + dto.getFilename());
        String contentType = Files.probeContentType(path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename(dto.getFilename(), StandardCharsets.UTF_8)
                        .build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        Resource resource = new InputStreamResource(Files.newInputStream(path));

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }
    //이미지 출력
    @GetMapping("/display")
    public ResponseEntity<Resource> display(@RequestParam("filename") String filename) {
        String path = "C:\\Temp\\upload";

        Resource resource = new FileSystemResource(path + File.separator + filename);

        if(!resource.exists()) {
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders header = new HttpHeaders();
        Path filePath = null;
        try {
            filePath = Paths.get(path + File.separator + filename);
            header.add("Content-type", Files.probeContentType(filePath));
        } catch(IOException e) {
            e.printStackTrace();
        }
        System.out.println("look at me: "+filePath);

        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }

}