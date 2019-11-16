package partTwods;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

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

	@JsonProperty
	public int getUserId() {
		return userId;
	}

	@JsonProperty
	public String getPassword() {
		return password;
	}
		
}
