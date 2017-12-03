package assign.services;

import assign.domain.Course;
import assign.domain.NewProject;

public interface ProjectService {

	public NewProject addProject(NewProject c) throws Exception;
	
	public NewProject getProject(int projectId) throws Exception;
	
	public NewProject updateProject(int projectId, NewProject c) throws Exception;
	
	public int deleteProject(int projectId) throws Exception;

    public NewProject getProject_correct(int project_Id) throws Exception;

}
