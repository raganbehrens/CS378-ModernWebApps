package assign.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "output")
@XmlAccessorType(XmlAccessType.FIELD)
public class Error {

	String error;
		
	List<String> year = null;

	public void setMessage(String name) {
		error = ("Project " + name + " does not exist");
	}

}