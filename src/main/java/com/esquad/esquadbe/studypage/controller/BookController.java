package com.esquad.esquadbe.studypage.controller;

import com.esquad.esquadbe.studypage.service.BookService;
import com.esquad.esquadbe.studypage.vo.BookVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/esquad/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/search")
    public ResponseEntity<List<BookVo>> searchTitle(@RequestParam("title") String title, Model model) {
        log.info("받은 제목: {}", title);

        List<BookVo> bookList = bookService.resultList(title);
        model.addAttribute("bookList", bookList);

        return ResponseEntity.status(200).body(bookList);
    }

    @GetMapping("/search/{isbn}")
    public ResponseEntity<List<BookVo>> showDetail(@PathVariable String isbn, Model model) {
        log.info("받은 ISBN: {}", isbn);

        List<BookVo> book = bookService.resultDetail(isbn);
        model.addAttribute("book", book);

        return ResponseEntity.status(200).body(book);
    }
}