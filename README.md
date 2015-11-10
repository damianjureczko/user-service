# user-service

### This is an example of service built with Spray and Akka. 

Check out more about [Spray](http://spray.io/) and [Akka](akka.io) as they are very powerful tools.  

## Branches

There are separate branches for different patterns:

- **actor-per-request** - contains solution based on "Actor Per Request" pattern
- **ask-per-request** - contains solution using Akka's "ask patter"
 
## How to start 

1. Clone the git repository.  

		$ git clone https://github.com/DamianJureczko/user-service.git
	
2. Change directory to user-service.  

		$ cd user-service
	
3. Start SBT.  

		$ sbt
		
### Quick SBT tutorial

1. Clean project.

		> clean
	
2. Compile and test project.

		> test
	
3. Start service.
	
		> re-start
		
4. Stop service.

		> re-stop
		
## Running service

From SBT type **re-start** command. By default service is running on **8080** port. It can be change by setting environment variable.

		$ export APP_PORT=90001
	
## API

Check the **src/main/resources/api** folder, it contains the API of the service written in RAML. 
For more information about RAML go to the official page [raml.org](raml.org)

It's possible to display and edit RAML locally with [https://github.com/colthreepv/express-raml-store](https://github.com/colthreepv/express-raml-store)  
