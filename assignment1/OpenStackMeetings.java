

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Servlet implementation class OpenStackMeeting
 */
@WebServlet(name = "openstackmeeting", urlPatterns = { "/openstackmeeting" })
public class OpenStackMeetings extends HttpServlet {
	ArrayList<String> history = new ArrayList<String>();
	ArrayList<String> data = new ArrayList<String>();
	boolean sessionStarted = false;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OpenStackMeetings() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		boolean badRequest = false;
		Document doc = null;
		String url = "";
		String session = request.getParameter("session");
		String project = request.getParameter("project");
		String year = request.getParameter("year");
		data.clear();
		
		if (session == null) {
			if ((project != null || year != null) && !(project != null && year != null)) {
				String missingParam = "";
				if (project == null)
					missingParam = ("project");
				if (year == null)
					missingParam = ("year");
				response.getWriter().println("Required parameter " + missingParam + " missing.");
			}
			else if (project != null && year != null) {
				data.clear();
				String urlProject = ("http://eavesdrop.openstack.org/meetings/" + project);
				url = ("http://eavesdrop.openstack.org/meetings/" + project + "/" + year);
				try {
					Jsoup.connect(urlProject).get();
				}
				catch (Exception e) {
					badRequest = true;
					//response.getWriter().println(urlProject);
					response.getWriter().println("Project with name: " + project + " not found.");
				}
				
				if (!badRequest) {
					//response.getWriter().println(badRequest);
					try {
						doc = Jsoup.connect(url).get();
					} catch (Exception e) {
						badRequest = true;
						response.getWriter().println("Invalid year for project " + project + ".");
					}
					if (!badRequest) {
						Elements links = doc.select("a[href]");
						//if (sessionStarted)
							//history.add(url);
						for (Element link : links) {
							String dataText = link.text();
							if (dataText.equals("Data") || dataText.equals("Name") || dataText.equals("Last Modified")
									|| dataText.equals("Size") || dataText.equals("Description")
									|| dataText.equals("Parent Directory")) {

							} else {
								data.add(link.text());
							}
						}
					}
				}
			}
		}
		
		else if (session != null){
			url = ("http://eavesdrop.openstack.org/meetings/" + session);
			session = session.toLowerCase();
			if (session.equals("start")) {
				//history.add(url);
				response.getWriter().println("session started");
				sessionStarted = true;
			}
			else if (session.equals("end")) {
				sessionStarted = false;
				history.clear();
			}
			else {
				// Invalid
			}
			
		}
		response.getWriter().println("History");
		for (String link : history) {
			response.getWriter().println(link);
		}
		response.getWriter().println("Data");
		for (String line : data) {
			response.getWriter().println(line);
		}
		if (sessionStarted) {
			history.add(url);

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
