# Text Schema Validator
This project requires 2 parts, ValidatorFrontend implemented using AngularJS framework and ValidatorRESTService implmented using Jersey framework.

## Getting Started
Download both projects first, then install the dependencies as following:

### ValidatorFrontend
All necessary components are already installed.
#### Run the Application
We have preconfigured the project with a simple development web server.  The simplest way to start
this server is to open a terminal window, navigate to the project folder, and run the following command:

```
npm start
```

Now browse to the app at `http://localhost:8000/app/index.html`.


#### Directory Layout

```
app/                 --> all of the source files for the application
  css/                  --> stylesheets
  components/           --> all app specific modules
  bower_components/     --> dependencies install through bower
  resourceJS/           --> other dependencies
  images/               --> GUI images
  services/             --> angularJS services
    services.js           --> service that interacts with RESTful server
  js/                   --> main application module
    app.js                --> app definition
    mainCtrl.js           --> control component that processes user command
    directives.js         --> upload chosen input files and download displayed data in view 
    modalCtrl.js          --> controller for instruction modal view
  index.html            --> app layout file (the view of the app)
karma.conf.js         --> config file for running unit tests with Karma
e2e-tests/            --> end-to-end tests
  lib/                --> mock RESTful server
    httpbackend.js        --> mock backend
    httprequest.js        --> mock http request function
    httpresponse.js       --> mock http request function
    javascriptbuilder.js  --> mock http backend controller logic
  testFiles/          --> input files for tests
  protractor.conf.js    --> Protractor config file
  testFrontend.js         --> GUI behavior scenarios to be run by Protractor
  testMockHttp.js         --> communicating with backend scenario to be run by Protractor
```

#### Testing
To run the protractor tests, navigate to e2d-tests folder and execute the following command:
```
protractor protractor.conf.js
```


### ValidatorRESTService