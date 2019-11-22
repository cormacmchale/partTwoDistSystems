package partTwods;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class LoginObject {

	@NotNull
	private int userId;
	@NotBlank
	private String password;
	
	public LoginObject(){//Json deserialisation
	}
	
	public LoginObject(int i, String p)
	{
		this.userId = i;
		this.password = p;
	}
	@XmlElement(required = true)
	@JsonProperty
	public int getUserId() {
		return userId;
	}
	@XmlElement(required = true)
	@JsonProperty
	public String getPassword() {
		return password;
	}
	//needed for xml
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}
		
}
