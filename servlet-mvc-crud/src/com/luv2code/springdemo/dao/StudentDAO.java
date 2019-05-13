package com.luv2code.springdemo.dao;

import java.sql.SQLException;
import java.util.List;

import com.luv2code.springdemo.entity.Student;

public interface StudentDAO {

	public List<Student> getStudents() throws SQLException;

	public void saveStudent(Student theStudent) throws SQLException;

	public Student getStudent(int theId) throws SQLException, Exception;

	public void deleteStudent(int theId) throws SQLException;

	public void updateStudent(Student theStudent) throws SQLException;
	
}
