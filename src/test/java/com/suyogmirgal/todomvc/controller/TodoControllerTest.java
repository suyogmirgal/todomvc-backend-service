package com.suyogmirgal.todomvc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suyogmirgal.todomvc.model.TodoDto;
import com.suyogmirgal.todomvc.service.TodoService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * This is unit test class for {@link TodoController}
 *
 * @author suyogmirgal
 * created on 2024/05/01
 */
@WebMvcTest(TodoController.class)
public class TodoControllerTest {

  static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
  
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TodoService todoService;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * This is the unit test to verify create todo API.
   *
   * @throws Exception exception thrown if occurred while executing test.
   */
  @Test
  public void testCreateTodo() throws Exception {

    String todoCreateRequestJson = "{ \"title\" : \"Go to Gym\", \"order\" : 1 }";

    LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter); 
    Mockito.when(todoService.createTodo(Mockito.any(TodoDto.class))).
        thenReturn(new TodoDto(1, "Go to Gym", 1, false, now, now));

    mockMvc.perform(post("/todo").content(todoCreateRequestJson)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("Go to Gym"))
        .andExpect(jsonPath("$.order").value(1))
        .andExpect(jsonPath("$.isCompleted").value(false))
        .andExpect(jsonPath("$.createdDate").value(now.toString()))
        .andExpect(jsonPath("$.updatedDate").value(now.toString()));

    ArgumentCaptor<TodoDto> todoDtoArgumentCaptor = ArgumentCaptor.forClass(TodoDto.class);
    Mockito.verify(todoService).createTodo(todoDtoArgumentCaptor.capture());
    Assertions.assertEquals("Go to Gym", todoDtoArgumentCaptor.getValue().getTitle());
    Assertions.assertEquals(1, todoDtoArgumentCaptor.getValue().getOrder());
  }

  /**
   * This is the unit test to verify get all todos API.
   *
   * @throws Exception exception thrown if occurred while executing test.
   */
  @Test
  public void testGetAllTodo() throws Exception {

    LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter); 
    TodoDto todo1 = new TodoDto(1, "Go to Gym", 1, false, now, now);
    TodoDto todo2 = new TodoDto(2, "Eat breakfast", 2, false, now, now);

    Mockito.when(todoService.getAllTodos()).thenReturn(Arrays.asList(todo1, todo2));

    mockMvc.perform(get("/todo"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].title").value("Go to Gym"))
        .andExpect(jsonPath("$[0].order").value(1))
        .andExpect(jsonPath("$[0].isCompleted").value(false))
        .andExpect(jsonPath("$[0].createdDate").value(now.toString()))
        .andExpect(jsonPath("$[0].updatedDate").value(now.toString()))
        .andExpect(jsonPath("$[1].title").value("Eat breakfast"))
        .andExpect(jsonPath("$[1].order").value(2))
        .andExpect(jsonPath("$[1].isCompleted").value(false))
        .andExpect(jsonPath("$[1].createdDate").value(now.toString()))
        .andExpect(jsonPath("$[1].updatedDate").value(now.toString()));

  }

  /**
   * This is the unit test to verify get all active todos API.
   *
   * @throws Exception exception thrown if occurred while executing test.
   */
  @Test
  public void testGetAllActiveTodo() throws Exception {

    LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter); 
    TodoDto todo1 = new TodoDto(1, "Go to Gym", 1, false, now, now);

    Mockito.when(todoService.getAllActiveTodos()).thenReturn(Arrays.asList(todo1));

    mockMvc.perform(get("/todo/active"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].title").value("Go to Gym"))
        .andExpect(jsonPath("$[0].order").value(1))
        .andExpect(jsonPath("$[0].isCompleted").value(false))
        .andExpect(jsonPath("$[0].createdDate").value(now.toString()))
        .andExpect(jsonPath("$[0].updatedDate").value(now.toString()));

  }

  /**
   * This is the unit test to verify successful get todo by todoId API.
   *
   * @throws Exception exception thrown if occurred while executing test.
   */
  @Test
  public void testSuccessfulGetTodoByTodoId() throws Exception {

    LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter); 
    TodoDto todo = new TodoDto(1, "Go to Gym", 1, false, now, now);

    Mockito.when(todoService.getTodoById(1)).thenReturn(Optional.of(todo));

    mockMvc.perform(get("/todo/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("Go to Gym"))
        .andExpect(jsonPath("$.order").value(1))
        .andExpect(jsonPath("$.isCompleted").value(false))
        .andExpect(jsonPath("$.createdDate").value(now.toString()))
        .andExpect(jsonPath("$.updatedDate").value(now.toString()));

  }

  /**
   * This is the unit test to verify get todo by todoId API with
   * todo that does not exist for provided todoId.
   *
   * @throws Exception exception thrown if occurred while executing test.
   */
  @Test
  public void testNotFoundWhenGetTodoByTodoId() throws Exception {

    Mockito.when(todoService.getTodoById(1)).thenReturn(Optional.empty());

    mockMvc.perform(get("/todo/1"))
        .andExpect(status().isNotFound());
  }

  /**
   * This is the unit test to verify successful update todo by todoId API.
   *
   * @throws Exception exception thrown if occurred while executing test.
   */
  @Test
  public void testUpdateTodoByTodoId() throws Exception {

    LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter); 
    TodoDto todo = new TodoDto(1, "Go to Gym", 1, true, now, now);

    String todoUpdateRequestJson = "{ \"isCompleted\" : true }";

    Mockito.when(todoService.updateTodoById(Mockito.anyInt(), Mockito.any(TodoDto.class)))
        .thenReturn(Optional.of(todo));

    mockMvc.perform(patch("/todo/1").contentType(MediaType.APPLICATION_JSON)
        .content(todoUpdateRequestJson))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("Go to Gym"))
        .andExpect(jsonPath("$.order").value(1))
        .andExpect(jsonPath("$.isCompleted").value(true))
        .andExpect(jsonPath("$.createdDate").value(now.toString()))
        .andExpect(jsonPath("$.updatedDate").value(now.toString()));

    ArgumentCaptor<TodoDto> todoDtoArgumentCaptor = ArgumentCaptor.forClass(TodoDto.class);
    ArgumentCaptor<Integer> todoIdArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

    Mockito.verify(todoService).updateTodoById(todoIdArgumentCaptor.capture(), todoDtoArgumentCaptor.capture());

    Assertions.assertEquals(1, todoIdArgumentCaptor.getValue());

    Assertions.assertTrue(todoDtoArgumentCaptor.getValue().isCompleted());
  }

  /**
   * This is the unit test to verify successful delete todo by toId API.
   *
   * @throws Exception exception thrown if occurred while executing test.
   */
  @Test
  public void testSuccessfulDeleteTodoByTodoId() throws Exception {

    Mockito.when(todoService.deleteTodoById(1)).thenReturn(true);

    mockMvc.perform(delete("/todo/1"))
        .andExpect(status().isOk()).andExpect(content().string("todo with id 1 deleted."));
  }

  /**
   * This is the unit test to verify unsuccessful delete todo by toId API
   * when todo with provided todoid does not exist.
   *
   * @throws Exception exception thrown if occurred while executing test.
   */
  @Test
  public void testUnSuccessfulDeleteTodoByTodoId() throws Exception {

    Mockito.when(todoService.deleteTodoById(1)).thenReturn(false);

    mockMvc.perform(delete("/todo/1"))
        .andExpect(status().isBadRequest()).andExpect(content().string("todo with id 1 does not exist."));
  }

  /**
   * This is the unit test to verify successful delete all isCompleted todos API.
   *
   * @throws Exception exception thrown if occurred while executing test.
   */
  @Test
  public void testSuccessfulDeleteAllCompletedTodo() throws Exception {

    Mockito.when(todoService.deleteAllCompletedTodos()).thenReturn(2L);

    mockMvc.perform(delete("/todo/clear-isCompleted-todos"))
        .andExpect(status().isOk()).andExpect(content().string("Total 2 todos were isCompleted are deleted."));
  }

}