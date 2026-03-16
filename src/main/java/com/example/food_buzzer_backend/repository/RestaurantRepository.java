package com.example.food_buzzer_backend.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.food_buzzer_backend.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // >>> ADDED: list by approvalStatus ("PENDING", "APPROVED", "DECLINED")
    List<Restaurant> findByApprovalStatus(String approvalStatus);

    // >>> ADDED: fetch restaurant for a given owner user id
    List<Restaurant> findByOwner_Id(Long ownerUserId);

    long countByApprovalStatus(String approvalStatus);

    boolean existsBySlug(String slug);

    Optional<Restaurant> findByIdAndIsLiveTrue(Long id);
    
    boolean existsByIdAndIsLiveTrue(Long id);
}