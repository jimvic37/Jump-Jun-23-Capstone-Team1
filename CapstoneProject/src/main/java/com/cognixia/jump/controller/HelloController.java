package com.cognixia.jump.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {
	
	@GetMapping("/hello")
	public String getHello() {
		return "Hello World!";
	}
	
	@GetMapping("/admin")
	public String getAdmin() {
		return "You are an Admin!";
	}
	
	@GetMapping("/dev")
	public String getDev() {
		return "You are a Dev!";
	}
	
	@GetMapping("/usr")
	public String getUser() {
		return "You are a User!";
	}
	
	@GetMapping("/book") 
	public String getBook() {
		return "Book";
	}
	
	@GetMapping("/employee")
	public String getEmployee() {
		return "Employee";
	}
	
	@GetMapping("/employee/salary")
	public String getEmployeeSalary() {
		return "Salary";
	}
	
	@GetMapping("/employee/department")
	public String getEmployeeDept() {
		return "Department";
	}
	
	@PutMapping("/employee/department")
	public String updateDept() {
		return "Updated department";
	}
	
	@GetMapping("/all")
	public String getAll() {
		return "Accessible by all";
	}
	
	@GetMapping("/test")
	public String getTest() {
		return "Test";
	}
}