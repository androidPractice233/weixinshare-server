package com.scut.weixinserver.repo;

import com.scut.weixinserver.entity.Moment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MomentRepository extends JpaRepository<Moment, Long> {
    int deleteMomentsByUserId(String userId);

    List<Moment> getMomentsByUserId(String userId, Pageable pageRequest);

    List<Moment> getMomentsByLatitudeBetweenAndLongitudeBetween(double latitude, double latitude2,
                                                                double longitude, double longitude2, Pageable pageRequest);


    List<Moment> getMomentsByLatitudeBetweenAndLongitudeBetween(double latitude, double latitude2,
                                                                double longitude, double longitude2);

    List<Moment> getMomentsByMomentIdIn(List momentIds);

    Moment getMomentByMomentId(String momentId);

    int deleteMomentByMomentId(String momentId);

}
