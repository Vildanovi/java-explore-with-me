package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.UserDto;
import ru.practicum.model.Users;
import ru.practicum.repository.UsersRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService {

    private final UsersRepository usersRepository;

    @Transactional
    public Users createUser(Users users) {
        return usersRepository.save(users);
    }

    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        return Collections.emptyList();
    }

    public void deleteUser(Integer userId) {
    }
}
