package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.model.Users;
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
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable page = PageRequest.of(from, size, sort);
        return userRepository.findAllByIdIn(ids, page);
    }

    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Объект не найден: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
