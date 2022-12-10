package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.service.file.FileService;
import com.jojoldu.book.springboot.service.posts.PostsService;
import com.jojoldu.book.springboot.util.MD5Generator;
import com.jojoldu.book.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostsApiController {
    private final PostsService postsService;
    private final FileService fileService;

    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
    @Value("${upload-path}")
    private String filePath;
    @PostMapping
    public Long save(@RequestParam(value="bfile", required=false) MultipartFile files, PostsSaveRequestDto requestDto){

        try {

            String origFilename = files.getOriginalFilename();
            // 파일명을 암호화하되 확장자는 그대로
            String filename = new MD5Generator(origFilename).toString() + "." + extractExt(origFilename);
            File newFileName = new File(filename);
            try {
                files.transferTo(newFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String newFilePath = filePath+ "\\" + filename;

            FileDto fileDto = new FileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(newFilePath);
            if (files.getContentType().startsWith("image") == true) {
                fileDto.setImgExist(true);
            }

            Long fileId = fileService.saveFile(fileDto);
            requestDto.setFileId(fileId);


        } catch (Exception e) {
            e.printStackTrace();
        }


        return postsService.save(requestDto);

    }

//    @PostMapping
//    public Long save(@RequestBody PostsSaveRequestDto requestDto){
//        return postsService.save(requestDto);
//    }

    @PutMapping("/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    //추가 실습 용
    @GetMapping
    public List<PostsListResponseDto> getPostsList(){
        return postsService.findAllDesc();
    }

    @DeleteMapping("/{id}")
    public Long delete(@PathVariable Long id){
        postsService.delete(id);
        return id;
    }

}
