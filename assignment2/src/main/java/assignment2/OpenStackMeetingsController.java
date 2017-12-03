package assignment2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OpenStackMeetingsController {
	
	UrlQuery query;
	
	public OpenStackMeetingsController() {
		
	}
	
	public OpenStackMeetingsController(UrlQuery query) {
		this.query = query;
	}
	
	@ResponseBody
	@RequestMapping(value = "/openstackmeetings")
	public String welcomePage() {
		return "Welcome to OpenStack meeting statistics calculation page. Please provide project and year as query parameters.";
	}
	
	@ResponseBody
	@RequestMapping(value = "/openstackmeetings", params = {"project", "year"})
	public String parseData(@RequestParam("project") String project, @RequestParam("year") String year) {
		if (!project.equals("") && !year.equals(""))
		{
			//String url = buildURL(project, year);
			return this.query.countFiles(project, year);
		}
		else {
			if (project.equals(""))
				return "Project name required";
			else if (year.equals(""))
				return "Year required";
		}
		return "Error";
	}
	
	public String buildURL(String project, String year) {
		String url = ("http://eavesdrop.openstack.org/meetings/" + project + "/" + year);
		return url;
	}
	
}
