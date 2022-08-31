package hous.server.service.todo;

import hous.server.domain.room.Room;
import hous.server.domain.todo.Redo;
import hous.server.domain.todo.Take;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.repository.RedoRepository;
import hous.server.domain.todo.repository.TakeRepository;
import hous.server.domain.todo.repository.TodoRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.OnboardingRepository;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.todo.dto.request.UpdateTodoRequestDto;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class TodoService {

    private final UserRepository userRepository;
    private final OnboardingRepository onboardingRepository;
    private final TodoRepository todoRepository;
    private final TakeRepository takeRepository;
    private final RedoRepository redoRepository;

    public void createTodo(UpdateTodoRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        TodoServiceUtils.validateTodoCounts(room);
        Todo todo = todoRepository.save(Todo.newInstance(room, request.getName(), request.isPushNotification()));
        request.getTodoUsers().forEach(todoUser -> {
            Onboarding onboarding = onboardingRepository.findOnboardingById(todoUser.getOnboardingId());
            Take take = takeRepository.save(Take.newInstance(todo, onboarding));
            todoUser.getDayOfWeeks().forEach(dayOfWeek -> {
                Redo redo = redoRepository.save(Redo.newInstance(take, dayOfWeek));
                take.addRedo(redo);
            });
            todo.addTake(take);
        });
        room.addTodo(todo);
    }

    public void updateTodo(Long todoId, UpdateTodoRequestDto request) {
        Todo todo = TodoServiceUtils.findTodoById(todoRepository, todoId);
        todo.getTakes().forEach(take -> {
            redoRepository.deleteAll(take.getRedos());
            takeRepository.delete(take);
        });
        List<Take> takes = new ArrayList<>();
        request.getTodoUsers().forEach(todoUser -> {
            Onboarding onboarding = onboardingRepository.findOnboardingById(todoUser.getOnboardingId());
            Take take = takeRepository.save(Take.newInstance(todo, onboarding));
            todoUser.getDayOfWeeks().forEach(dayOfWeek -> {
                Redo redo = redoRepository.save(Redo.newInstance(take, dayOfWeek));
                take.addRedo(redo);
            });
            takes.add(take);
        });
        todo.updateTodo(request.getName(), request.isPushNotification(), takes);
    }
}
