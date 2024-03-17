    package com.targil.calendar.users.repository;

    import com.targil.calendar.users.model.entity.UserEntity;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;

    @Repository
    public interface UserRepository extends JpaRepository<UserEntity, Long> {
        boolean existsByEmail(String email);
        Optional<UserEntity> findByEmail(String username);
    }