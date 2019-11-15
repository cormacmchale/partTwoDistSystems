package partTwods;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserAPI {
	
	//Client for password service
	Client gRPCCalls = new Client("127.0.0.1", 40000);
	
	//Simulated Database
	static HashMap<Integer, UserObject> users = new HashMap<>();	

	
	//validator
	private final Validator validator;

	//pass in validator for validating JSON
	public UserAPI(Validator validator)
	{
	    this.validator = validator;
	}
	//add a user - validate JSON
	@POST
	public Response adduser(UserObject newUser)
	{
		Set<ConstraintViolation<UserObject>> violations = validator.validate(newUser);
		if(violations.size()>0)
		{
			return Response.status(400).type(MediaType.TEXT_PLAIN).entity("Json failed Validation!").build();
		}
		else
		{
			//pass to async method
			gRPCCalls.requestAHash(newUser);
			return Response.status(200).build();
		}
		//call password service grpc
	}
	//return all users
	@GET
	public Collection<UserObject> getUsers() 
	{	      	
	   return users.values();
	}
}
