package partTwods;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class UserResponse {

	private String message;
	
	public UserResponse() {}
	
	public UserResponse(String m)
	{
		this.message = m;
	}
	@XmlElement(required = true)
	@JsonProperty 
	public String getMessage() 
	{
		return message;
	}

}
