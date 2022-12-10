package com.jojoldu.book.springboot.domain.files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilesRepository extends JpaRepository<Files, Long> {

}