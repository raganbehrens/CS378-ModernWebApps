package assignment2;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ControllerTest {

	OpenStackMeetingsController controller;
	UrlQuery mock;
	//UrlQueryImpl queryTest = new UrlQueryImpl();
	
	@Before
	public void setUp() {
		//assert (1==0);
		mock = mock(UrlQuery.class);
		controller = new OpenStackMeetingsController(mock);
	}
	
	@Test
	public void welcomePage() {
		String response = controller.welcomePage();
		assertEquals(response, "Welcome to OpenStack meeting statistics calculation page. Please provide project and year as query parameters.");
	}
	
	// If the project parameter was passed without a value ie. ?project&year=2000
	@Test
	public void missingParams() {
		
		//when(mock.countFiles("", "2000")).thenReturn("Project name required");
		
		String response1 = controller.parseData("", "2000");
		String response2 = controller.parseData("solum", "");
		assertEquals(response1, "Project name required");
		assertEquals(response2, "Year required");
			
	}
	
	@Test
	public void invalidYear() {
		when(mock.countFiles("solum",  "2016")).thenReturn("Invalid year for project solum");
		String response = controller.parseData("solum", "2016");
		assertEquals(response, "Invalid year for project solum");
	}
	
	@Test
	public void invalidProject() {
		when(mock.countFiles("solu", "2015")).thenReturn("Project with name: solu not found.");
		String response = controller.parseData("solu", "2015");
		//System.out.println(response);
		assertEquals(response, "Project with name: solu not found.");
		
	}
	
	@Test
	public void validRequest() {
		when(mock.countFiles("solum", "2015")).thenReturn("Number of meeting files: 4");
		String response = controller.parseData("solum", "2015");
		assertEquals(response, "Number of meeting files: 4");
	}
}
