angularProj 
	- http://localhost:8000/app/index.html#
  	- e2e-tests
  		testFrontend.js tests
  			1. if the instruction modal window opens
  			2. if txt/xml file input is accepted by loading a .txt file from e2e-tests/testFiles/ folder
  			3. if txt/xml file input is rejected by loading a .xsd file from e2e-tests/testFiles/ folder
  			4. if schema file input is accepted by loading a .xsd file from e2e-tests/testFiles/ folder
  			5. if schema file input is rejected by loading a .txt file from e2e-tests/testFiles/ folder
  		testMockHttp.js tests
  			1. if submitting a valid form receives a response from REST service at 
  				http://localhost:8080/ValidateService/webapi/runSchema

ValidateService
	- RESTful back end
	- ValidateService.war deploy on tomcat
	- angularProj calls REST services
	- error message and transformation result are send back to front end as a string in response
	- auto detect transformation direction based on input file type
	- services:
		transformation:
			http://localhost:8080/ValidateService/webapi/runSchema
			transforms input file according to specified schema
		login:
			http://localhost:8080/ValidateService/webapi/login
			stub authentication service granting access to username and password of at least 2 characters


