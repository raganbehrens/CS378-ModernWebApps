package assign.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

//import org.springframework.web.bind.annotation.RequestParam;

import assign.domain.Course;
import assign.domain.Courses;
import assign.domain.Project;
import assign.domain.Projects;
import assign.domain.Error;
import assign.services.EavesdropService;

@Path("/projects")
public class MyEavesdropResource {
	
	EavesdropService eavesdropService;
	
	public MyEavesdropResource() {
		this.eavesdropService = new EavesdropService();
	}
	
	@GET
	@Path("/helloworld")
	@Produces("text/html")
	public String helloWorld() {
		return "Hello world";		
	}
	
	@GET
	@Path("/helloeavesdrop")
	@Produces("text/html")
	public String helloEavesdrop() {
		return this.eavesdropService.getData();		
	}	
	
	@GET
	@Path("/{name}/meetings")
	@Produces("application/xml")
	public StreamingOutput getAllCourses(@PathParam("name") String projectName) throws Exception {
//		Course modernWebApps = new Course();
		final Project proj = new Project();
		ArrayList<String> years = new ArrayList<String>();

//		modernWebApps.setDepartment("CS");
//		modernWebApps.setName("Modern Web Applications");
		years = eavesdropService.getYears(projectName);
		if (years != null) {
			//proj.setName(projectName);
			proj.setLink(years);
		}
		else {
			final Error error = new Error();
			error.setMessage(projectName);
		    return new StreamingOutput() {
		         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
		            outputCourses(outputStream, error);
		         }
		      };
		}
//		Course operatingSystems = new Course();
//		operatingSystems.setDepartment("CS");
//		operatingSystems.setName("Operating Systems");
		
		final Courses courses = new Courses();
//		List<Course> courseList = new ArrayList<Course>();
//		courseList.add(modernWebApps);
//		courseList.add(operatingSystems);
//		courses.setCourses(courseList);		
			    
	    return new StreamingOutput() {
	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
	            outputCourses(outputStream, proj);
	         }
	      };	    
	}
	
//	@GET
//	@Path("/projects")
//	@Produces("application/xml")
//	public StreamingOutput getAllProjects() throws Exception {
//		Project heat = new Project();
//		heat.setName("%23heat");
//				
//		final Projects projects = new Projects();
//		projects.setProjects(new ArrayList<String>());
//		projects.getProjects().add("%23heat");
//		projects.getProjects().add("%23dox");		
//			    
//	    return new StreamingOutput() {
//	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
//	            outputCourses(outputStream, projects);
//	         }
//	      };	    
//	}	
	
	// Lists all projects
	@GET
	@Path("/")
	@Produces("application/xml")
	public StreamingOutput getProjects() throws Exception {
		//final Project proj = new Project();
		//heat.setName("%23heat");
		//heat.setLink(new ArrayList<String>());
		//heat.getLink().add("l1");
		//heat.getLink().add("l2");	
		final Projects projects = new Projects();
		projects.setProjects(eavesdropService.listAll());
			    
	    return new StreamingOutput() {
	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
	            outputCourses(outputStream, projects);
	         }
	      };	    
	}		
	
	protected void outputCourses(OutputStream os, Error error) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Error.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(error, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	
	protected void outputCourses(OutputStream os, Projects projects) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Projects.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(projects, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}	
	
	protected void outputCourses(OutputStream os, Project project) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(project, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
}