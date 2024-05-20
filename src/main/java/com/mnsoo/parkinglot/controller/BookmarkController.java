package com.mnsoo.parkinglot.controller;

import com.mnsoo.parkinglot.domain.persist.BookmarkEntity;
import com.mnsoo.parkinglot.exception.impl.AlreadyAddedBookmarkException;
import com.mnsoo.parkinglot.service.BookmarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"즐겨찾기(북마크)를 다루는 Controller"})
@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @ApiOperation(value = "주차장 북마크를 추가합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "parkingCode", value = "북마크에 추가할 주차장의 코드", required = true, dataType = "string", paramType = "path")
    })
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

    @ApiOperation(value = "주차장 북마크 전체를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getBookmarks(){
        List<BookmarkEntity> bookmarks = bookmarkService.getBookmarks();

        if (bookmarks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookmarks, HttpStatus.OK);
    }

    @ApiOperation(value = "주차장 북마크를 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "parkingCode", value = "북마크에서 삭제할 주차장의 코드", required = true, dataType = "string", paramType = "path")
    })
    @DeleteMapping("/{parkingCode}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteBookmark(
            @PathVariable String parkingCode
    ){
        bookmarkService.deleteBookmark(parkingCode);
        return ResponseEntity.ok("Bookmark deleted successfully");
    }
}
