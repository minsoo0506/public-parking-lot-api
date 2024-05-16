package com.mnsoo.parkinglot.controller;

import com.mnsoo.parkinglot.domain.persist.BookmarkEntity;
import com.mnsoo.parkinglot.exception.impl.AlreadyAddedBookmarkException;
import com.mnsoo.parkinglot.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{parkingCode}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addBookmark(
            @PathVariable String parkingCode
    ){
        try {
            bookmarkService.addBookmark(parkingCode);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (AlreadyAddedBookmarkException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getBookmarks(){
        List<BookmarkEntity> bookmarks = bookmarkService.getBookmarks();

        if (bookmarks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookmarks, HttpStatus.OK);
    }

    @DeleteMapping("/{parkingCode}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteBookmark(
            @PathVariable String parkingCode
    ){
        bookmarkService.deleteBookmark(parkingCode);
        return ResponseEntity.ok("Bookmark deleted successfully");
    }
}
