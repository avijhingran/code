package com.luv2code.springdemo.service;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.luv2code.springdemo.dao.StudentDAO;
import com.luv2code.springdemo.dao.StudentDAOImpl;
import com.luv2code.springdemo.entity.Student;

public class StudentServiceImpl implements StudentService {

	private StudentDAO studentDAO;
	
	public StudentServiceImpl(DataSource theDataSource) {
		studentDAO = new StudentDAOImpl(theDataSource);
	}
	
	@Override
	public List<Student> getStudents() throws SQLException {
		return studentDAO.getStudents();
	}

	@Override
	public void saveStudent(Student theStudent) throws SQLException {
		studentDAO.saveStudent(theStudent);
	}

	@Override
	public Student getStudent(int theId) throws SQLException {
		try {
			return studentDAO.getStudent(theId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deleteStudent(int theId) throws SQLException {
		studentDAO.deleteStudent(theId);
	}

	@Override
	public void updateStudent(Student theStudent) throws SQLException {
		studentDAO.updateStudent(theStudent);
		
	}

}
