package com.suyogmirgal.todomvc.service;

import com.suyogmirgal.todomvc.entity.TodoEntity;
import com.suyogmirgal.todomvc.model.TodoDto;
import com.suyogmirgal.todomvc.repository.TodoRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * This is service class which provides all the operations/
 * methods for managing todos.
 *
 * @author suyogmirgal
 * created on 2024/04/30
 */
@Service
public class TodoService {

  private final TodoRepository todoRepository;

  /**
   * Parameterized constructor for {@link TodoService}.
   *
   * @param todoRepository instance of {@link TodoRepository}.
   */
  public TodoService(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }


  /**
   * This method creates todo.
   *
   * @param todoDto instance of {@link TodoDto} which hold data about todo to be craeted.
   * @return {@link TodoDto} which holds data about todo created.
   */
  public TodoDto createTodo(final TodoDto todoDto){
    TodoEntity todoEntity = new TodoEntity();
    todoEntity.setTitle(todoDto.getTitle());
    todoEntity.setOrder(todoDto.getOrder());
    TodoEntity savedTodoEntity = todoRepository.save(todoEntity);
    return new TodoDto(savedTodoEntity.getId(), savedTodoEntity.getTitle(),
            savedTodoEntity.getOrder(), savedTodoEntity.isCompleted(),
            savedTodoEntity.getCreatedDate(), savedTodoEntity.getUpdatedDate());
  }

  /**
   * This method provides details about todo for provided todoId.
   *
   * @param todoId id of the todo to be searched.
   * @return Optional of {@link TodoDto} if todo found for provided todoId else Optional.empty().
   */
  public Optional<TodoDto> getTodoById(final int todoId){
    Optional<TodoEntity>  todoEntityOptional = todoRepository.findById(todoId);
    if(todoEntityOptional.isPresent()) {
      TodoEntity todoEntity = todoEntityOptional.get();
      return Optional.of(new TodoDto(todoEntity.getId(), todoEntity.getTitle(),
          todoEntity.getOrder(), todoEntity.isCompleted(), todoEntity.getCreatedDate(), todoEntity.getUpdatedDate()));
    }
    return Optional.empty();
  }

  /**
   * This method provides list of all active and completed todos.
   *
   * @return List of {@link TodoDto}.
   */
  public List<TodoDto> getAllTodos(){
    List<TodoEntity>  todoEntityList = todoRepository.findAllByOrderByOrderAsc();
    List<TodoDto> todoDtoList = new ArrayList<>();

    todoEntityList.forEach(todoEntity -> {
      todoDtoList.add(new TodoDto(todoEntity.getId(), todoEntity.getTitle(),
          todoEntity.getOrder(), todoEntity.isCompleted(), todoEntity.getCreatedDate(), todoEntity.getUpdatedDate()));
    });
    return todoDtoList;
  }

  /**
   * This method provides list of all active todos.
   *
   * @return List of {@link TodoDto}.
   */
  public List<TodoDto> getAllActiveTodos(){
    List<TodoEntity>  todoEntityList = todoRepository.findByIsCompletedOrderByOrderAsc(false);
    List<TodoDto> todoDtoList = new ArrayList<>();

    todoEntityList.forEach(todoEntity -> {
      todoDtoList.add(new TodoDto(todoEntity.getId(), todoEntity.getTitle(),
          todoEntity.getOrder(), todoEntity.isCompleted(), todoEntity.getCreatedDate(), todoEntity.getUpdatedDate()));
    });
    return todoDtoList;
  }

  /**
   * This method updates todo for provided todoId.
   *
   * @param todoId id of the todo to be updated.
   * @param updatedTodo instance of {@link TodoDto} holds data about todo to be updated.
   * @return Optional of updated {@link TodoDto} if todo found for provided todoId else Optional.empty().
   */
  public Optional<TodoDto> updateTodoById(final int todoId, final TodoDto updatedTodo){
    Optional<TodoEntity>  todoEntityOptional = todoRepository.findById(todoId);
    if(todoEntityOptional.isPresent()) {
      TodoEntity todoEntity = todoEntityOptional.get();
      if(updatedTodo.getTitle() != null && updatedTodo.getTitle().trim().length() > 0) {
        todoEntity.setTitle(updatedTodo.getTitle());
      }
      if(updatedTodo.getOrder() != null) {
        todoEntity.setOrder(updatedTodo.getOrder());
      }
      if(updatedTodo.isCompleted()) {
        todoEntity.setCompleted(true);
      }
      TodoEntity updatedTodoEntity = todoRepository.save(todoEntity);
      return Optional.of(new TodoDto(updatedTodoEntity.getId(), updatedTodoEntity.getTitle(),
          updatedTodoEntity.getOrder(), updatedTodoEntity.isCompleted(), updatedTodoEntity.getCreatedDate(),
          updatedTodoEntity.getUpdatedDate()));
    }
    return Optional.empty();
  }

  /**
   * This method deletes todo for provided todoId.
   *
   * @param todoId id of the todo to be searched.
   * @return true if todo for provided todoId is found and deleted else false.
   */
  public boolean deleteTodoById(final int todoId){
    Optional<TodoEntity>  todoEntityOptional = todoRepository.findById(todoId);
    if(todoEntityOptional.isPresent()) {
      todoRepository.delete(todoEntityOptional.get());
      return true;
    }
    return false;
  }

  /**
   * This method deletes all completed todos.
   *
   * @return number of completed todo deleted.
   */
  @Transactional
  public long deleteAllCompletedTodos(){
    return todoRepository.deleteByIsCompleted(true);
  }
}
