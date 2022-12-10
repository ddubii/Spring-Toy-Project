package com.jojoldu.book.springboot.service.file;

import com.jojoldu.book.springboot.domain.files.Files;
import com.jojoldu.book.springboot.domain.files.FilesRepository;
import com.jojoldu.book.springboot.web.dto.FileDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class FileService {
    private FilesRepository filesRepository;

    public FileService(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    @Transactional
    public Long saveFile(FileDto fileDto) {
        return filesRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public void delete (Long id) {
        Files files = filesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일이 없습니다. id="+ id));
        filesRepository.delete(files);
    }

    @Transactional
    public FileDto getFile(Long id) {
        Files files = filesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일이 없습니다. id="+ id));

        FileDto fileDto = FileDto.builder()
                .id(id)
                .origFilename(files.getOrigFilename())
                .filename(files.getFilename())
                .filePath(files.getFilePath())
                .isImgExist(files.isImgExist())
                .build();
        return fileDto;
    }
}
