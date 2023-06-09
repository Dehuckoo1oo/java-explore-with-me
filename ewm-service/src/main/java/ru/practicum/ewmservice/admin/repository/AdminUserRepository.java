package ru.practicum.ewmservice.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<User,Integer> {

    List<User> findAllByIdIn(List<Integer> userIds, Pageable pageable);

    Page<User> findAll(Pageable pageable);

    Optional<User> findUserByName(String name);
}
