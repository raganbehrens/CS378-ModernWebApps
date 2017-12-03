package assign.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@XmlRootElement(name = "meeting")
@Table( name = "meetings" )
public class Meeting {
	
	private Long id;
    private String meetingName;
    private String year;
    private Project project;
    //private Set<Assignment> assignments;

    public Meeting() {
    	// this form used by Hibernate
    }
    
    public Meeting(String meetingName, String year) {
    	this.meetingName = meetingName;
    	this.year = year;
    	//this.project = project;
    }
    
    @XmlAttribute(name = "id")
    @Id
	//@GeneratedValue(generator="increment")
	//@GenericGenerator(name="increment", strategy = "increment")
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
		return id;
    }

    private void setId(Long id) {
		this.id = id;
    }
    
    @XmlElement(name = "name")
    @Column(name="meeting_name")
    public String getMeetingName() {
		return meetingName;
    }

    public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
    }
    
    @XmlElement(name = "year")
    @Column(name = "year")
    public String getYear() {
    	return year;
    }
    
    public void setYear(String year) {
    	this.year = year;
    }
    
    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "project_id")
    public Project getProject() {
    	return this.project;
    }
    
    public void setProject(Project project) {
    	this.project = project;
    }

}