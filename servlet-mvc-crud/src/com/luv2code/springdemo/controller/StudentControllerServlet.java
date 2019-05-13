package com.luv2code.springdemo.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.luv2code.springdemo.entity.Student;
import com.luv2code.springdemo.service.StudentService;
import com.luv2code.springdemo.service.StudentServiceImpl;

@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentService studentService;
	
	// Define DataSource a.k.a Connection Pool for Resource Injection
	// (Using this, Tomcat will set the connection pool on the Servlet Code)
	@Resource(name="jdbc/servlet-mvc-crud")
	private DataSource dataSource;
	
	
	@Override
	public void init() throws ServletException {
		super.init();

		// Create an instance of StudentService and pass the Connection pool
		try {
			studentService = new StudentServiceImpl(dataSource);
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// List the students in MVC fashion
		try {
			
			// Read the "command" parameter
			String theCommand = request.getParameter("command");
			
			// If the command is missing, then default to listing students
			if(theCommand == null)
				theCommand = "LIST";
			
			// Route to the appropriate method
			switch(theCommand) {

			case "LIST":
				listStudents(request, response);
				break;
			
			case "LOAD":
				loadStudent(request, response);
				break;
				
			case "UPDATE":
				updateStudent(request, response);
				break;
				
			case "DELETE":
				deleteStudent(request, response);
				break;
				
			default:
				listStudents(request, response);
			}
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// Read the "command" parameter
			String theCommand = request.getParameter("command");
			
			// Route to the appropriate method
			switch(theCommand) {

			case "ADD":
				addStudent(request, response);
				break;
			
			default:
				listStudents(request, response);
			}
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
	}
	

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		// Read the student info from the Form data
		String theStudentId = request.getParameter("studentId");

		// Convert Student ID to int and Delete the student in the database
		studentService.deleteStudent(Integer.parseInt(theStudentId));

		// Send back to the main page (Student List)
		listStudents(request, response);
	}


	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		// Read the student info from the Form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// Create a new Student object
		Student theStudent = new Student(id, firstName, lastName, email);

		// Update the student in the database
		studentService.updateStudent(theStudent);

		// Send back to the main page (Student List)
		listStudents(request, response);
	}


	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		// Getting the student ID from Form Data.
		String theStudentId = request.getParameter("studentId");
		
		// Get student from database
		Student theStudent = studentService.getStudent(Integer.parseInt(theStudentId));
		
		// Place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		// Send to update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp"); 
		dispatcher.forward(request, response);
	}


	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// Read the student info from the Form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// Create a new Student object
		Student theStudent = new Student(firstName, lastName, email);
		
		// Save the student to the database
		studentService.saveStudent(theStudent);
		
		// Send back to the main page (Student List)
		// Here, we are using redirect to avoid adding duplicate student on clicking refresh button
		response.sendRedirect(request.getContextPath() +
				"/StudentControllerServlet?command=LIST");
	}


	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		// Get students
		List<Student> students = studentService.getStudents();
		
		// Add students to the Request
		request.setAttribute("STUDENT_LIST", students);
		
		// Send to JSP page (View)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp"); 
		dispatcher.forward(request, response);
	}


}
