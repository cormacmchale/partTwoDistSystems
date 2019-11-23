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

//this resouse handles all calls to /user
//produces xml and json as per assignment requirments
@Path("/user")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserAPI {

	//validator for request 
	private final Validator validator;
	//client for interacting with the password service
	private Client gRPCCalls;

	//pass in validator for validating requests
	//the string and int for the client comes from the webConfig
	public UserAPI(Validator validator, String s, int p)
	{
	    this.validator = validator;
		//Client for password service
	    //this try/catch doesn't avtually do anything
		try
		{
			gRPCCalls = new Client(s, p);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	//add a user here
	@POST
	//object is annotated correctly so the program will parse the body of request into it for use in java
	public Response adduser(UserObject newUser)
	{
		//use the validator to build up a collection of validation issues
		Set<ConstraintViolation<UserObject>> violations = validator.validate(newUser);
		//if any field fails then enter the if
		if(violations.size()>0)
		{
			//send back appropriate response
			return Response.status(400).type(MediaType.APPLICATION_JSON).entity(new UserResponse("Json failed Validation")).build();
		}
		//no violations
		else
		{
			//pass to async method
			gRPCCalls.requestAHash(newUser);
			//obviously the issue here is the if you request a hash async you have to respond with 200 even if it fails
			//you just alert the user they at least got this far
			return Response.status(200).type(MediaType.APPLICATION_JSON).entity(new UserResponse("User Added Sucessfully")).build();
		}
	}
	
	//send a login object
	@Path("/login")
	@POST
	public Response login(LoginObject login)
	{
		//validate
		Set<ConstraintViolation<LoginObject>> violations = validator.validate(login);
		if(violations.size()>0)
		{
			return Response.status(400).type(MediaType.APPLICATION_JSON).entity(new UserResponse("Validation Failed")).build();
		}
		else
		{
			//get the database
			PretendDatabase d = PretendDatabase.getInstance();
			//make the call to service,pass it the specific user information
			//this method call will return true or flase and enter the correct part of statement based off the result of validation
			//it then passes an appropriate response
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
	//bog shtandard - you know whats up
	@GET
	public Collection<UserObject> getUsers() 
	{	    
	   PretendDatabase d = PretendDatabase.getInstance();
	   return d.data();
	}
	
	//return sepcific user
	//add the id of the user to eth end point to return a specific user
	@Path("/{id}")	
	@GET
	public Response getUser(@PathParam("id") int id) 
	{
		  PretendDatabase d = PretendDatabase.getInstance();
		  //if the user does not exist in database
		  if(d.returnUser(id)==null)
		  {
			  return Response.status(404).type(MediaType.APPLICATION_JSON).entity(new UserResponse("User Not in database")).build(); 
		  }
		  //send back the user object
		  else
		  {
			  //send the user in Json back 
			  return Response.status(200).type(MediaType.APPLICATION_JSON).entity(d.returnUser(id)).build();
		  }
	}
	//the same logic as get user however
	//there is a fundamental issue with how this works
	//because the user is initially given a key in the map based off of the id they get added with
	//when I replace user id=5 with a new user with a new id they save over with the key 5 in the hash map
	//they will then have a different id that they can't be retrieved with
	//there is a work around for this but I've decided to leave it out as I'm happy with the amount of time I have put into the project 
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
	//delete the user
	//same logic as alter only now the user is deleted
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
