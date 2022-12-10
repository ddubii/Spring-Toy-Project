package com.jojoldu.book.springboot.web.dto;
import com.jojoldu.book.springboot.domain.files.Files;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileDto {
    private Long id;
    private String origFilename;
    private String filename;
    private String filePath;
    //
    private boolean isImgExist;

    @Builder
    public FileDto(Long id, String origFilename, String filename, String filePath, boolean isImgExist) {
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
        this.isImgExist = isImgExist;
    }

    public Files toEntity() {
        Files build = Files.builder()
                .id(id)
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filePath)
                .isImgExist(isImgExist)
                .build();
        return build;
    }



}
