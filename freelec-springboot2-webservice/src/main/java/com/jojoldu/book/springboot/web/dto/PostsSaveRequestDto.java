package com.jojoldu.book.springboot.web.dto;

import com.jojoldu.book.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private Long id; ///
    private String title;
    private String content;
    private String author;

    private Long fileId; //이미지

    @Builder
    public PostsSaveRequestDto(Long id, String title, String content, String author, Long fileId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.fileId = fileId;
    }

    public Posts toEntity() {
        return Posts.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .fileId(fileId)
                .build();
    }
}