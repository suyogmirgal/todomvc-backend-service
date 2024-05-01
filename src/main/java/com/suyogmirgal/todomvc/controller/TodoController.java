package com.suyogmirgal.todomvc.controller;


import com.suyogmirgal.todomvc.model.TodoDto;
import com.suyogmirgal.todomvc.service.TodoService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is controller class which has all the API
 * methods for managing todos.
 *
 * @author suyogmirgal
 * created on 2024/04/29
 */
@RestController
@RequestMapping("/todo")
public class TodoController {

  private final TodoService todoService;

  /**
   * Parametrized constructor for {@link TodoController}.
   *
   * @param todoService instance of {@link TodoService}.
   */
  public TodoController(TodoService todoService) {
    this.todoService = todoService;
  }

  /**
   * API Method to create todo.
   *
   * @param todo instance of {@link TodoDto} holds data about todo to be created.
   * @return response {@link ResponseEntity} which holds Http Status and body {@link TodoDto}.
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<TodoDto> createTodo(@RequestBody final TodoDto todo) {
    TodoDto todoDto = todoService.createTodo(todo);
    return ResponseEntity.status(HttpStatus.CREATED).body(todoDto);
  }

  /**
   * API Method to get all active and isCompleted todos.
   *
   *
   * @return response {@link ResponseEntity} which holds Http Status and body List of {@link TodoDto}.
   */
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<TodoDto>> getAllToDo() {
    return ResponseEntity.status(HttpStatus.OK).body(todoService.getAllTodos());
  }

  /**
   * API Method to get all active todos.
   *
   *
   * @return response {@link ResponseEntity} which holds Http Status and body List of {@link TodoDto}.
   */
  @RequestMapping(method = RequestMethod.GET, value = "/active")
  public ResponseEntity<List<TodoDto>> getAllActiveToDo() {
    return ResponseEntity.status(HttpStatus.OK).body(todoService.getAllActiveTodos());
  }

  /**
   * API Method to get todo for provided todoId.
   *
   * @param todoId id of the todo to be searched.
   * @return response {@link ResponseEntity} which holds Http Status and body {@link TodoDto}.
   */
  @RequestMapping(method = RequestMethod.GET, value = "/{todo-id}")
  public ResponseEntity<TodoDto> getTodoById(@PathVariable(name = "todo-id") int todoId) {
    Optional<TodoDto> todoDto = todoService.getTodoById(todoId);
    if(todoDto.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(todoDto.get());
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  /**
   * API Method to update todo.
   *
   * @param todoId id of the todo to be updated.
   * @param updatedTodo instance of {@link TodoDto} holds data about todo to be updated.
   * @return response {@link ResponseEntity} which holds Http Status and body {@link TodoDto}.
   */
  @RequestMapping(method = RequestMethod.PATCH, value = "/{todo-id}")
  public ResponseEntity<TodoDto> updateTodo(@PathVariable(name = "todo-id") int todoId,
              @RequestBody final TodoDto updatedTodo) {
    Optional<TodoDto> todoDto = todoService.updateTodoById(todoId, updatedTodo);
    if(todoDto.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(todoDto.get());
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

  /**
   * API Method to delete todo for provided todoId.
   *
   * @param todoId id of the todo to be deleted.
   * @return response {@link ResponseEntity} which holds Http Status and message body.
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/{todo-id}")
  public ResponseEntity<String> deleteTodoById(@PathVariable(name = "todo-id") int todoId) {
    if(todoService.deleteTodoById(todoId)) {
      return ResponseEntity.status(HttpStatus.OK).body("todo with id "+todoId+" deleted.");
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("todo with id "+todoId+" does not exist.");
  }

  /**
   * API Method to delete/clear all isCompleted todos.
   *
   * @return response {@link ResponseEntity} which holds Http Status and message body.
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/clear-isCompleted-todos")
  public ResponseEntity<String> deleteAllCompletedTodos() {
    long count = todoService.deleteAllCompletedTodos();
    return ResponseEntity.status(HttpStatus.OK).body("Total " +count+" todos were isCompleted are deleted.");
  }
}