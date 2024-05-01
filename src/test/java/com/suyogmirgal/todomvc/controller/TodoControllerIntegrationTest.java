package com.suyogmirgal.todomvc.controller;

import com.suyogmirgal.todomvc.model.TodoDto;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * This is integration test class for {@link TodoController}
 *
 * @author suyogmirgal
 * created on 2024/05/01
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class TodoControllerIntegrationTest {

  @LocalServerPort
  private int port;

  private RestTemplate restTemplate;

  private String apiBaseUrl;

  static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  @PostConstruct
  void init() {
    apiBaseUrl = "http://localhost:" + port;
    restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
  }

  /**
   * This is the integration test to verify create todo API.
   *
   */
  @Test
  public void testCreateTodo() {

    String todoCreateRequestJson = "{ \"title\" : \"Go to Gym\", \"order\" : 1 }";

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<TodoDto> entity = new HttpEntity(todoCreateRequestJson, headers);

    LocalDateTime dateTimeJustBeforeTodoCreate = format(LocalDateTime.now());

    ResponseEntity<TodoDto> responseEntity = restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.POST, entity, TodoDto.class);

    LocalDateTime dateTimeJustAfterTodoCreate = format(LocalDateTime.now());

    Assertions.assertEquals(201, responseEntity.getStatusCode().value());
    TodoDto todoResponse = responseEntity.getBody();
    Assertions.assertEquals("Go to Gym", todoResponse.getTitle());
    Assertions.assertEquals(1, todoResponse.getOrder());
    Assertions.assertFalse(todoResponse.isCompleted());
    assertTodoCreatedDateTime(todoResponse.getCreatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);
    assertTodoUpdatedDateTime(todoResponse.getUpdatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);
  }

  /**
   * This is the integration test to verify get all todo API.
   *
   */
  @Test
  public void testGetAllTodo() {

    String todoCreateRequestJson1 = "{ \"title\" : \"Eat breakfast\", \"order\" : 1 }";
    String todoCreateRequestJson2 = "{ \"title\" : \"Wash Clothes\", \"order\" : 2 }";

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<TodoDto> entity1 = new HttpEntity(todoCreateRequestJson1, headers);
    HttpEntity<TodoDto> entity2 = new HttpEntity(todoCreateRequestJson2, headers);

    LocalDateTime dateTimeJustBeforeTodoCreate = format(LocalDateTime.now());

    restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.POST, entity1, TodoDto.class);
    restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.POST, entity2, TodoDto.class);

    LocalDateTime dateTimeJustAfterTodoCreate = format(LocalDateTime.now());

    ResponseEntity<List<TodoDto>> responseEntity = restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.GET,
        null, new ParameterizedTypeReference<List<TodoDto>>() {});


    Assertions.assertEquals(200, responseEntity.getStatusCode().value());
    List<TodoDto> todoResponseList = responseEntity.getBody();
    Assertions.assertEquals(2, todoResponseList.size());

    TodoDto todoResponse1 = responseEntity.getBody().get(0);
    Assertions.assertEquals("Eat breakfast", todoResponse1.getTitle());
    Assertions.assertEquals(1, todoResponse1.getOrder());
    Assertions.assertFalse(todoResponse1.isCompleted());
    assertTodoCreatedDateTime(todoResponse1.getCreatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);
    assertTodoUpdatedDateTime(todoResponse1.getUpdatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);

    TodoDto todoResponse2 = responseEntity.getBody().get(1);
    Assertions.assertEquals("Wash Clothes", todoResponse2.getTitle());
    Assertions.assertEquals(2, todoResponse2.getOrder());
    Assertions.assertFalse(todoResponse2.isCompleted());
    assertTodoCreatedDateTime(todoResponse2.getCreatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);
    assertTodoUpdatedDateTime(todoResponse2.getUpdatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);
  }

  /**
   * This is the integration test to verify get all todo API.
   *
   */
  @Test
  public void testGetAllActiveTodo() {

    String todoCreateRequestJson1 = "{ \"title\" : \"Eat breakfast\", \"order\" : 1, \"isCompleted\" : false}";
    String todoCreateRequestJson2 = "{ \"title\" : \"Wash Clothes\", \"order\" : 2 , \"isCompleted\" : true }";

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<TodoDto> entity1 = new HttpEntity(todoCreateRequestJson1, headers);
    HttpEntity<TodoDto> entity2 = new HttpEntity(todoCreateRequestJson2, headers);

    LocalDateTime dateTimeJustBeforeTodoCreate = format(LocalDateTime.now());

    restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.POST, entity1, TodoDto.class);
    restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.POST, entity2, TodoDto.class);

    LocalDateTime dateTimeJustAfterTodoCreate = format(LocalDateTime.now());

    ResponseEntity<List<TodoDto>> responseEntity = restTemplate.exchange(apiBaseUrl + "/todo/active", HttpMethod.GET,
        null, new ParameterizedTypeReference<List<TodoDto>>() {});


    Assertions.assertEquals(200, responseEntity.getStatusCode().value());
    List<TodoDto> todoResponseList = responseEntity.getBody();
    Assertions.assertEquals(1, todoResponseList.size());

    TodoDto todoResponse1 = responseEntity.getBody().get(0);
    Assertions.assertEquals("Eat breakfast", todoResponse1.getTitle());
    Assertions.assertEquals(1, todoResponse1.getOrder());
    Assertions.assertFalse(todoResponse1.isCompleted());
    assertTodoCreatedDateTime(todoResponse1.getCreatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);
    assertTodoUpdatedDateTime(todoResponse1.getUpdatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);
  }

  /**
   * This is the integration test to verify get todo by todoId API.
   *
   */
  @Test
  public void testGetTodoByTodoId() {

    String todoCreateRequestJson = "{ \"title\" : \"Eat breakfast\", \"order\" : 1 }";

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<TodoDto> entity = new HttpEntity(todoCreateRequestJson, headers);

    LocalDateTime dateTimeJustBeforeTodoCreate = format(LocalDateTime.now());

    ResponseEntity<TodoDto> todoCreateResponseEntity = restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.POST, entity, TodoDto.class);
    int todoId = todoCreateResponseEntity.getBody().getId();

    LocalDateTime dateTimeJustAfterTodoCreate = format(LocalDateTime.now());

    ResponseEntity<TodoDto> responseEntity = restTemplate.exchange(apiBaseUrl + "/todo/"+todoId, HttpMethod.GET,
        null, TodoDto.class);


    Assertions.assertEquals(200, responseEntity.getStatusCode().value());

    TodoDto todoResponse = responseEntity.getBody();
    Assertions.assertEquals("Eat breakfast", todoResponse.getTitle());
    Assertions.assertEquals(todoId, todoResponse.getId());
    Assertions.assertEquals(1, todoResponse.getOrder());
    Assertions.assertFalse(todoResponse.isCompleted());
    assertTodoCreatedDateTime(todoResponse.getCreatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);
    assertTodoUpdatedDateTime(todoResponse.getUpdatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);
  }

  /**
   * This is the integration test to verify get todo by todoId API
   * for toto which does not exist.
   *
   */
  @Test
  public void testGetTodoByTodoIdForToDoWhichNotExist() {

    ResponseEntity responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(apiBaseUrl + "/todo/999", HttpMethod.GET,null, Object.class);
    } catch (HttpClientErrorException ex) {
      responseEntity = new ResponseEntity<>(ex.getStatusCode());
    }

    Assertions.assertEquals(404, responseEntity.getStatusCode().value());
  }

  /**
   * This is the integration test to verify update/patch todo by todoId API.
   *
   */
  @Test
  public void testUpdateTodo() {

    String todoCreateRequestJson = "{ \"title\" : \"Eat breakfast\", \"order\" : 1 }";

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<TodoDto> entity = new HttpEntity(todoCreateRequestJson, headers);

    ResponseEntity<TodoDto> todoCreateResponseEntity = restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.POST, entity, TodoDto.class);
    int todoId = todoCreateResponseEntity.getBody().getId();

    String todoUpdateRequestJson = "{ \"title\" : \"Eat healthy breakfast\",  \"isCompleted\" : true }";

    HttpEntity<TodoDto> updatedEntity = new HttpEntity(todoUpdateRequestJson, headers);

    LocalDateTime dateTimeJustBeforeTodoUpdate = format(LocalDateTime.now());

    //update patch todo
    restTemplate.exchange(apiBaseUrl + "/todo/"+todoId, HttpMethod.PATCH, updatedEntity, TodoDto.class);

    LocalDateTime dateTimeJustAfterTodoUpdate = format(LocalDateTime.now());

    //retrieve todo after update/patch is isCompleted
    ResponseEntity<TodoDto> responseEntity = restTemplate.exchange(apiBaseUrl + "/todo/"+todoId, HttpMethod.GET,
        null, TodoDto.class);


    Assertions.assertEquals(200, responseEntity.getStatusCode().value());

    TodoDto todoResponse = responseEntity.getBody();
    Assertions.assertEquals("Eat healthy breakfast", todoResponse.getTitle());
    Assertions.assertTrue(todoResponse.isCompleted());
    assertTodoUpdatedDateTime(todoResponse.getUpdatedDate(), dateTimeJustBeforeTodoUpdate, dateTimeJustAfterTodoUpdate);
  }

  /**
   * This is the integration test to verify unsuccessful update/patch todo by todoId API
   * when todo with provided todoId does not exist.
   *
   */
  @Test
  public void testUnSuccessfulUpdateTodo() {

    String todoCreateRequestJson = "{ \"title\" : \"Eat breakfast\", \"order\" : 1 }";

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<TodoDto> entity = new HttpEntity(todoCreateRequestJson, headers);

    ResponseEntity<TodoDto> todoCreateResponseEntity = restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.POST, entity, TodoDto.class);
    int todoId = todoCreateResponseEntity.getBody().getId();

    String todoUpdateRequestJson = "{ \"title\" : \"Eat healthy breakfast\",  \"isCompleted\" : true }";

    HttpEntity<TodoDto> updatedEntity = new HttpEntity(todoUpdateRequestJson, headers);

    ResponseEntity responseEntity = null;
    try {
      //update patch todo by providing todoId (999) that does not exist
      restTemplate.exchange(apiBaseUrl + "/todo/999", HttpMethod.PATCH, updatedEntity, TodoDto.class);

    } catch (HttpClientErrorException ex) {
      responseEntity = new ResponseEntity<>(ex.getStatusCode());
    }

    Assertions.assertEquals(400, responseEntity.getStatusCode().value());
  }

  /**
   * This is the integration test to verify successful delete of todo
   * for provided totoId.
   *
   */
  @Test
  public void testSuccessfulDeleteTodoByTodoId() {

    String todoCreateRequestJson = "{ \"title\" : \"Go to Gym\", \"order\" : 1 }";

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<TodoDto> entity = new HttpEntity(todoCreateRequestJson, headers);

    //Create todo
    ResponseEntity<TodoDto> responseEntity = restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.POST, entity, TodoDto.class);
    long todoId = responseEntity.getBody().getId();

    ResponseEntity<String> responseEntityForDelete = restTemplate.exchange(apiBaseUrl + "/todo/"+todoId, HttpMethod.DELETE, null, String.class);

    Assertions.assertEquals(200, responseEntityForDelete.getStatusCode().value());
    Assertions.assertEquals("todo with id "+todoId+" deleted.", responseEntityForDelete.getBody());
  }

  /**
   * This is the integration test to verify unsuccessful delete of todo
   * for provided totoId which does not exist
   *
   */
  @Test
  public void testUnSuccessfulDeleteTodoByTodoId() {

    ResponseEntity responseEntity = null;
    try {
      //calling delete todo by providing todoId (999) that does not exist
      restTemplate.exchange(apiBaseUrl + "/todo/999", HttpMethod.DELETE, null, String.class);

    } catch (HttpClientErrorException ex) {
      responseEntity = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
    }

    Assertions.assertEquals(400, responseEntity.getStatusCode().value());
    Assertions.assertEquals("todo with id 999 does not exist.", responseEntity.getBody());
  }

  /**
   * This is the integration test to verify delete/clear all completed todo API.
   *
   */
  @Test
  public void testClearCompletedTodo() {

    String todoCreateRequestJson1 = "{ \"title\" : \"Eat breakfast\", \"order\" : 1, \"isCompleted\" : true }";
    String todoCreateRequestJson2 = "{ \"title\" : \"Wash Clothes\", \"order\" : 2, \"isCompleted\" : false }";
    String todoCreateRequestJson3 = "{ \"title\" : \"Cook Lunch\", \"order\" : 3, \"isCompleted\" : false }";

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<TodoDto> entity1 = new HttpEntity(todoCreateRequestJson1, headers);
    HttpEntity<TodoDto> entity2 = new HttpEntity(todoCreateRequestJson2, headers);
    HttpEntity<TodoDto> entity3 = new HttpEntity(todoCreateRequestJson3, headers);

    LocalDateTime dateTimeJustBeforeTodoCreate = format(LocalDateTime.now());

    //create all todos
    restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.POST, entity1, TodoDto.class);
    restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.POST, entity2, TodoDto.class);
    restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.POST, entity3, TodoDto.class);

    LocalDateTime dateTimeJustAfterTodoCreate = format(LocalDateTime.now());

    //delete all completed todos
    ResponseEntity<String> deleteAllCompletedResponse = restTemplate.exchange(apiBaseUrl + "/todo/clear-isCompleted-todos", HttpMethod.DELETE,
        null, String.class);

    Assertions.assertEquals(200, deleteAllCompletedResponse.getStatusCode().value());
    Assertions.assertEquals("Total 1 todos were isCompleted are deleted.", deleteAllCompletedResponse.getBody());


    //retrieve all todos after deletion of completed todos
    ResponseEntity<List<TodoDto>> responseEntity = restTemplate.exchange(apiBaseUrl + "/todo", HttpMethod.GET,
        null, new ParameterizedTypeReference<List<TodoDto>>() {});


    Assertions.assertEquals(200, responseEntity.getStatusCode().value());
    List<TodoDto> todoResponseList = responseEntity.getBody();
    Assertions.assertEquals(2, todoResponseList.size());

    TodoDto todoResponse1 = responseEntity.getBody().get(0);
    Assertions.assertEquals("Wash Clothes", todoResponse1.getTitle());
    Assertions.assertEquals(2, todoResponse1.getOrder());
    Assertions.assertFalse(todoResponse1.isCompleted());
    assertTodoCreatedDateTime(todoResponse1.getCreatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);
    assertTodoUpdatedDateTime(todoResponse1.getUpdatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);

    TodoDto todoResponse2 = responseEntity.getBody().get(1);
    Assertions.assertEquals("Cook Lunch", todoResponse2.getTitle());
    Assertions.assertEquals(3, todoResponse2.getOrder());
    Assertions.assertFalse(todoResponse2.isCompleted());
    assertTodoCreatedDateTime(todoResponse2.getCreatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);
    assertTodoUpdatedDateTime(todoResponse2.getUpdatedDate(), dateTimeJustBeforeTodoCreate, dateTimeJustAfterTodoCreate);

  }

  /**
   * This method checks if todo created datetime is equal or after datetime recorded just before todocreate request.
   *  also it checks if todo created datetime is equal or before datetime recorded just after todocreate request isCompleted.
   *
   * @param createdDateTime created date time for todo.
   * @param dateTimeJustBeforeTodoCreate date time recorded just before create request was sent.
   * @param dateTimeJustAfterTodoCreate date time recorded just after create request was isCompleted.
   */
  private void assertTodoCreatedDateTime(LocalDateTime createdDateTime, LocalDateTime dateTimeJustBeforeTodoCreate,
      LocalDateTime dateTimeJustAfterTodoCreate) {
    Assertions.assertTrue((createdDateTime.isEqual(dateTimeJustBeforeTodoCreate) ||
        createdDateTime.isAfter(dateTimeJustBeforeTodoCreate)) &&
        (createdDateTime.isEqual(dateTimeJustAfterTodoCreate) ||
            createdDateTime.isBefore(dateTimeJustAfterTodoCreate)));
  }

  /**
   * This method checks if todo updated datetime is equal or after datetime recorded just before todo update request.
   *  also it checks if todo updated datetime is equal or before datetime recorded just after todo update request isCompleted.
   *
   * @param updatedDateTime updated date time for todo.
   * @param dateTimeJustBeforeTodoUpdate date time recorded just before update request was sent.
   * @param dateTimeJustAfterTodoUpdate date time recorded just after update request was isCompleted.
   */
  private void assertTodoUpdatedDateTime(LocalDateTime updatedDateTime, LocalDateTime dateTimeJustBeforeTodoUpdate,
      LocalDateTime dateTimeJustAfterTodoUpdate) {
    Assertions.assertTrue((updatedDateTime.isEqual(dateTimeJustBeforeTodoUpdate) ||
        updatedDateTime.isAfter(dateTimeJustBeforeTodoUpdate)) &&
        (updatedDateTime.isEqual(dateTimeJustAfterTodoUpdate) ||
            updatedDateTime.isBefore(dateTimeJustAfterTodoUpdate)));
  }


  private LocalDateTime format(LocalDateTime dateTime) {
    return LocalDateTime.parse(dateTime.format(formatter), formatter);
  }
}