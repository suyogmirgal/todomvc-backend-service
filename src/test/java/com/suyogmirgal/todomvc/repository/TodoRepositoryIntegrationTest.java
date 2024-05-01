package com.suyogmirgal.todomvc.repository;

import com.suyogmirgal.todomvc.entity.TodoEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * This is integration test class for {@link TodoRepository}
 *
 * @author suyogmirgal
 * created on 2024/05/01
 */
@DataJpaTest
public class TodoRepositoryIntegrationTest {

  static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  @Autowired
  private TodoRepository todoRepository;

  /**
   * This test verifies retrieval of all orders in ascending order of Order.
   */
  @Test
  public void testFindAllByOrderByOrderAsc() {
    LocalDateTime dateTime = format(LocalDateTime.now());

    TodoEntity todoEntity1 = new TodoEntity();
    todoEntity1.setOrder(1);
    todoEntity1.setCompleted(false);
    todoEntity1.setTitle("Go to Gym");
    todoEntity1.setCreatedDate(dateTime);
    todoEntity1.setUpdatedDate(dateTime);

    TodoEntity todoEntity2 = new TodoEntity();
    todoEntity2.setOrder(2);
    todoEntity2.setCompleted(false);
    todoEntity2.setTitle("Eat breakfast");
    todoEntity2.setCreatedDate(dateTime);
    todoEntity2.setUpdatedDate(dateTime);

    todoRepository.save(todoEntity1);
    todoRepository.save(todoEntity2);

    List<TodoEntity> TodoEntityList = todoRepository.findAllByOrderByOrderAsc();

    Assertions.assertEquals(2, TodoEntityList.size());

    Assertions.assertTrue(TodoEntityList.get(0).getId() > 0);
    Assertions.assertEquals(1, TodoEntityList.get(0).getOrder());
    Assertions.assertEquals("Go to Gym", TodoEntityList.get(0).getTitle());
    Assertions.assertFalse(TodoEntityList.get(0).isCompleted());
    Assertions.assertEquals(dateTime, TodoEntityList.get(0).getCreatedDate());
    Assertions.assertEquals(dateTime, TodoEntityList.get(0).getUpdatedDate());

    Assertions.assertTrue(TodoEntityList.get(1).getId() > 0);
    Assertions.assertEquals(2, TodoEntityList.get(1).getOrder());
    Assertions.assertEquals("Eat breakfast", TodoEntityList.get(1).getTitle());
    Assertions.assertFalse(TodoEntityList.get(1).isCompleted());
    Assertions.assertEquals(dateTime, TodoEntityList.get(1).getCreatedDate());
    Assertions.assertEquals(dateTime, TodoEntityList.get(1).getUpdatedDate());

  }

  /**
   * This test verifies retrieval of all completed orders in ascending order of Order.
   */
  @Test
  public void testFindByIsCompletedOrderByOrderAsc() {
    LocalDateTime dateTime = format(LocalDateTime.now());

    TodoEntity todoEntity1 = new TodoEntity();
    todoEntity1.setOrder(1);
    todoEntity1.setCompleted(true);
    todoEntity1.setTitle("Go to Gym");
    todoEntity1.setCreatedDate(dateTime);
    todoEntity1.setUpdatedDate(dateTime);

    TodoEntity todoEntity2 = new TodoEntity();
    todoEntity2.setOrder(2);
    todoEntity2.setCompleted(false);
    todoEntity2.setTitle("Eat breakfast");
    todoEntity2.setCreatedDate(dateTime);
    todoEntity2.setUpdatedDate(dateTime);

    TodoEntity todoEntity3 = new TodoEntity();
    todoEntity3.setOrder(3);
    todoEntity3.setCompleted(false);
    todoEntity3.setTitle("Read Story book");
    todoEntity3.setCreatedDate(dateTime);
    todoEntity3.setUpdatedDate(dateTime);

    todoRepository.save(todoEntity1);
    todoRepository.save(todoEntity2);
    todoRepository.save(todoEntity3);

    List<TodoEntity> todoEntityList = todoRepository.findByIsCompletedOrderByOrderAsc(false);

    Assertions.assertEquals(2, todoEntityList.size());

    Assertions.assertTrue(todoEntityList.get(0).getId() > 0);
    Assertions.assertEquals(2, todoEntityList.get(0).getOrder());
    Assertions.assertEquals("Eat breakfast", todoEntityList.get(0).getTitle());
    Assertions.assertFalse(todoEntityList.get(1).isCompleted());
    Assertions.assertEquals(dateTime, todoEntityList.get(0).getCreatedDate());
    Assertions.assertEquals(dateTime, todoEntityList.get(0).getUpdatedDate());

    Assertions.assertTrue(todoEntityList.get(1).getId() > 0);
    Assertions.assertEquals(3, todoEntityList.get(1).getOrder());
    Assertions.assertEquals("Read Story book", todoEntityList.get(1).getTitle());
    Assertions.assertFalse(todoEntityList.get(1).isCompleted());
    Assertions.assertEquals(dateTime, todoEntityList.get(1).getCreatedDate());
    Assertions.assertEquals(dateTime, todoEntityList.get(1).getUpdatedDate());

  }

  /**
   * This test verifies deletion of Completed todo.
   */
  @Test
  public void testDeleteByIsCompleted() {
    LocalDateTime dateTime = format(LocalDateTime.now());

    TodoEntity todoEntity1 = new TodoEntity();
    todoEntity1.setOrder(1);
    todoEntity1.setCompleted(true);
    todoEntity1.setTitle("Go to Gym");
    todoEntity1.setCreatedDate(dateTime);
    todoEntity1.setUpdatedDate(dateTime);

    TodoEntity todoEntity2 = new TodoEntity();
    todoEntity2.setOrder(2);
    todoEntity2.setCompleted(true);
    todoEntity2.setTitle("Eat breakfast");
    todoEntity2.setCreatedDate(dateTime);
    todoEntity2.setUpdatedDate(dateTime);

    TodoEntity todoEntity3 = new TodoEntity();
    todoEntity3.setOrder(3);
    todoEntity3.setCompleted(false);
    todoEntity3.setTitle("Read Story book");
    todoEntity3.setCreatedDate(dateTime);
    todoEntity3.setUpdatedDate(dateTime);

    todoRepository.save(todoEntity1);
    todoRepository.save(todoEntity2);
    todoRepository.save(todoEntity3);

    Assertions.assertTrue(todoRepository.deleteByIsCompleted(true) > 0);

    //retrieve completed todo after deletion
    List<TodoEntity> todoEntityList = todoRepository.findByIsCompletedOrderByOrderAsc(true);

    Assertions.assertEquals(0, todoEntityList.size());
  }

  private LocalDateTime format(LocalDateTime localDateTime) {
    return LocalDateTime.parse(localDateTime.format(formatter), formatter);
  }
}
