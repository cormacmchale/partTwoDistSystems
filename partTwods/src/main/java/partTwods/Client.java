package partTwods;

import java.util.concurrent.TimeUnit;
//import java.util.logging.Logger;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.PasswordServiceGrpc;
import ie.gmit.ds.ValidatorRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class Client {
		
	//initialize applicable fields
    //private static final Logger logger = Logger.getLogger(Client.class.getName());  
    private final ManagedChannel channel;
    private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordService;
    private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService;

    public Client(String host, int port) {
    	
        channel = ManagedChannelBuilder
        		.forAddress(host, port)
                .usePlaintext()
                .build();      
        syncPasswordService = PasswordServiceGrpc.newBlockingStub(channel);
        asyncPasswordService = PasswordServiceGrpc.newStub(channel);
    }
    
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
    
    //method for making the request for a hashed password with salt
    //we should send a message (hashrequest in this) and wait on a message back (hash response)
    //add the functionality to ask for a request in console window
    public void requestAHash(UserObject newUser)
    {
    	StreamObserver<HashResponse> responseObserver = new StreamObserver<HashResponse>()
    			{
					@Override
					public void onCompleted() {
						//TODO Auto-generated method stub
						//System.exit(0);
					}
					@Override
					public void onError(Throwable t) {
						// TODO Auto-generated method stub	
					}
					@Override
					public void onNext(HashResponse passwordAndHash) {
						//add to the database here
						PretendDatabase d = PretendDatabase.getInstance();
						UserObject user = new UserObject(newUser.getUserId(), newUser.getUserName(), newUser.getEmail(), newUser.getPassword(),
														 passwordAndHash.getHashedPassword(), passwordAndHash.getSalt());
						d.addUser(user);
					}
    			};
    		try 
    		{
    			asyncPasswordService.hash(HashRequest.newBuilder()
        	    			                   .setUserId(newUser.getUserId())
        	    			                   .setPassword(newUser.getPassword())
        	    			                   .build(),responseObserver);
    	    }
        	catch (StatusRuntimeException ex)
    		{
    		    //not working for the moment
        		System.out.println("here");     		
    		}   
    	}   
    
    //async method to check password here
    public boolean syncPasswordValidation(LoginObject login, ByteString hp, ByteString s)
    {
    	ValidatorRequest request = ValidatorRequest.newBuilder().setPassword(login.getPassword()).setHashedPassword(hp).setSalt(s).build();
    	BoolValue result = BoolValue.newBuilder().getDefaultInstanceForType();
    	try
    	 {
    		 result = syncPasswordService.validate(request); 
    		 System.out.println(result);
    		 return result.getValue();
    	 }
         catch (StatusRuntimeException ex) 
     	 {
 	       System.out.println(ex.getLocalizedMessage());
 	       return false;
         }  	
    }

}//end client
