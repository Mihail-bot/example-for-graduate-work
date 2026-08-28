package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    /**
     * Получить комментарии к объявлению.
     * Используем commentMapper.toDtoList().
     * Внимание: внутри маппера происходит преобразование createdAt в миллисекунды.
     */
    public Comments getCommentsByAdId(Integer adId) {
        if (!adRepository.existsById(adId)) {
            throw new NotFoundException("Ad not found");
        }
        List<CommentEntity> entities = commentRepository.findByAdPk(adId);
        List<Comment> dtos = commentMapper.toDtoList(entities);
        return new Comments(dtos.size(), dtos);
    }

    /**
     * Добавить комментарий.
     * Сначала маппер создаёт сущность из DTO.
     * Устанавливаем автора и объявление вручную.
     * Сохраняем, преобразуем обратно в DTO.
     */
    @Transactional
    public Comment addComment(Integer adId, CreateOrUpdateComment createDto) {
        String email = getCurrentEmail();
        UserEntity author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Ad not found"));

        CommentEntity comment = commentMapper.toEntity(createDto);
        comment.setAuthor(author);
        comment.setAd(ad);
        // createdAt будет установлен @CreationTimestamp в БД

        CommentEntity saved = commentRepository.save(comment);
        return commentMapper.toDto(saved);
    }

    /**
     * Обновить комментарий.
     * Проверяем владельца, обновляем через маппер, сохраняем.
     */
    @Transactional
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment updateDto) {
        if (!adRepository.existsById(adId)) {
            throw new NotFoundException("Ad not found");
        }
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getAd().getPk().equals(adId)) {
            throw new NotFoundException("Comment does not belong to this ad");
        }
        checkOwnershipOrAdmin(comment.getAuthor().getEmail());
        commentMapper.updateEntity(comment, updateDto);
        CommentEntity updated = commentRepository.save(comment);
        return commentMapper.toDto(updated);
    }

    /**
     * Удалить комментарий.
     */
    @Transactional
    public void deleteComment(Integer adId, Integer commentId) {
        if (!adRepository.existsById(adId)) {
            throw new NotFoundException("Ad not found");
        }
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getAd().getPk().equals(adId)) {
            throw new NotFoundException("Comment does not belong to this ad");
        }
        checkOwnershipOrAdmin(comment.getAuthor().getEmail());
        commentRepository.delete(comment);
    }

    // === вспомогательные методы ===
    private void checkOwnershipOrAdmin(String ownerEmail) {
        String currentEmail = getCurrentEmail();
        if (currentEmail.equals(ownerEmail)) return;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(granted -> granted.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new ForbiddenException("Access denied");
        }
    }

    private String getCurrentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }
        return auth.getName();
    }
}