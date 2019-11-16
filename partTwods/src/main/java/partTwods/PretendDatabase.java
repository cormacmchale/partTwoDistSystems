package partTwods;

import java.util.Collection;
import java.util.HashMap;

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
	//maybe more to be done here?
	public boolean alterUser(int id, UserObject changeUser)
	{
		if(userStorage.get(id)==null)
		{
			return false;
		}
		else
		{	
			//ask about password
			userStorage.replace(id, changeUser);
			return true;
		}
	}
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
