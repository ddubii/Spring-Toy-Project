package com.jojoldu.book.springboot.domain.posts;
import com.jojoldu.book.springboot.domain.BaseTimeEntity;
import com.jojoldu.book.springboot.domain.comment.Comment;
import com.jojoldu.book.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "posts", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc") // 댓글 정렬
    private List<Comment> comments;
    @Column
    private Long fileId;

    @Builder
    public Posts(Long id, String title, String content, String author, Long fileId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.fileId = fileId;
    }
//    @Builder
//    public Posts(String title, String content,String author) {
//        this.title = title;
//        this.content = content;
//        this.author = author;
//    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}