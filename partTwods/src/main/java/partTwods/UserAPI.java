package partTwods;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.Collection;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserAPI {

	//validator
	private final Validator validator;
	private Client gRPCCalls;

	//pass in validator for validating JSON
	public UserAPI(Validator validator)
	{
	    this.validator = validator;
		//Client for password service
		try
		{
			gRPCCalls = new Client("127.0.0.1", 40000);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	//add a user - validate JSON
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public Response adduser(UserObject newUser)
	{
		Set<ConstraintViolation<UserObject>> violations = validator.validate(newUser);
		if(violations.size()>0)
		{
			return Response.status(400).type(MediaType.APPLICATION_JSON).entity(new UserResponse("Json failed Validation")).build();
		}
		else
		{
			//pass to async method
			gRPCCalls.requestAHash(newUser);
			return Response.status(200).type(MediaType.APPLICATION_JSON).entity(new UserResponse("User Added Sucessfully")).build();
		}
		//call password service grpc
	}
	
	@Path("/login")
	@POST
	public Response login(LoginObject login)
	{
		Set<ConstraintViolation<LoginObject>> violations = validator.validate(login);
		if(violations.size()>0)
		{
			return Response.status(400).type(MediaType.APPLICATION_JSON).entity(new UserResponse("Json failed Validation")).build();
		}
		else
		{
			//get the database
			PretendDatabase d = PretendDatabase.getInstance();
			//make the call to service,pass it the specific user information
			if(gRPCCalls.syncPasswordValidation(login, d.returnUser(login.getUserId()).getHashedPassword(), d.returnUser(login.getUserId()).getSalt()))
			{
				return Response.status(200).type(MediaType.APPLICATION_JSON).entity(new UserResponse("Login Sucess")).build();
			}
			else
			{
				return Response.status(400).type(MediaType.APPLICATION_JSON).entity(new UserResponse("Login Failed - Incorrect Password")).build();
			}
		}
	}	
	//return all users
	@GET
	public Collection<UserObject> getUsers() 
	{	    
	   PretendDatabase d = PretendDatabase.getInstance();
	   return d.data();
	}
	
	//return sepcific user
	@Path("/{id}")	
	@GET
	public Response getUser(@PathParam("id") int id) 
	{
		  PretendDatabase d = PretendDatabase.getInstance();
		  if(d.returnUser(id)==null)
		  {
			  return Response.status(404).type(MediaType.APPLICATION_JSON).entity(new UserResponse("User Not in database")).build(); 
		  }
		  else
		  {
			  //send the user in Json back 
			  return Response.status(200).type(MediaType.APPLICATION_JSON).entity(d.returnUser(id)).build();
		  }
	}
	//alter the user
	@Path("/{id}")	
	@PUT
	public Response alterUser(UserObject changeUser, @PathParam("id") int id)
	{
		PretendDatabase d = PretendDatabase.getInstance();
		if(d.alterUser(id, changeUser))
		{
			return Response.status(200).type(MediaType.APPLICATION_JSON).entity(new UserResponse("User Changed")).build();	
		}
		else
		{
			return Response.status(404).type(MediaType.APPLICATION_JSON).entity(new UserResponse("User not in Database - please add a user before altering")).build();
		}
	}
	//alter the user
	@Path("/{id}")	
	@DELETE
	public Response deleteUser(@PathParam("id") int id)
	{
		PretendDatabase d = PretendDatabase.getInstance();
		if(d.deleteUser(id))
		{
			return Response.status(200).type(MediaType.APPLICATION_JSON).entity(new UserResponse("User Deleted")).build();	
		}
		else
		{
			return Response.status(404).type(MediaType.APPLICATION_JSON).entity(new UserResponse("User not in Database - please add a user before deleting")).build();
		}
	}	
}
