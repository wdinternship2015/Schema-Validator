'use strict';
var backEndMocks = require('./httpBackEndMocks');

var ptor;


describe('text schema validator', function() {


  it('should automatically open instruction modal window', function() {
    browser.get('index.html');
    expect(element(by.className('modal-header')).isDisplayed()).toBe(true);
    element(by.id('modalOK')).click();
	expect(element(by.className('modal-header')).isPresent()).toBe(false);    
  });


  describe('loding input files', function() {

    beforeEach(function() {
      browser.get('index.html');
      element(by.id('modalOK')).click();
    });

    var path = require('path');
    
    it('should upload if text input file type is correct', function() {   	
    	var fileToUpload = 'testFiles/transamerica_expected_text.txt',
        absolutePath = path.resolve(__dirname, fileToUpload);
    	element(by.id('inputFile')).sendKeys(absolutePath);  
    	expect(element(by.id('inputFile')).isPresent()).toBe(true);   
    	expect(element(by.id('inputFile')).getAttribute('value')).not.toEqual('');    
    });

    it('should refuse upload if input file type is incorrect', function() {   	
    	var fileToUpload = 'testFiles/transamerica_inbound_loans.xsd',
        absolutePath = path.resolve(__dirname, fileToUpload);
    	element(by.id('inputFile')).sendKeys(absolutePath);  
    	expect(element(by.id('inputFile')).getText()).toEqual('');    
    });
    
    it('should upload if schema input file type is correct', function() {   	
    	var fileToUpload = 'testFiles/transamerica_inbound_loans.xsd',
        absolutePath = path.resolve(__dirname, fileToUpload);
    	element(by.id('schemaFile')).sendKeys(absolutePath);  
    	expect(element(by.id('schemaFile')).isPresent()).toBe(true);    
    });
    
    it('should refuse upload if schema file type is incorrect', function() {   	
    	var fileToUpload = 'testFiles/transamerica_expected_text.txt',
        absolutePath = path.resolve(__dirname, fileToUpload);
    	element(by.id('schemaFile')).sendKeys(absolutePath);  
    	expect(element(by.id('schemaFile')).getText()).toEqual('');    
    });
    
  });
  
  describe('select transformation direction', function() {

	    beforeEach(function() {
	      browser.get('index.html');
	      element(by.id('modalOK')).click();
	    });

	    it('should always has value', function() {   	
	    	expect(element(by.id('direction')).getAttribute('value')).not.toEqual('');    
	    });	    
  });
  
  describe('submit form', function() {

	    beforeEach(function() {
	      browser.get('index.html');
	      element(by.id('modalOK')).click();
	    });

	    it('should get file name and result message', function() {   	
	    	ptor = protractor.getInstance();  
	    	ptor.addMockModule('httpBackEndMocks', backEndMocks.build([backEndMocks.serverMsg]));
	    	
	    	var fileToUpload = 'testFiles/transamerica_expected_text.txt',
	        absolutePath = path.resolve(__dirname, fileToUpload);
	    	element(by.id('inputFile')).sendKeys(absolutePath); 
	    	
	       	var fileToUpload = 'testFiles/transamerica_inbound_loans.xsd',
	        absolutePath = path.resolve(__dirname, fileToUpload);
	    	element(by.id('schemaFile')).sendKeys(absolutePath);  
	    	
	    	element(by.id('submitForm')).click();
	    	
	    	expect(element(by.id('enter_name')).getAttribute('value')).toEqual('filename.ext'); 
	    	expect(element(by.id('comment')).getAttribute('value')).toEqual('result string from server'); 
	    });	    
  });

});

