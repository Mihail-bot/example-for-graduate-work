package ru.skypro.homework.repository;

import ru.skypro.homework.entity.AdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdRepository extends JpaRepository<AdEntity, Integer> {
    List<AdEntity> findByAuthorId(Integer authorId);
}