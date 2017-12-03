package assignment2;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UrlQueryImpl implements UrlQuery {
	public String countFiles(String project, String year) {
		int val = 0;
		String url = ("http://eavesdrop.openstack.org/meetings/");
		Document doc;
		try {
			Jsoup.connect(url + project).get();

			try {
				doc = Jsoup.connect(url + project + "/" + year).get();
				Elements links = doc.select("a[href]");
				for (Element link : links) {
					String dataText = link.text();
					if (!dataText.equals("Data") && !dataText.equals("Name") && !dataText.equals("Last modified")
							&& !dataText.equals("Size") && !dataText.equals("Description")
							&& !dataText.equals("Parent Directory")) {
						val++;
					}
				}
			} catch (Exception d) {
				return ("Invalid year for project " + project);
			}

		} catch (Exception e) {
			return ("Project with name: " + project + " not found.");
		}
		return ("Number of meeting files: " + val);
		// return "In URL query";

	}
}
