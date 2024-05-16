package com.mnsoo.parkinglot.service;

import com.mnsoo.parkinglot.domain.persist.BookmarkEntity;
import com.mnsoo.parkinglot.domain.persist.UserEntity;
import com.mnsoo.parkinglot.exception.impl.AlreadyAddedBookmarkException;
import com.mnsoo.parkinglot.exception.impl.NoSuchBookmarkException;
import com.mnsoo.parkinglot.exception.impl.ParkingLotNotFoundException;
import com.mnsoo.parkinglot.exception.impl.UserNotFoundException;
import com.mnsoo.parkinglot.repository.BookmarkRepository;
import com.mnsoo.parkinglot.repository.ParkingLotRepository;
import com.mnsoo.parkinglot.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final UserRepository userRepository;


    public BookmarkService(BookmarkRepository bookmarkRepository, ParkingLotRepository parkingLotRepository, UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.parkingLotRepository = parkingLotRepository;
        this.userRepository = userRepository;
    }

    private UserEntity getCurrentUser() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void addBookmark(String parkingCode){
        var parkingLot = this.parkingLotRepository.findByParkingCode(Integer.parseInt(parkingCode)).get();
        if(parkingLot == null) throw new ParkingLotNotFoundException();

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
