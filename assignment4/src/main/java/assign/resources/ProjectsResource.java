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

import assign.domain.Course;
import assign.domain.Courses;
import assign.domain.NewProject;
import assign.domain.NotFound;
import assign.domain.Project;
import assign.domain.Projects;
import assign.services.ProjectService;
import assign.services.ProjectServiceImpl;

@Path("/projects")
public class ProjectsResource {

	ProjectService projectService;
	String password;
	String username;
	String dburl;
	String dbhost, dbname;

	public ProjectsResource(@Context ServletContext servletContext) {
		dbhost = servletContext.getInitParameter("DBHOST");
		dbname = servletContext.getInitParameter("DBNAME");
		dburl = "jdbc:mysql://" + dbhost + ":3306/" + dbname;
		username = servletContext.getInitParameter("DBUSERNAME");
		password = servletContext.getInitParameter("DBPASSWORD");
		this.projectService = new ProjectServiceImpl(dburl, username, password);
	}

	@GET
	@Path("/helloworld")
	@Produces("text/html")
	public String helloWorld() {
		System.out.println("Inside helloworld");
		System.out.println("DB creds are:");
		System.out.println("DBURL:" + dburl);
		System.out.println("DBUsername:" + username);
		System.out.println("DBPassword:" + password);
		return "Hello world " + dburl + " " + username + " " + password;
	}

	@POST
	@Consumes("application/xml")
	public Response createProject(InputStream is) throws Exception {
		System.out.println("IN POST");
		NewProject newProject = readNewProject(is);
		newProject = this.projectService.addProject(newProject);
		if (newProject == null) {
			//System.out.println("about to return bad response");
			return Response.status(400).build();
		}
		return Response.created(URI.create("/projects/" + newProject.getProjectId())).build();
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

		final NewProject project = projectService.getProject(Integer.parseInt(projectId));
		if (project == null)
			return Response.status(404).build();

		StreamingOutput stream = new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputProject(outputStream, project);
			}
		};
		
		return Response.ok(stream).build();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes("application/xml")
	public Response updateProject(InputStream is, @PathParam("id") String projectId) throws Exception {
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
		int id = Integer.parseInt(projectId);
		NewProject newProject = readNewProject(is);
		NewProject oldProject = projectService.getProject(id);
		NewProject retProject = projectService.updateProject(id, newProject);
		
		
		if (oldProject == null)
			return Response.status(404).build();
		else if (retProject == null)
			return Response.status(400).build();
		else
			return Response.noContent().build();
		/*return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputProject(outputStream, project);
			}
		};*/
	}
	
	@DELETE
	@Path("/{id}")
	public Response updateProject(@PathParam("id") String projectId) throws Exception {
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
		int id = Integer.parseInt(projectId);
		//NewProject newProject = readNewProject(is);
		//NewProject oldProject = projectService.getProject(id);
		//NewProject retProject = projectService.updateProject(id, newProject);
		int response = projectService.deleteProject(id);
		
		if (response == 200)
			return Response.status(200).build();
		else
			return Response.status(404).build();
		/*return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputProject(outputStream, project);
			}
		};*/
	}

	@GET
	@Path("/projects")
	@Produces("application/xml")
	public StreamingOutput getAllProjects() throws Exception {
		// Project heat = new Project();
		// heat.setName("%23heat");

		final Projects projects = new Projects();
		projects.setProjects(new ArrayList<String>());
		// projects.getProjects().add("%23heat");
		projects.getProjects().add("%23dox");

		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputCourses(outputStream, projects);
			}
		};
	}

//	@GET
//	@Path("/project")
//	@Produces("application/xml")
//	public StreamingOutput getProject() throws Exception {
//
//		final Project heat = new Project();
//		heat.setName("%23heat");
//		heat.setLink(new ArrayList<String>());
//		heat.getLink().add("l3");
//		heat.getLink().add("l2");
//
//		// throw new WebApplicationException();
//
//		final NotFound notFound = new NotFound();
//		notFound.setError("Project non-existent-project does not exist");
//
//		return new StreamingOutput() {
//			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
//				outputCourses(outputStream, notFound);
//			}
//		};
//
//	}

	protected void outputCourses(OutputStream os, Courses courses) throws IOException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Courses.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(courses, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	
	protected void outputProject(OutputStream os, NewProject project) throws IOException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(NewProject.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(project, os);
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

	protected void outputCourses(OutputStream os, NotFound notFound) throws IOException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(NotFound.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(notFound, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}

	protected NewProject readNewProject(InputStream is) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			NewProject course = new NewProject();
			NodeList nodes = root.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				if (element.getTagName().equals("name")) {
					course.setName(element.getTextContent());
				} else if (element.getTagName().equals("description")) {
					course.setDescription(element.getTextContent());
				}
			}
			return course;
		} catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
	}
}