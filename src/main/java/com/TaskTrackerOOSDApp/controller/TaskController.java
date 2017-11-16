package com.TaskTrackerOOSDApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.TaskTrackerOOSDApp.model.Task;
import com.TaskTrackerOOSDApp.service.TaskService;

//This class is the controller that maps user request URLS to java 
//methods by leveraging spring MVC
@Controller
public class TaskController {
	// auto-wiring task service is injecting taskService dependency into this
	// controller
	@Autowired
	TaskService taskService;

	// we are created an array task list and getting the content of the data for
	// service layer and passing it to the task page
	@RequestMapping(value = "/adminTasks")
	public ModelAndView viewTasks(ModelAndView model) {
		List<Task> taskList = taskService.retrieveAll();
		model.addObject("taskList", taskList);
		model.setViewName("adminTasks");
		return model;
	}

	// if admin user pushes create task button then it goes to task creation
	// page which would display task form.
	@RequestMapping(value = "/taskForm")
	public ModelAndView createTask(ModelAndView model) {
		Task task = new Task();
		model.addObject("task", task);
		model.setViewName("createTask");
		return model;
	}
	/*
	if admin pushes save task button then the task is created , populated
	into task list, finally admin is redirected to admin task page
	 */
	@RequestMapping(value = "/saveTask", method = RequestMethod.POST)
	public ModelAndView createTask(Task taskformobject) {
		ModelAndView model = null;
		taskService.createTask(taskformobject);
		model = new ModelAndView("redirect:/adminTasks");
		return model;

	}
	/*
	if employee logs into app they are redirected to employee task page
	which would display all tasks from database
	 */
	@RequestMapping(value = "/empTasks/{name}")
	public ModelAndView viewEmpTasks(ModelAndView model, @PathVariable String name ) {
		List<Task> taskList = taskService.retrieveAll();
		model.addObject("taskList", taskList);
		model.addObject(name);
		model.setViewName("empTasks");
		return model;

	}
	/*
	when employee selects a task it retrieves task from db, if unassigned it will show only one button " assign " ,
	if the task is assigned to a user - two bottons will appear "In-Progress" & "Completed"
	 */
	@RequestMapping(value = "/viewTask/{taskId}/{name}")
	public ModelAndView viewEmpTasks(ModelAndView model, @PathVariable Integer taskId, @PathVariable String name) {
		Task task = taskService.retrieveTask(taskId);
		model.addObject("task", task);
		model.addObject(name);
		model.setViewName("updateTask");
		return model;

	}
	/*
	when a user pushes either In-Prgress button or Completed button , the status of the task is
	updated in the db and reflected on task page & automatically redirects to task page
	 */
	@RequestMapping(value = "updateStatus/{taskId}/{status}/{name}", method=RequestMethod.GET)
	public ModelAndView updateStatus(@PathVariable Integer taskId, @PathVariable String status, @PathVariable String name) {
		taskService.updateStatus(taskId, status);
		return new ModelAndView("redirect:/empTasks/" + name);

	}
	/*
	when user pushes assign button it assigns that task to the user that is logged in.
	automatically redirects to task page
	 */
	@RequestMapping(value = "updateAssignedTo/{taskId}/{name}", method=RequestMethod.GET)
	public ModelAndView updateAssignedTo(@PathVariable Integer taskId, @PathVariable String name)  {
		taskService.updateAssignedTo(taskId, name);
		return new ModelAndView("redirect:/empTasks/" + name);

	}

}