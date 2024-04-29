package com.suyogmirgal.todomvc.model;

import java.sql.Timestamp;

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
  private final int order;
  private final boolean isCompleted;
  private final Timestamp createdDate;
  private final Timestamp updatedDate;

  /**
   * Parameterized constructor for {@link TodoDto}
   *
   * @param id id of the todo.
   * @param title title for the todo.
   * @param order order for the todo.
   * @param isCompleted flag which tells if todo is completed or not.
   * @param createdDate date on which todo item is created.
   * @param updatedDate date on which tot item is updated.
   */
  public TodoDto(int id, String title, int order, boolean isCompleted, Timestamp createdDate,
      Timestamp updatedDate) {
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
  public int getOrder() {
    return order;
  }

  /**
   * Getter method for flag isCompleted of the todo item.
   *
   * @return true if todo item is completed else false.
   */
  public boolean isCompleted() {
    return isCompleted;
  }

  /**
   * Getter method to created date time stamp for the todo item.
   *
   * @return timestamp.
   */
  public Timestamp getCreatedDate() {
    return createdDate;
  }

  /**
   * Getter method to updated date time stamp for the todo item.
   *
   * @return timestamp.
   */
  public Timestamp getUpdatedDate() {
    return updatedDate;
  }
}
