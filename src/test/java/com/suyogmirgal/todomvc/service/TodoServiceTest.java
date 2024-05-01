package com.suyogmirgal.todomvc.service;

import com.suyogmirgal.todomvc.entity.TodoEntity;
import com.suyogmirgal.todomvc.model.TodoDto;
import com.suyogmirgal.todomvc.repository.TodoRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * This is unit test class for {@link TodoService}
 *
 * @author suyogmirgal
 * created on 2024/05/01
 */
@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

  static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  @Mock
  private TodoRepository todoRepository;

  private TodoService todoService;

  @BeforeEach
  void init() {
    todoService = new TodoService(todoRepository);
  }

  /**
   * This is the Unit test to verify todo create.
   */
  @Test
  public void testCreateTodo() {
    LocalDateTime dateTimeJustBeforeCreateTodo = format(LocalDateTime.now());

    TodoEntity todoEntity = new TodoEntity();
    todoEntity.setId(1);
    todoEntity.setOrder(1);
    todoEntity.setCompleted(false);
    todoEntity.setTitle("Go to Gym");
    todoEntity.setCreatedDate(dateTimeJustBeforeCreateTodo);
    todoEntity.setUpdatedDate(dateTimeJustBeforeCreateTodo);

    Mockito.when(todoRepository.save(Mockito.any(TodoEntity.class))).
        thenReturn(todoEntity);

    TodoDto createTodoDtoResult = todoService.createTodo(
        new TodoDto(1, "Go to Gym", 1, false, null, null));

    Assertions.assertEquals(1, createTodoDtoResult.getId());
    Assertions.assertEquals(1, createTodoDtoResult.getOrder());
    Assertions.assertEquals("Go to Gym", createTodoDtoResult.getTitle());
    Assertions.assertFalse(createTodoDtoResult.isCompleted());
    Assertions.assertEquals(dateTimeJustBeforeCreateTodo, createTodoDtoResult.getCreatedDate());
    Assertions.assertEquals(dateTimeJustBeforeCreateTodo, createTodoDtoResult.getUpdatedDate());

    ArgumentCaptor<TodoEntity> todoEntityArgumentCaptor = ArgumentCaptor.forClass(TodoEntity.class);
    Mockito.verify(todoRepository).save(todoEntityArgumentCaptor.capture());

    Assertions.assertEquals(1, todoEntityArgumentCaptor.getValue().getOrder());
    Assertions.assertEquals("Go to Gym", todoEntityArgumentCaptor.getValue().getTitle());
    Assertions.assertFalse(todoEntityArgumentCaptor.getValue().isCompleted());

    LocalDateTime now = format(LocalDateTime.now());

    //checks if todo created datetime is equal or after datetime recorded just before todocreate call.
    //also check if todo created datetime is equal or before datetime recorded just after todocreate call.
    Assertions.assertTrue(todoEntityArgumentCaptor.getValue().getCreatedDate().isEqual(dateTimeJustBeforeCreateTodo) ||
        todoEntityArgumentCaptor.getValue().getCreatedDate().isAfter(dateTimeJustBeforeCreateTodo));

    Assertions.assertTrue(todoEntityArgumentCaptor.getValue().getCreatedDate().isEqual(now) ||
        todoEntityArgumentCaptor.getValue().getCreatedDate().isBefore(now));

    //checks if todo updated datetime is equal or after datetime recorded just before todocreate call.
    //also check if todo updated datetime is equal or before datetime recorded just after todocreate call.
    Assertions.assertTrue(todoEntityArgumentCaptor.getValue().getUpdatedDate().isEqual(dateTimeJustBeforeCreateTodo) ||
        todoEntityArgumentCaptor.getValue().getUpdatedDate().isAfter(dateTimeJustBeforeCreateTodo));

    Assertions.assertTrue(todoEntityArgumentCaptor.getValue().getUpdatedDate().isEqual(now) ||
        todoEntityArgumentCaptor.getValue().getUpdatedDate().isBefore(now));

  }


  /**
   * This is the Unit test to verify successful get todo by todoId.
   */
  @Test
  public void testSuccessfulGetTodoByTodId() {
    LocalDateTime dateTime = format(LocalDateTime.now());

    TodoEntity todoEntity = new TodoEntity();
    todoEntity.setId(1);
    todoEntity.setOrder(1);
    todoEntity.setCompleted(false);
    todoEntity.setTitle("Go to Gym");
    todoEntity.setCreatedDate(dateTime);
    todoEntity.setUpdatedDate(dateTime);

    Mockito.when(todoRepository.findById(1)).thenReturn(Optional.of(todoEntity));

    Optional<TodoDto> createTodoDtoResultOptional = todoService.getTodoById(1);

    TodoDto createTodoDtoResult = createTodoDtoResultOptional.get();

    Assertions.assertEquals(1, createTodoDtoResult.getId());
    Assertions.assertEquals(1, createTodoDtoResult.getOrder());
    Assertions.assertEquals("Go to Gym", createTodoDtoResult.getTitle());
    Assertions.assertFalse(createTodoDtoResult.isCompleted());
    Assertions.assertEquals(dateTime, createTodoDtoResult.getCreatedDate());
    Assertions.assertEquals(dateTime, createTodoDtoResult.getUpdatedDate());
  }

  /**
   * This is the Unit test to verify unsuccessful get todo by todoId
   * which does not exist.
   */
  @Test
  public void testUnSuccessfulGetTodoByTodId() {
    Mockito.when(todoRepository.findById(1)).thenReturn(Optional.empty());

    Assertions.assertFalse(todoService.getTodoById(1).isPresent());
  }

  /**
   * This is the Unit test to verify get all todos.
   */
  @Test
  public void testGetAllTodo() {
    LocalDateTime dateTime = format(LocalDateTime.now());

    TodoEntity todoEntity1 = new TodoEntity();
    todoEntity1.setId(1);
    todoEntity1.setOrder(1);
    todoEntity1.setCompleted(false);
    todoEntity1.setTitle("Go to Gym");
    todoEntity1.setCreatedDate(dateTime);
    todoEntity1.setUpdatedDate(dateTime);

    TodoEntity todoEntity2 = new TodoEntity();
    todoEntity2.setId(2);
    todoEntity2.setOrder(2);
    todoEntity2.setCompleted(false);
    todoEntity2.setTitle("Eat breakfast");
    todoEntity2.setCreatedDate(dateTime);
    todoEntity2.setUpdatedDate(dateTime);

    Mockito.when(todoRepository.findAllByOrderByOrderAsc()).thenReturn(Arrays.asList(todoEntity1, todoEntity2));

    List<TodoDto> todoDtoList = todoService.getAllTodos();

    Assertions.assertEquals(2, todoDtoList.size());

    Assertions.assertEquals(1, todoDtoList.get(0).getId());
    Assertions.assertEquals(1, todoDtoList.get(0).getOrder());
    Assertions.assertEquals("Go to Gym", todoDtoList.get(0).getTitle());
    Assertions.assertFalse(todoDtoList.get(0).isCompleted());
    Assertions.assertEquals(dateTime, todoDtoList.get(0).getCreatedDate());
    Assertions.assertEquals(dateTime, todoDtoList.get(0).getUpdatedDate());

    Assertions.assertEquals(2, todoDtoList.get(1).getId());
    Assertions.assertEquals(2, todoDtoList.get(1).getOrder());
    Assertions.assertEquals("Eat breakfast", todoDtoList.get(1).getTitle());
    Assertions.assertFalse(todoDtoList.get(1).isCompleted());
    Assertions.assertEquals(dateTime, todoDtoList.get(1).getCreatedDate());
    Assertions.assertEquals(dateTime, todoDtoList.get(1).getUpdatedDate());
  }

  /**
   * This is the Unit test to verify get all active todos.
   */
  @Test
  public void testGetAllActiveTodo() {
    LocalDateTime dateTime = format(LocalDateTime.now());

    TodoEntity todoEntity1 = new TodoEntity();
    todoEntity1.setId(1);
    todoEntity1.setOrder(1);
    todoEntity1.setCompleted(false);
    todoEntity1.setTitle("Complete homework");
    todoEntity1.setCreatedDate(dateTime);
    todoEntity1.setUpdatedDate(dateTime);

    Mockito.when(todoRepository.findByIsCompletedOrderByOrderAsc(false)).thenReturn(Arrays.asList(todoEntity1));

    List<TodoDto> todoDtoList = todoService.getAllActiveTodos();

    Assertions.assertEquals(1, todoDtoList.size());

    Assertions.assertEquals(1, todoDtoList.get(0).getId());
    Assertions.assertEquals(1, todoDtoList.get(0).getOrder());
    Assertions.assertEquals("Complete homework", todoDtoList.get(0).getTitle());
    Assertions.assertFalse(todoDtoList.get(0).isCompleted());
    Assertions.assertEquals(dateTime, todoDtoList.get(0).getCreatedDate());
    Assertions.assertEquals(dateTime, todoDtoList.get(0).getUpdatedDate());
  }

  /**
   * This is the Unit test to verify successful update todo by todoId.
   */
  @Test
  public void testSuccessfulUpdateTodoByTodId() {
    LocalDateTime dateTime = format(LocalDateTime.now());

    TodoEntity todoEntity = new TodoEntity();
    todoEntity.setId(1);
    todoEntity.setOrder(1);
    todoEntity.setCompleted(false);
    todoEntity.setTitle("Go to Gym");
    todoEntity.setCreatedDate(dateTime);
    todoEntity.setUpdatedDate(dateTime);

    Mockito.when(todoRepository.findById(1)).thenReturn(Optional.of(todoEntity));

    Mockito.when(todoRepository.save(Mockito.any(TodoEntity.class))).thenReturn(todoEntity);

    TodoDto updatedTodo = new TodoDto(0, "Go to Gym at 6", 2, true, null, null);

    Optional<TodoDto> todoDtoUpdateResultOptional = todoService.updateTodoById(1, updatedTodo);

    TodoDto todoDtoUpdateResult = todoDtoUpdateResultOptional.get();

    Assertions.assertEquals(1, todoDtoUpdateResult.getId());
    Assertions.assertEquals(2, todoDtoUpdateResult.getOrder());
    Assertions.assertEquals("Go to Gym at 6", todoDtoUpdateResult.getTitle());
    Assertions.assertTrue(todoDtoUpdateResult.isCompleted());
    Assertions.assertEquals(dateTime, todoDtoUpdateResult.getCreatedDate());
    Assertions.assertEquals(dateTime, todoDtoUpdateResult.getUpdatedDate());

    ArgumentCaptor<TodoEntity> todoEntityArgumentCaptor = ArgumentCaptor.forClass(TodoEntity.class);
    Mockito.verify(todoRepository).save(todoEntityArgumentCaptor.capture());

    Assertions.assertEquals(2, todoEntityArgumentCaptor.getValue().getOrder());
    Assertions.assertEquals("Go to Gym at 6", todoEntityArgumentCaptor.getValue().getTitle());
    Assertions.assertTrue(todoEntityArgumentCaptor.getValue().isCompleted());
  }

  /**
   * This is the Unit test to verify successful delete todo by todoId.
   */
  @Test
  public void testSuccessfulDeleteTodoByTodId() {
    LocalDateTime dateTime = format(LocalDateTime.now());

    TodoEntity todoEntity = new TodoEntity();
    todoEntity.setId(1);
    todoEntity.setOrder(1);
    todoEntity.setCompleted(false);
    todoEntity.setTitle("Go to Gym");
    todoEntity.setCreatedDate(dateTime);
    todoEntity.setUpdatedDate(dateTime);

    Mockito.when(todoRepository.findById(1)).thenReturn(Optional.of(todoEntity));

    Assertions.assertTrue(todoService.deleteTodoById(1));

    Mockito.verify(todoRepository).delete(todoEntity);
  }

  /**
   * This is the Unit test to verify unsuccessful delete todo by todoId
   * which does not exist.
   */
  @Test
  public void testUnSuccessfulDeleteTodoByTodId() {
    Mockito.when(todoRepository.findById(1)).thenReturn(Optional.empty());

    Assertions.assertFalse(todoService.deleteTodoById(1));
  }

  /**
   * This is the Unit test to verify delete all completed todo.
   */
  @Test
  public void testDeleteAllCompletedTodo() {
    Mockito.when(todoRepository.deleteByIsCompleted(true)).thenReturn(2L);

    Assertions.assertEquals(2L, todoService.deleteAllCompletedTodos());
  }

  private LocalDateTime format(LocalDateTime localDateTime) {
    return LocalDateTime.parse(localDateTime.format(formatter), formatter);
  }
}