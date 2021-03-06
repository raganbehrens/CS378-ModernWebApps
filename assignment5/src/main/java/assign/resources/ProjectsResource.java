package assign.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import assign.domain.Project;
import assign.domain.Meeting;
import assign.services.DBLoader;

@Path("/projects")
public class ProjectsResource {

	DBLoader loader;
//	String password;
//	String username;
//	String dburl;
//	String dbhost, dbname;

	public ProjectsResource() {
//		dbhost = servletContext.getInitParameter("DBHOST");
//		dbname = servletContext.getInitParameter("DBNAME");
//		dburl = "jdbc:mysql://" + dbhost + ":3306/" + dbname;
//		username = servletContext.getInitParameter("DBUSERNAME");
//		password = servletContext.getInitParameter("DBPASSWORD");
		this.loader = new DBLoader();
	}

	@GET
	@Path("/helloworld")
	@Produces("text/html")
	public String helloWorld() {
//		System.out.println("Inside helloworld");
//		System.out.println("DB creds are:");
//		System.out.println("DBURL:" + dburl);
//		System.out.println("DBUsername:" + username);
//		System.out.println("DBPassword:" + password);
//		return "Hello world " + dburl + " " + username + " " + password;
		return ("Hello world");
	}


	@POST
	@Consumes("application/xml")
	public Response createProject(InputStream is) throws Exception {
		System.out.println("Creating Project");
		Project project = readProject(is);
		String projectName = project.getProjectName();
		String projectDescription = project.getDescription();
		//System.out.println("a" + projectDescription.trim() + "a");
		Long projectId = this.loader.addProject(projectName, projectDescription);
		//newProject = this.projectService.addProject(newProject);
//		if (newProject == null) {
//			//System.out.println("about to return bad response");
//			return Response.status(400).build();
//		}
		
		// TODO: Make sure description and name arent empty
		if (projectName == null || projectDescription == null)
			return Response.status(400).build();
		else if (projectName.trim().equals("") || projectDescription.trim().equals(""))
			return Response.status(400).build();
		return Response.created(URI.create("/projects/" + projectId)).build();
	}
	
	@POST
	@Path("/{projectId}/meetings")
	@Consumes("application/xml")
	public Response createMeeting(@PathParam("projectId") String projectId, InputStream is) throws Exception{
		Meeting meeting = readMeeting(is);
		String meetingName = meeting.getMeetingName();
		String meetingYear = meeting.getYear();
		if (meetingName == null || meetingYear == null || Integer.parseInt(meetingYear) < 2000 || Integer.parseInt(meetingYear) > 2017)
			return Response.status(400).build();
		else if (meetingName.trim().equals("") || meetingYear.trim().equals(""))
			return Response.status(400).build();
		Long meetingId = this.loader.addMeetingToProject(meetingName, Long.parseLong(projectId), meetingYear);
		
		// TODO: check if id exists return 404 if not
		// TODO: make sure year and name arent empty and year is between 2010-2017 return 400 if they are
		if (meetingId == null)
			return Response.status(404).build();

		return Response.created(URI.create("/projects/" + projectId + "meetings/" + meetingId)).build();
	}

	@GET
	@Path("/{id}")
	@Produces("application/xml")
	public Response getProject(@PathParam("id") String projectId) throws Exception {
//		Course modernWebApps = new Course();
//		modernWebApps.setDepartment("CS");
//		modernWebApps.setName("Modern Web Applications");
//
//		Course operatingSystems = new Course();
//		operatingSystems.setDepartment("CS");
//		operatingSystems.setName("Operating Systems");

//		final Courses courses = new Courses();
//		List<Course> courseList = new ArrayList<Course>();
//		courseList.add(modernWebApps);
//		courseList.add(operatingSystems);
//		courses.setCourses(courseList);

		final Project project = loader.getProject(Long.parseLong(projectId));
		if (project == null)
			return Response.status(404).build();

		StreamingOutput stream = new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputProject(outputStream, project);
			}
		};
		
		// TODO: return 404 if id doesnt exist
		return Response.ok(stream).build();
	}
//	
	@PUT
	@Path("/{projectId}/meetings/{meetingId}")
	@Consumes("application/xml")
	public Response updateProject(InputStream is, @PathParam("projectId") String projectId, @PathParam("meetingId") String meetingId) throws Exception {
//		Course modernWebApps = new Course();
//		modernWebApps.setDepartment("CS");
//		modernWebApps.setName("Modern Web Applications");
//
//		Course operatingSystems = new Course();
//		operatingSystems.setDepartment("CS");
//		operatingSystems.setName("Operating Systems");

//		final Courses courses = new Courses();
//		List<Course> courseList = new ArrayList<Course>();
//		courseList.add(modernWebApps);
//		courseList.add(operatingSystems);
//		courses.setCourses(courseList);
		Long pId = Long.parseLong(projectId);
		Long mId = Long.parseLong(meetingId);
		Meeting meeting = readMeeting(is);
		String newName = meeting.getMeetingName();
		String newYear = meeting.getYear();
		if (newName == null || newYear == null || Integer.parseInt(newYear) < 2000 || Integer.parseInt(newYear) > 2017)
			return Response.status(400).build();
		else if (newName.trim().equals("") || newYear.trim().equals(""))
			return Response.status(400).build();
		Long retId = loader.updateMeeting(pId, mId, newName, newYear);
		if (retId == null)
			return Response.status(404).build();
		//Project oldProject = projectService.getProject(id);
		//Project retProject = projectService.updateProject(id, newProject);
		
		
//		if (oldProject == null)
//			return Response.status(404).build();
//		else if (retProject == null)
//			return Response.status(400).build();
//		else
		
		// TODO: make sure year and name and year is between 2000-2017 and return 400 if they are
		// TODO: make sure id exists return 404 if doesnt
		return Response.ok().build();
		/*return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputProject(outputStream, project);
			}
		};*/
	}
//	
	@DELETE
	@Path("/{projectId}")
	public Response deleteProject(@PathParam("projectId") String projectId) throws Exception {
//		Course modernWebApps = new Course();
//		modernWebApps.setDepartment("CS");
//		modernWebApps.setName("Modern Web Applications");
//
//		Course operatingSystems = new Course();
//		operatingSystems.setDepartment("CS");
//		operatingSystems.setName("Operating Systems");

//		final Courses courses = new Courses();
//		List<Course> courseList = new ArrayList<Course>();
//		courseList.add(modernWebApps);
//		courseList.add(operatingSystems);
//		courses.setCourses(courseList);
		Long pId = Long.parseLong(projectId);
		//NewProject newProject = readNewProject(is);
		//NewProject oldProject = projectService.getProject(id);
		//NewProject retProject = projectService.updateProject(id, newProject);
		//int response = projectService.deleteProject(id);
		Object response = loader.deleteProject(pId);
		
		if (response == null)
			return Response.status(404).build();
		
		// TODO: if id doesnt exist return 404
		return Response.status(200).build();
//		else
//			return Response.status(404).build();
		/*return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputProject(outputStream, project);
			}
		};*/
	}
//
//	@GET
//	@Path("/projects")
//	@Produces("application/xml")
//	public StreamingOutput getAllProjects() throws Exception {
//		// Project heat = new Project();
//		// heat.setName("%23heat");
//
//		final Projects projects = new Projects();
//		projects.setProjects(new ArrayList<String>());
//		// projects.getProjects().add("%23heat");
//		projects.getProjects().add("%23dox");
//
//		return new StreamingOutput() {
//			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
//				outputCourses(outputStream, projects);
//			}
//		};
//	}
//
////	@GET
////	@Path("/project")
////	@Produces("application/xml")
////	public StreamingOutput getProject() throws Exception {
////
////		final Project heat = new Project();
////		heat.setName("%23heat");
////		heat.setLink(new ArrayList<String>());
////		heat.getLink().add("l3");
////		heat.getLink().add("l2");
////
////		// throw new WebApplicationException();
////
////		final NotFound notFound = new NotFound();
////		notFound.setError("Project non-existent-project does not exist");
////
////		return new StreamingOutput() {
////			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
////				outputCourses(outputStream, notFound);
////			}
////		};
////
////	}
//
//	protected void outputCourses(OutputStream os, Courses courses) throws IOException {
//		try {
//			JAXBContext jaxbContext = JAXBContext.newInstance(Courses.class);
//			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
//			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//			jaxbMarshaller.marshal(courses, os);
//		} catch (JAXBException jaxb) {
//			jaxb.printStackTrace();
//			throw new WebApplicationException();
//		}
//	}
//	
	protected void outputProject(OutputStream os, Project project) throws IOException {
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
//
//	protected void outputCourses(OutputStream os, Projects projects) throws IOException {
//		try {
//			JAXBContext jaxbContext = JAXBContext.newInstance(Projects.class);
//			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
//			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//			jaxbMarshaller.marshal(projects, os);
//		} catch (JAXBException jaxb) {
//			jaxb.printStackTrace();
//			throw new WebApplicationException();
//		}
//	}
//
//	protected void outputCourses(OutputStream os, Project project) throws IOException {
//		try {
//			JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
//			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
//			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//			jaxbMarshaller.marshal(project, os);
//		} catch (JAXBException jaxb) {
//			jaxb.printStackTrace();
//			throw new WebApplicationException();
//		}
//	}
//
//	protected void outputCourses(OutputStream os, NotFound notFound) throws IOException {
//		try {
//			JAXBContext jaxbContext = JAXBContext.newInstance(NotFound.class);
//			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
//			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//			jaxbMarshaller.marshal(notFound, os);
//		} catch (JAXBException jaxb) {
//			jaxb.printStackTrace();
//			throw new WebApplicationException();
//		}
//	}
//
	protected Project readProject(InputStream is) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			Project project = new Project();
			NodeList nodes = root.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				if (element.getTagName().equals("name")) {
					project.setProjectName(element.getTextContent());
				} else if (element.getTagName().equals("description")) {
					project.setDescription(element.getTextContent());
				}
			}
			return project;
		} catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
	}
	
	protected Meeting readMeeting(InputStream is) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			Meeting meeting = new Meeting();
			NodeList nodes = root.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				if (element.getTagName().equals("name")) {
					meeting.setMeetingName(element.getTextContent());
				} else if (element.getTagName().equals("year")) {
					meeting.setYear(element.getTextContent());
				}
			}
			return meeting;
		} catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
	}
}