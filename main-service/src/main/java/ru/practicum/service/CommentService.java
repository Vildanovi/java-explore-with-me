package ru.practicum.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.constant.Constants;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.exception.ValidationBadRequestException;
import ru.practicum.model.*;
import ru.practicum.model.enumerations.RequestStatus;
import ru.practicum.pageable.OffsetBasedPageRequest;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.stats.dto.comment.NewCommentDto;
import ru.practicum.stats.dto.comment.UpdateCommentDto;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    @Transactional
    public Comments createComment(int eventId, int userId, NewCommentDto newComment) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + eventId));
        validateComment(eventId, userId);
        Comments comments = Comments.builder()
                .author(user)
                .created(LocalDateTime.now())
                .text(newComment.getText())
                .status(RequestStatus.PENDING)
                .event(event)
                .build();
        return commentRepository.save(comments);
    }


    //UserUpdate
    @Transactional
    public Comments updateComment(int commentId, int userId, UpdateCommentDto updateComment) {
        RequestStatus status;
        log.info("Пользователь обновляет комментарий");
        if (userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Объект не найден: " + userId);
        }
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + commentId));
        if (userId != comment.getAuthor().getId()) {
            throw new ValidationBadRequestException("Редактирование доступно только владельцу комментария");
        }

        if (updateComment.getCommentState() != null) {
            try {
                status = RequestStatus.valueOf(updateComment.getCommentState());
            } catch (IllegalArgumentException e) {
                throw new ValidationBadRequestException("Неизвестный параметр " + updateComment.getCommentState());
            }
            if (status.equals(RequestStatus.CANCELED)) {
                comment.setStatus(RequestStatus.CANCELED);
            } else {
                throw new ValidationBadRequestException("Некорректный статус");
            }
        }

        if (updateComment.getText() != null) {
            comment.setText(updateComment.getText());
            comment.setStatus(RequestStatus.PENDING);
        }
        return comment;
    }


    //AdminUpdate
    @Transactional
    public Comments updateCommentAdmin(int commentId, UpdateCommentDto updateComment) {
        log.info("Администратор обновляет комментарий");
        RequestStatus status;
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Объект не найден: " + commentId));


        if (updateComment.getCommentState() != null) {
            try {
                status = RequestStatus.valueOf(updateComment.getCommentState());
            } catch (IllegalArgumentException e) {
                throw new ValidationBadRequestException("Неизвестный параметр " + updateComment.getCommentState());
            }

            switch (status) {
                case CONFIRMED:
                    if (!comment.getStatus().equals(RequestStatus.PENDING)) {
                        throw new ValidationBadRequestException("Нельзя подтвердить отклоненный комментарий");
                    }
                    comment.setStatus(RequestStatus.CONFIRMED);
                    break;
                case REJECTED:
                    comment.setStatus(RequestStatus.REJECTED);
                    break;
                default:
                    throw new ValidationBadRequestException("Неизвестный статус комментария");
            }
        }
        return comment;
    }


    //privateController
    public List<Comments> getAllCommentsByUser(int userId, int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size, Constants.SORT_DESC_ID);
        return commentRepository.findByAuthor_Id(userId, pageable);
    }


    //AdminController
    public List<Comments> getComments(List<Integer> users, List<RequestStatus> status, String text,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        List<Comments> comments;
        Pageable pageable = new OffsetBasedPageRequest(from, size, Constants.SORT_DESC_CREATED);

        BooleanBuilder searchParam = new BooleanBuilder();
        if (users != null && !users.isEmpty()) {
            BooleanExpression byUsersId = QComments.comments.author.id.in(users);
            searchParam.and(byUsersId);
        }
        if (status != null && !status.isEmpty()) {
            BooleanExpression byStates = QComments.comments.status.in(status);
            searchParam.and(byStates);
        }
        if (text != null && !text.isBlank()) {
            BooleanExpression byText = QComments.comments.text.containsIgnoreCase(text);
            searchParam.and(byText);
        }
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Дата начала не может быть раньше даты завершения");
        }
        BooleanExpression byCreatedDate;
        if (rangeStart == null && rangeEnd == null) {
            LocalDateTime now = LocalDateTime.now();
            byCreatedDate = QComments.comments.created.after(now);
        } else if (rangeStart == null) {
            byCreatedDate = QComments.comments.created.before(rangeEnd);
        } else if (rangeEnd == null) {
            byCreatedDate = QComments.comments.created.after(rangeStart);
        } else {
            byCreatedDate = QComments.comments.created.between(rangeStart, rangeEnd);
        }

        if (rangeStart == null && rangeEnd == null) {
            comments = commentRepository.findAll(searchParam, pageable).getContent();
        } else {
            comments = commentRepository.findAll(searchParam.and(byCreatedDate), pageable).getContent();
        }
        return comments;
    }

    private void validateComment(int eventId, int userId) {
        LocalDateTime now = LocalDateTime.now();
        ParticipationRequest request = participationRequestRepository.findByEvent_IdAndRequester_Id(eventId, userId)
                .orElseThrow(() -> new ValidationBadRequestException("Нельзя оставить комментарий без участия в мероприятии"));
        if (request.getEvent().getEventDate().isBefore(now)) {
            throw new ValidationBadRequestException("Мероприятие не наступило, чтобы оставить отзыв");
        }
        if (!request.getStatus().equals(RequestStatus.CONFIRMED)) {
            throw new ValidationBadRequestException("Комментарировать нельзя без подтвержденного запроса на участие");
        }
    }
}
