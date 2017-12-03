package assign.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import assign.domain.Meeting;
import assign.domain.Project;
//import assign.domain.UTCourse;

import java.util.logging.*;

public class DBLoader {
	private SessionFactory sessionFactory;
	
	Logger logger;
	
	public DBLoader() {
		// A SessionFactory is set up once for an application
		Configuration configuration = new Configuration();
		configuration.configure();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(
		            configuration.getProperties()). buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        logger = Logger.getLogger("DBLoader");
	}
	
	public void loadData(Map<String, List<String>> data) {
		logger.info("Inside loadData.");
	}
	
	// Creating a newProject
	public Long addProject(String projectName, String projectDescription) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Long projectId = null;
		try {
			tx = session.beginTransaction();
			Project newProject = new Project(projectName, projectDescription); 
			session.save(newProject);
		    projectId = newProject.getId();
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();
		}
		return projectId;
	}

	
//	public Long addAssignment(String title) throws Exception {
//		Session session = sessionFactory.openSession();
//		Transaction tx = null;
//		Long assignmentId = null;
//		try {
//			tx = session.beginTransaction();
//			Assignment newAssignment = new Assignment( title, new Date(), new Long(1)); 
//			session.save(newAssignment);
//		    assignmentId = newAssignment.getId();
//		    tx.commit();
//		} catch (Exception e) {
//			if (tx != null) {
//				tx.rollback();
//				throw e;
//			}
//		}
//		finally {
//			session.close();
//		}
//		return assignmentId;
//	}
	
	public Long addMeetingToProject(String meetingName, Long projectId, String year) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Long meetingId = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Project.class).add(Restrictions.eq("id", projectId));
			List<Project> projectList = criteria.list();
			if (projectList == null)
				return null;
			Meeting meeting = new Meeting(meetingName, year);
			meeting.setProject(projectList.get(0));
			session.save(meeting);
			meetingId = meeting.getId();
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				//throw e;
				return null;
			}
		}
		finally {
			session.close();
		}
		return meetingId;
	}
	
	public Long updateMeeting(Long projectId, Long meetingId, String name, String year) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Criteria criteria2 = session.createCriteria(Project.class).add(Restrictions.eq("id", meetingId));
			Criteria criteria = session.createCriteria(Meeting.class).add(Restrictions.eq("id", meetingId));
			List<Meeting> meetingList = criteria.list();
			List<Project> projectList = criteria2.list();
			if (meetingList.size() == 0)
				return null;
			if (projectList.size() == 0)
				return null;
			Meeting meeting = meetingList.get(0);
			if (meeting.getProject().getId() != projectId)
				return null;
			meeting.setMeetingName(name);
			meeting.setYear(year);
			session.save(meeting);
			meetingId = meeting.getId();
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();
		}
		return meetingId;
	}
	
	
//	public Long addAssignmentAndCourse(String title, String courseTitle) throws Exception {
//		Session session = sessionFactory.openSession();
//		Transaction tx = null;
//		Long assignmentId = null;
//		try {
//			tx = session.beginTransaction();
//			Assignment newAssignment = new Assignment( title, new Date() );
//			UTCourse course = new UTCourse(courseTitle);
//			newAssignment.setUtcourse(course);
//			session.save(course);
//			session.save(newAssignment);
//		    assignmentId = newAssignment.getId();
//		    tx.commit();
//		} catch (Exception e) {
//			if (tx != null) {
//				tx.rollback();
//				throw e;
//			}
//		}
//		finally {
//			session.close();
//		}
//		return assignmentId;
//	}
	
//	public Long addAssignmentsToCourse(List<String> assignments, String courseTitle) throws Exception {
//		Session session = sessionFactory.openSession();
//		Transaction tx = null;
//		Long courseId = null;
//		try {
//			tx = session.beginTransaction();
//			UTCourse course = new UTCourse(courseTitle);
//			session.save(course);
//			courseId = course.getId();
//			for(String a : assignments) {
//				Assignment newAssignment = new Assignment( a, new Date() );
//				newAssignment.setUtcourse(course);
//				session.save(newAssignment);
//			}
//		    tx.commit();
//		} catch (Exception e) {
//			if (tx != null) {
//				tx.rollback();
//				throw e;
//			}
//		}
//		finally {
//			session.close();
//		}
//		return courseId;
//	}
	
//	public List<Assignment> getAssignmentsForACourse(Long courseId) throws Exception {
//		Session session = sessionFactory.openSession();		
//		session.beginTransaction();
//		String query = "from Assignment where course=" + courseId; // BAD PRACTICE
//		List<Assignment> assignments = session.createQuery(query).list();
//		session.close();
//		return assignments;
//	}
	
//	public List<Object[]> getAssignmentsForACourse(String courseName) throws Exception {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		String query = "from Assignment a join a.course c where c.courseName = :cname";		
//				
//		List<Object[]> assignments = session.createQuery(query).setParameter("cname", courseName).list();
//		session.close();
//		
//		return assignments;
//	}
	
//	public Assignment getAssignment(String title) throws Exception {
//		Session session = sessionFactory.openSession();
//		
//		session.beginTransaction();
//		
//		Criteria criteria = session.createCriteria(Assignment.class).
//        		add(Restrictions.eq("title", title));
//		
//		List<Assignment> assignments = criteria.list();
//		
//		session.close();
//		
//		if (assignments.size() > 0) {
//			return assignments.get(0);			
//		} else {
//			return null;
//		}
//	}
	
	public Project getProject(Long projectId) throws Exception {
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Project.class).
        		add(Restrictions.eq("id", projectId));
		
		List<Project> projects = criteria.list();
		
		if (projects.size() > 0) {
			return projects.get(0);	
		} else {
			session.close();
			return null;
		}
	}

//	public Homework getHomework_using_get(Long homeworkId) throws Exception {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		Homework h = (Homework)session.get(Homework.class, homeworkId);
//		session.close();
//		return h;		
//	}

//	public Homework getHomework_using_load(Long homeworkId) throws Exception {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		Homework h = null;
//		try {
//		h = (Homework)session.load(Homework.class, homeworkId);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//		finally {
//			session.close();
//		}
//		return h;		
//	}
	
//	public Homework getHomework_using_criteria(Long homeworkId) throws Exception {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		Criteria criteria = session.createCriteria(Homework.class).
//        		add(Restrictions.eq("id", homeworkId));
//		List<Homework> homeworks = criteria.list();
//		if (homeworks.size() > 0) {
//			session.close();
//			return homeworks.get(0);	
//		} else {
//			session.close();
//			return null;
//		}
//	}
	

	public Object deleteProject(Long projectId) throws Exception {
		
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		//String query = "from Projects p where p.project_name = :projectName";		
		Criteria criteria = session.createCriteria(Project.class).
        		add(Restrictions.eq("id", projectId));
		List<Project> projects = criteria.list();
		if (projects.size() == 0)
			return null;
		//Project p = (Project)session.createQuery(query).setParameter("projectName", projectName).list().get(0);
		
        session.delete(projects.get(0));

        session.getTransaction().commit();
        session.close();
        return (200);
	}
	
//	public void deleteCourse(String courseName) throws Exception {
//		
//		Session session = sessionFactory.openSession();		
//		session.beginTransaction();
//		String query = "from UTCourse c where c.courseName = :courseName";		
//				
//		UTCourse c = (UTCourse)session.createQuery(query).setParameter("courseName", courseName).list().get(0);
//		
//        session.delete(c);
//
//        session.getTransaction().commit();
//        session.close();
//	}
	
	
//	public Assignment getAssignment(Long assignmentId) throws Exception {
//		Session session = sessionFactory.openSession();
//		
//		session.beginTransaction();
//		
//		Criteria criteria = session.createCriteria(Assignment.class).
//        		add(Restrictions.eq("id", assignmentId));
//		
//		List<Assignment> assignments = criteria.list();
//		
//		session.close();
//		
//		return assignments.get(0);		
//	}
}