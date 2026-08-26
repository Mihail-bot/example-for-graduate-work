package ru.skypro.homework.repository;

import ru.skypro.homework.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Integer> {
    List<Ad> findByAuthorId(Integer authorId);
}