package assign.domain;

import java.util.Collections;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@XmlRootElement(name = "project")
@Table( name = "projects" )
public class Project {
	
	private Long id;

    private String projectName;
    private String description;
    private Set<Meeting> meetings;
    
    public Project() {
    	// this form used by Hibernate
    }
    
    public Project(String name, String description) {
    	// for application use, to create new assignment
    	this.projectName = name;
    	this.description = description;
    	this.meetings = Collections.emptySet();
    }
    
    public Project(String name, String description, Long providedId) {
    	// for application use, to create new assignment
    	this.projectName = name;
    	this.description = description;
    	this.id = providedId;
    }    
    
    @XmlAttribute(name = "id")
    @Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
		return id;
    }

    private void setId(Long id) {
		this.id = id;
    }
    
    @XmlElement(name = "name")
    @Column(name = "project_name")
    public String getProjectName() {
		return projectName;
    }

    public void setProjectName(String projectName) {
		this.projectName = projectName;
    }

    @XmlElement(name = "description")
	@Column(name = "description")
	public String getDescription() {
		return description;
    }

    public void setDescription(String description) {
		this.description = description;
    }
    
    @XmlElementWrapper(name = "meetings")
    @XmlElement(name = "meeting")
    @OneToMany(orphanRemoval = true, mappedBy="project")
    @Cascade({CascadeType.DELETE})
    public Set<Meeting> getMeetings() {
    	return this.meetings;
    }
 
    public void setMeetings(Set<Meeting> meetings) {
    	this.meetings = meetings;
    }

}