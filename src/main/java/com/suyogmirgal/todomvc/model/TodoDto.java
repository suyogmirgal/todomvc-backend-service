package com.suyogmirgal.todomvc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * This class instance is used as DTO which holds
 * the information about Todo
 *
 * @author suyogmirgal
 * created on 2024/04/29
 */
public class TodoDto {

  private final int id;
  private final String title;
  private final Integer order;
  private final boolean isCompleted;
  private final LocalDateTime createdDate;
  private final LocalDateTime updatedDate;

  /**
   * Parameterized constructor for {@link TodoDto}
   *
   * @param id id of the todo.
   * @param title title for the todo.
   * @param order order for the todo.
   * @param isCompleted flag which tells if todo is isCompleted or not.
   * @param createdDate date time on which todo item is created.
   * @param updatedDate date time on which tot item is updated.
   */
  public TodoDto(int id, String title, Integer order, boolean isCompleted,
      LocalDateTime createdDate, LocalDateTime updatedDate) {
    this.id = id;
    this.title = title;
    this.order = order;
    this.isCompleted = isCompleted;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
  }

  /**
   * Getter method to get id of the todo item.
   *
   * @return id for the todo item.
   */
  public int getId() {
    return id;
  }

  /**
   * Getter method to get title of the todo item.
   *
   * @return title for the todo item.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Getter method to get order of the todo item.
   *
   * @return order for the todo item.
   */
  public Integer getOrder() {
    return order;
  }

  /**
   * Getter method for flag isCompleted of the todo item.
   *
   * @return true if todo item is isCompleted else false.
   */
  @JsonProperty("isCompleted")
  public boolean isCompleted() {
    return isCompleted;
  }

  /**
   * Getter method to created date time for the todo item.
   *
   * @return LocalDateTime in the format '"yyyy-MM-dd'T'HH:mm:ss"' when todo is created.
   */
  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  /**
   * Getter method to updated date time for the todo item.
   *
   * @return LocalDateTime in the format '"yyyy-MM-dd'T'HH:mm:ss"' when todo is updated.
   */
  public LocalDateTime getUpdatedDate() {
    return updatedDate;
  }
}
