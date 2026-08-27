package ru.skypro.homework.repository;

import ru.skypro.homework.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findByAdPk(Integer adId);
}