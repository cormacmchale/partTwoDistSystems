package partTwods;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;

public class UserObject {

	public UserObject(){//Json deserialisation
	}
    //first object for add user
	public UserObject(int id, String uN ,String e, String p)
	{
		this.userId = id;
		this.userName = uN;
		this.email = e;
		this.password = p;
	}
	//second object for storing and returning
	public UserObject(int id, String uN ,String e, String p, ByteString h, ByteString s)
	{
		this.userId = id;
		this.userName = uN;
		this.email = e;
		this.password = p;
		this.hashedPassword = h;
		this.salt = s;
	}
	//variables required for a post
	@NotNull
	private int userId;
	@NotBlank
	private String userName;
	@NotBlank
	private String email;
	@NotBlank
	private String password;
	//No validation required
	private ByteString hashedPassword;
	private ByteString salt;
	 @JsonProperty
	public ByteString getHashedPassword() {
		return hashedPassword;
	}
	 @JsonProperty
	public ByteString getSalt() {
		return salt;
	}	
	 @JsonProperty
	public int getUserId() {
		return userId;
	}
	 @JsonProperty
	public String getUserName() {
		return userName;
	}
	 @JsonProperty
	public String getEmail() {
		return email;
	}
	 @JsonProperty
	public String getPassword() {
		return password;
	}	
}