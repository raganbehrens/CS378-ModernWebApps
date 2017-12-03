package assign.services;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EavesdropService {

	public String getData() {
		return "Hello from Eavesdrop service.";
	}
	
	public ArrayList<String> listAll(){
		ArrayList<String> list = new ArrayList<String>();
		Document doc;
		try {
			doc = Jsoup.connect("http://eavesdrop.openstack.org/meetings/").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			String dataText = link.text();
			if (!dataText.equals("Data") && !dataText.equals("Name") && !dataText.equals("Last modified")
					&& !dataText.equals("Size") && !dataText.equals("Description")
					&& !dataText.equals("Parent Directory")) {
				list.add(dataText);
			}
		}
		return list;
	}
	
	public ArrayList<String> getYears(String projectName) {
		ArrayList<String> years = new ArrayList<String>();
		Document doc;
		try {
			doc = Jsoup.connect("http://eavesdrop.openstack.org/meetings/" + projectName).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			String dataText = link.text();
			if (!dataText.equals("Data") && !dataText.equals("Name") && !dataText.equals("Last modified")
					&& !dataText.equals("Size") && !dataText.equals("Description")
					&& !dataText.equals("Parent Directory")) {
				years.add(dataText);
			}
		}
		return years;
	}
}
