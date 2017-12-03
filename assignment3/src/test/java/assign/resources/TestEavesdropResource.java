package assign.resources;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class TestEavesdropResource {

	private HttpClient client;

	@Test
	public void testGetCourseListing() throws Exception {

		String url1 = "http://localhost:8080/assignment3/myeavesdrop/projects/";
		String url2 = "http://localhost:8080/assignment3/myeavesdrop/projects/solum_team_meeting/meetings";
		String url3 = "http://localhost:8080/assignment3/myeavesdrop/projects/3rd_party_ci/meetings";
		String url4 = "http://localhost:8080/assignment3/myeavesdrop/projects/non-existent-project/meetings";
		
		
		String response1 = query(url1, "project");
		String response2 = query(url2, "year");
		String response3 = query(url3, "year");
		String response4 = query(url4, "error");
		
		System.out.println("First Test");
		assertTrue(response1.contains("_poppy"));
		assertTrue(response1.contains("_fuel"));
		assertTrue(response1.contains("_murano"));
		assertTrue(response1.contains("ambassadors"));
		assertTrue(response1.contains("barbican"));
		assertTrue(response1.contains("compass"));
		assertTrue(response1.contains("defore"));
		assertTrue(response1.contains("kolla"));
		assertTrue(response1.contains("octavia"));
		assertTrue(response1.contains("storlets"));
		
		System.out.println("Second Test");
		assertTrue(response2.contains("2013"));
		assertTrue(response2.contains("2014"));
		assertTrue(response2.contains("2015"));
		assertTrue(response2.contains("2016"));
		assertTrue(response2.contains("2017"));
		
		System.out.println("Third Test");
		assertTrue(response3.contains("2014"));
		
		System.out.println("Fourth Test");
		assertEquals(response4, "Project non-existent-project does not exist");
		


		
	}
	
	public String query(String url, String tag) {
		Document doc;
		Elements projects = null;
		try {
			doc = Jsoup.connect(url).get();
			projects = doc.select(tag);
			//System.out.println("Projects: " + projects.text().contains("climate"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projects.text();
	}
}
