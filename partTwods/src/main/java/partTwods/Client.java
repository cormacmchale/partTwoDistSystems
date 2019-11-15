package partTwods;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.j2objc.annotations.ReflectionSupport.Level;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.PasswordServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class Client {
	
	//save hashRequest results for request for validation
	private ByteString hashedPassword;
	private ByteString salt;
	
	//initialize applicable fields
    private static final Logger logger = Logger.getLogger(Client.class.getName());  
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
						//users.put(newUser.getUserId(), new UserObject(newUser.getUserId(), newUser.getUserName(), newUser.getEmail(), newUser.getPassword(),
																//passwordAndHash.getHashedPassword(), passwordAndHash.getSalt()));
						System.out.println(passwordAndHash.getHashedPassword().toString());
						System.out.println(passwordAndHash.getSalt().toString());
						System.out.println("User added");
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
        		
    		}   
    		    //not working for the moment
        		//logger.log(Level.NATIVE_ONLY,"RPC failed: {0}", ex.getStatus());
    	}   
    
    //async method to check password here
    //public boolean asyncPasswordValidation(String p, ByteString hp, ByteString s)
    //{
 
    //	StreamObserver<BoolValue> responseObserver = new StreamObserver<BoolValue>()
    //	{
	//		@Override
	//		public void onNext(BoolValue value) {
				// TODO Auto-generated method stub
	//			if(value.getValue())
	//			{
	//				isCorrect = true;
	//			}
	//			else
	//			{
	//				isCorrect = false;
	//			}
				
	//		}
	//		@Override
	//		public void onError(Throwable t) {
				// TODO Auto-generated method stub
				//dont care
	//		}

	//		@Override
	//		public void onCompleted() {				
	//			System.exit(0);
	//		}   		
    //	};   	
     //   try {
     //   	 asyncPasswordService.validate(ValidatorRequest.newBuilder().setPassword(p)
     //       														   .setHashedPassword(hp)
     //       														   .setSalt(s).build(), responseObserver);

     //   	while(!isCorrect)
      //  	{
        		//wait until it has been changed 
      //  	}
      //    	return isCorrect;
         
     //   } catch (
      //          StatusRuntimeException ex) {
            //logger.log(Level.WARNING, "RPC failed: {0}", ex.getStatus());
      //      return isCorrect;
     //   }
  ///  }
}//end client
