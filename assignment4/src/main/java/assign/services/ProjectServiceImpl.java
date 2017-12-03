package assign.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import assign.domain.Course;
import assign.domain.NewProject;

public class ProjectServiceImpl implements ProjectService {

	String dbURL = "";
	String dbUsername = "";
	String dbPassword = "";
	DataSource ds;

	// DB connection information would typically be read from a config file.
	public ProjectServiceImpl(String dbUrl, String username, String password) {
		this.dbURL = dbUrl;
		this.dbUsername = username;
		this.dbPassword = password;
		
		ds = setupDataSource();
	}
	
	public DataSource setupDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUsername(this.dbUsername);
        ds.setPassword(this.dbPassword);
        ds.setUrl(this.dbURL);
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        return ds;
    }
	
	public NewProject addProject(NewProject c) throws Exception {
		Connection conn = ds.getConnection();
		
		String insert = "INSERT INTO projects(name, description) VALUES(?, ?)";
		PreparedStatement stmt = conn.prepareStatement(insert,
                Statement.RETURN_GENERATED_KEYS);
		if (c.getName() == null || c.getDescription() == null)
			return null;
		if (c.getName().trim() == "" || c.getDescription().trim() == "") {
			System.out.println("bad request");
			return null;
		}
		stmt.setString(1, c.getName());
		stmt.setString(2, c.getDescription());
		
		int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating course failed, no rows affected.");
        }
        
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
        	c.setProjectId(generatedKeys.getInt(1));
        }
        else {
            throw new SQLException("Creating course failed, no ID obtained.");
        }
        
        // Close the connection
        conn.close();
        
		return c;
	}

	public NewProject getProject(int projectId) throws Exception {
		String query = "select * from projects where project_id=" + projectId;
		Connection conn = ds.getConnection();
		PreparedStatement s = conn.prepareStatement(query);
		ResultSet r = s.executeQuery();
		
		if (!r.next()) {
			return null;
		}
		
		NewProject c = new NewProject();
		c.setDescription(r.getString("description"));
		c.setName(r.getString("name"));
		c.setProjectId(r.getInt("project_id"));
		return c;
	}
	
	public NewProject updateProject(int projectId, NewProject c) throws Exception {
		
		if (c.getName() == null || c.getDescription() == null)
			return null;
		if (c.getName().trim() == "" || c.getDescription().trim() == "") {
			System.out.println("bad request");
			return null;
		}
		String query = "update projects set name = \'" + c.getName() + "\', description = \'" + c.getDescription() + "\' where project_id=" + projectId;
		//System.out.println(query);
		Connection conn = ds.getConnection();
		PreparedStatement s = conn.prepareStatement(query);
		int r = s.executeUpdate();
		
//		if (!r.next()) {
//			return null;
//		}
//		
//		NewProject c = new NewProject();
//		c.setDescription(r.getString("description"));
//		c.setName(r.getString("name"));
//		c.setProjectId(r.getInt("project_id"));
		return c;
	}
	
	public int deleteProject(int projectId) throws Exception {
		String query = "delete from projects where project_id = " + projectId;
		Connection conn = ds.getConnection();
		PreparedStatement s = conn.prepareStatement(query);
		int r = s.executeUpdate();
		if (r == 1)
			return 200;
		else
			return 404;
	}

    public NewProject getProject_correct(int courseId) throws Exception {
	String query = "select * from courses where course_id=?";
	Connection conn = ds.getConnection();
	PreparedStatement s = conn.prepareStatement(query);
	s.setString(1, String.valueOf(courseId));

	ResultSet r = s.executeQuery();
	
	if (!r.next()) {
	    return null;
	}
	
	NewProject c = new NewProject();
	c.setDescription(r.getString("course_num"));
	c.setName(r.getString("name"));
	c.setProjectId(r.getInt("course_id"));
	return c;
    }

}
