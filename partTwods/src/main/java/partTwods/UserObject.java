package partTwods;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.*;
import com.google.protobuf.ByteString;

@XmlRootElement
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

	@XmlElement(required = true)
	@JsonProperty
	public ByteString getHashedPassword() {
		return hashedPassword;
	}
	@XmlElement(required = true)
	@JsonProperty
	public ByteString getSalt() {
		return salt;
	}
	@XmlElement(required = true)
	@JsonProperty
	public int getUserId() {
		return userId;
	}
	@XmlElement(required = true)
	@JsonProperty
	public String getUserName() {
		return userName;
	}
	@XmlElement(required = true)
	@JsonProperty
	public String getEmail() {
		return email;
	}
	@XmlElement(required = true)
	@JsonProperty
	public String getPassword() {
		return password;
	}
	
	//needed for xml?
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setHashedPassword(ByteString hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
	public void setSalt(ByteString salt) {
		this.salt = salt;
	}	
	
}