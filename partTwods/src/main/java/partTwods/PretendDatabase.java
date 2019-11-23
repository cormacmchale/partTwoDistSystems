package partTwods;

import java.util.Collection;
import java.util.HashMap;

//this singleton maintains a hashmap and simulates a database for testing
//explanations of issues with this approach described above the alterUser() method
public class PretendDatabase {
	
	private HashMap<Integer, UserObject> userStorage = new HashMap<Integer, UserObject>();
	
	private PretendDatabase() {}
	
	static PretendDatabase database = new PretendDatabase();
	
	static PretendDatabase getInstance()
	{
		return database;
	}
	
	public void addUser(UserObject newUser)
	{
		userStorage.put(newUser.getUserId(), newUser);
	}
	public Collection<UserObject> data()
	{
		return userStorage.values();
	}
	public UserObject returnUser(int id)
	{
		return userStorage.get(id);
	}
	//I don't think this is the best way to do this
	//because now when you alter a user a new user will get saved in
	//their place without a hashed version of their password
	//also now their Id and map Value are now different
	public boolean alterUser(int id, UserObject changeUser)
	{
		//return false if the user dosen't exist and alert the requester
		if(userStorage.get(id)==null)
		{
			return false;
		}
		else
		{	
			//replace the old user with the new user info
			userStorage.replace(id, changeUser);
			return true;
		}
	}
	//same as above
	//return false if not found
	//return true when delete is actually called
	public boolean deleteUser(int id)
	{
		if(userStorage.get(id)==null)
		{
			return false;
		}
		else
		{			
			userStorage.remove(id);
			return true;
		}
	}
}
