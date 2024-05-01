package com.suyogmirgal.todomvc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * This is entity class for Todo which
 * will be used to map and hold todo
 * record from database from todo table
 *
 * @author suyogmirgal
 * created on 2024/04/29
 */
@Entity
@Table(name = "todo")
@EntityListeners(AuditingEntityListener.class)
public class TodoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private int id;

  @Column(name = "title")
  private String title;

  @Column(name = "todo_order")
  private int order;

  @Column(name = "is_completed")
  private boolean isCompleted;

  @Column(name = "created_date")
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  private LocalDateTime updatedDate;

  /**
   * Getter method to get id of the todo record.
   *
   * @return id for the todo record.
   */
  public int getId() {
    return id;
  }

  /**
   * Setter method to get id of the todo.
   *
   * @param id id for the todo.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Getter method to get title of the todo.
   *
   * @return title for the todo.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Setter method to get title of the todo.
   *
   * @param title title for the todo.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Getter method to get order of the todo.
   *
   * @return order for the todo.
   */
  public int getOrder() {
    return order;
  }

  /**
   * Setter method to get order of the todo.
   *
   * @param order order for the todo.
   */
  public void setOrder(int order) {
    this.order = order;
  }

  /**
   * Getter method for flag isCompleted of the todo.
   *
   * @return true if todo is isCompleted else false.
   */
  public boolean isCompleted() {
    return isCompleted;
  }

  /**
   * Setter method for flag isCompleted of the todo.
   *
   * @param completed true if todo is isCompleted else false.
   */
  public void setCompleted(boolean completed) {
    isCompleted = completed;
  }

  /**
   * Getter method to get created date time for the todo.
   *
   * @return LocalDateTime.
   */
  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  /**
   * Setter method to created date time for the todo.
   *
   * @param createdDate LocalDateTime when toto is created.
   */
  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * Getter method to updated date time for the todo.
   *
   * @return LocalDateTime.
   */
  public LocalDateTime getUpdatedDate() {
    return updatedDate;
  }

  /**
   * Getter method to get updated date time for the todo.
   *
   * @param updatedDate LocalDateTime when todo is updated.
   */
  public void setUpdatedDate(LocalDateTime updatedDate) {
    this.updatedDate = updatedDate;
  }
}
