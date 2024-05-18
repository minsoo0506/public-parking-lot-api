package com.mnsoo.parkinglot.service;

import com.mnsoo.parkinglot.domain.persist.BookmarkEntity;
import com.mnsoo.parkinglot.domain.persist.UserEntity;
import com.mnsoo.parkinglot.exception.impl.AlreadyAddedBookmarkException;
import com.mnsoo.parkinglot.exception.impl.NoSuchBookmarkException;
import com.mnsoo.parkinglot.exception.impl.ParkingLotNotFoundException;
import com.mnsoo.parkinglot.repository.BookmarkRepository;
import com.mnsoo.parkinglot.repository.ParkingLotRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final ParkingLotRepository parkingLotRepository;


    public BookmarkService(BookmarkRepository bookmarkRepository, ParkingLotRepository parkingLotRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.parkingLotRepository = parkingLotRepository;
    }

    private UserEntity getCurrentUser() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void addBookmark(String parkingCode){
        var parkingLot = this.parkingLotRepository.findByParkingCode(Integer.parseInt(parkingCode))
                .orElseThrow(ParkingLotNotFoundException::new);


        UserEntity user = getCurrentUser();

        boolean exist = this.bookmarkRepository.existsByUserAndParkingLot(user, parkingLot);
        if(exist){
            throw new AlreadyAddedBookmarkException();
        }

        var bookmark = BookmarkEntity
                .builder()
                .user(user)
                .parkingLot(parkingLot)
                .build();

        bookmarkRepository.save(bookmark);
    }

    public List<BookmarkEntity> getBookmarks(){
        UserEntity user = getCurrentUser();

        return this.bookmarkRepository.findByUser_LoginId(user.getLoginId());
    }

    public void deleteBookmark(String parkingCode){
        UserEntity user = getCurrentUser();

        var parkingLot = this.parkingLotRepository.findByParkingCode(Integer.parseInt(parkingCode.trim())).get();

        var bookmark = this.bookmarkRepository.findByUserAndParkingLot(user, parkingLot);
        if(bookmark == null){
            throw new NoSuchBookmarkException();
        }

        this.bookmarkRepository.delete(bookmark);
    }
}
