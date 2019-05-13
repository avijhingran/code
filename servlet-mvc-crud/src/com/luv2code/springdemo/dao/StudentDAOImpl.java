package com.luv2code.springdemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.luv2code.springdemo.entity.Student;

public class StudentDAOImpl implements StudentDAO {
	
	private DataSource dataSource;

	public StudentDAOImpl(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	

	// Method to list the Students
	public List<Student> getStudents() throws SQLException{
		List<Student> students = new ArrayList<>();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			// Get a database connection
			con = dataSource.getConnection();
			
			// Create SQL Statements
			String sql = "select * from student ";
			st = con.createStatement();
			
			// Execute SQL Query
			rs = st.executeQuery(sql);
			
			// Process the result set
			if(rs.next()) {
				do {
					
					// Retrieve data from Result Set
					int id = rs.getInt("id");
					String firstName = rs.getString("first_name");
					String lastName = rs.getString("last_name");
					String email = rs.getString("email");
					
					// Create new student object
					Student tempStudent = new Student(id, firstName, lastName, email);
					
					// Add it to the list of students
					students.add(tempStudent);
				}while(rs.next());
			}
			
			return students;
		}
		finally {
			// Close JDBC Objects
			close(con, st, rs);
		}
	}


	private void close(Connection con, Statement st, ResultSet rs) {
		try {
			if(con != null)
				con.close();
			
			if(st != null)
				st.close();

			if(rs != null)
				rs.close();
		}

		catch (Exception e){
		
		}
	}


	public void saveStudent(Student theStudent) throws SQLException {
		
		Connection con = null;
		PreparedStatement st = null;
		
		try {
			// Get a database connection
			con = dataSource.getConnection();
						
			// Create SQL for insert
			String sql = "insert into student "
					   + "(first_name, last_name, email) "
					   + "values (?, ?, ?)";
			
			// Create prepared statement
			st = con.prepareStatement(sql);
			
			// Set the param value for the student
			st.setString(1, theStudent.getFirstName());
			st.setString(2, theStudent.getLastName());
			st.setString(3, theStudent.getEmail());
			
			// Execute SQL Statement
			st.execute();
			
		}
		finally {
			// Close JDBC Objects
			close(con, st, null);
		}
		
	}


	public Student getStudent(int theStudentId) throws Exception {
		Student theStudent = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			// Get a database connection
			con = dataSource.getConnection();
						
			// Create SQL to get the selected student
			String sql = "select * from student where id = ? ";

			// Create prepared statement	
			st = con.prepareStatement(sql);
			
			// Set the param values
			st.setInt(1, theStudentId);
			
			// Execute SQL Statement
			rs = st.executeQuery();
			
			// Retrieve data from result set row
			if(rs.next()) {
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");

				// Use this information to create a new student object
				theStudent = new Student(theStudentId, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find student id: " + theStudentId);
			}
			
			return theStudent;
		}
		finally {
			// Close JDBC Objects
			close(con, st, rs);
		}
		
	}


	public void updateStudent(Student theStudent) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;

		try {
			// Get a database connection
			con = dataSource.getConnection();
						
			// Create SQL for update
			String sql = "update student "
					   + "set first_name=?, last_name=?, email=? "
					   + "where id=? ";
			
			// Prepare statement
			st = con.prepareStatement(sql);
			
			// Set the param value for the student
			st.setString(1, theStudent.getFirstName());
			st.setString(2, theStudent.getLastName());
			st.setString(3, theStudent.getEmail());
			st.setInt(4, theStudent.getId());
			
			// Execute SQL Statement
			st.execute();
			
		}
		finally {
			// Close JDBC Objects
			close(con, st, null);
		}
	}


	public void deleteStudent(int theStudentId) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		
		try {
			
			// Get a database connection
			con = dataSource.getConnection();
						
			// Create SQL for delete
			String sql = "delete from student "
					   + "where id=? ";
			
			// Prepare statement
			st = con.prepareStatement(sql);
			
			// Set the param value for the student
			st.setInt(1, theStudentId);
			
			// Execute SQL Statement
			st.execute();
			
		}
		finally {
			// Close JDBC Objects
			close(con, st, null);
		}
		
	}

}