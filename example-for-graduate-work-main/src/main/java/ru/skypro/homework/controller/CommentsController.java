package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentMapperService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ads/{adId}/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentRepository commentRepository;
    private final CommentMapperService commentMapperService;

    @Operation(summary = "Получение комментариев объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = Comments.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @GetMapping
    public ResponseEntity<Comments> getComments(@PathVariable Integer adId) {
        List<CommentEntity> comments = commentRepository.findByAdPk(adId);
        List<Comment> dtos = commentMapperService.toDtoList(comments);
        Comments response = new Comments();
        response.setCount(dtos.size());
        response.setResults(dtos);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Добавление комментария к объявлению")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Добавлено",
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Integer adId,
                                              @Valid @RequestBody CreateOrUpdateComment comment) {
        return ResponseEntity.ok(new Comment());
    }

    @Operation(summary = "Удаление комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Удалено"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer adId,
                                              @PathVariable Integer commentId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Обновлено",
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    })
    @PatchMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer adId,
                                                 @PathVariable Integer commentId,
                                                 @Valid @RequestBody CreateOrUpdateComment comment) {
        return ResponseEntity.ok(new Comment());
    }
}