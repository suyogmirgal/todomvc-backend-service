package com.suyogmirgal.todomvc.repository;

import com.suyogmirgal.todomvc.entity.TodoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is repository class which interacts with DB and
 * provides DB operations for Todo Entity.
 *
 * @author suyogmirgal
 * created on 2024/04/30
 */
@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Integer> {

  List<TodoEntity> findAllByOrderByOrderAsc();

  List<TodoEntity> findByIsCompletedOrderByOrderAsc(boolean isCompleted);

  long deleteByIsCompleted(boolean isCompleted);
}
