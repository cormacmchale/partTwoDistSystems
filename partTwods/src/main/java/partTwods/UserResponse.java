package partTwods;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;

//small class for sending back a response in an appropriate format

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
	//in case it's ever need for xml
	public void setMessage(String message) {
		this.message = message;
	}

}
