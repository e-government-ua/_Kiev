package org.activiti.rest.controller;

/**
 * User: goodg_000
 * Date: 19.08.2015
 * Time: 21:06
 */
public class TaskNotFoundException extends Exception {

   public TaskNotFoundException() {
      super("Task not found");
   }
}
