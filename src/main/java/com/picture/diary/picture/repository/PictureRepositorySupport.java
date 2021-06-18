package com.picture.diary.picture.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PictureRepositorySupport {

    private final JPAQueryFactory queryFactory;


}
