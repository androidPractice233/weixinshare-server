package com.scut.weixinserver.repo;

import com.scut.weixinserver.entity.Moment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MomentRepository extends JpaRepository<Moment, Long> {
    int deleteMomentsByUserId(String userId);

    List<Moment> findMomentsByUserIdOrderByCreateTimeDesc(String userId, Pageable pageRequest);

    List<Moment> findMomentsByLatitudeBetweenAndLongitudeBetweenOrderByCreateTimeDesc(double latitude, double latitude2,
                                                                double longitude, double longitude2, Pageable pageRequest);


    List<Moment> findMomentsByLatitudeBetweenAndLongitudeBetween(double latitude, double latitude2,
                                                                double longitude, double longitude2);

    List<Moment> findMomentsByMomentIdIn(List momentIds);

    Moment findMomentByMomentId(String momentId);

    int deleteMomentByMomentId(String momentId);

}
