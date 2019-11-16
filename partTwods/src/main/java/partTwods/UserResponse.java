package partTwods;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponse {


	private String message;
	
	public UserResponse() {}
	
	public UserResponse(String m)
	{
		this.message = m;
	}
	
	 @JsonProperty 
	public String getMessage() 
	{
		return message;
	}

}
