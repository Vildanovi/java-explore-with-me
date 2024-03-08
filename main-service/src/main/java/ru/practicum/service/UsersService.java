package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.constant.Constants;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.model.Users;
import ru.practicum.pageable.OffsetBasedPageRequest;
import ru.practicum.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService {

    private final UserRepository userRepository;

    @Transactional
    public Users createUser(Users users) {
        return userRepository.save(users);
    }

    public List<Users> getUsers(List<Integer> ids, int from, int size) {
        Pageable page = new OffsetBasedPageRequest(from, size, Constants.SORT_DESC_ID);
        return userRepository.findAllByIdIn(ids, page);
    }

    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Объект не найден: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
