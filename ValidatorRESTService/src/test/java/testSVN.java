import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



public class testSVN {

	public static void main(String[] args) {
//		StringBuffer output = new StringBuffer();
//		String command = "svn list -R svn+ssh://code.workday.com/sandbox | grep '.*/textschematestxsd/.*\\.xsd$'";
//		String command = "/bin/sh -c svn list -R svn+ssh://code.workday.com/sandbox | grep '.*/textschematestxsd/.*\\.xsd$'";
//		System.out.println(command);
		Process p;
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonArrayBuilder xsdOptsBuilder = factory.createArrayBuilder();
		Pattern pattern = Pattern.compile("/([^/]*\\.xsd)$");
		String namesJson = "";

		
		try {
//	        Process exportProc = Runtime.getRuntime().exec("sh src/main/scripts/exportFile.sh " + "branches/textschematestxsd/'transamerica_inbound_loans copy.xsd'");
//	    	exportProc.waitFor();
//	    	System.out.println("exportProc done");
//	    	 BufferedReader reader = new BufferedReader(new InputStreamReader(exportProc.getInputStream()));
			
			p = Runtime.getRuntime().exec("sh src/main/scripts/getSchemaFileNames.sh ");
	        p.waitFor();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

	        String line = "";           
	        String op = "svn export";
	        String repo = "svn+ssh://code.workday.com/sandbox/";
	        String target = "src/main/scripts/svnFiles --force";
	        char space = ' ';
	        PrintWriter writer = new PrintWriter("src/main/scripts/exportSchemaScript.sh", "UTF-8");
	        PrintWriter namesWriter = new PrintWriter("src/main/scripts/schemaFileNames.txt", "UTF-8");

	        while ((line = reader.readLine())!= null) {
	        	System.out.println(line);
	        	Matcher matcher = pattern.matcher(line);
	        	if (matcher.find()) {
	        		int start = matcher.start() + 1;
	        		String optName = matcher.group(1);
	        		xsdOptsBuilder.add(factory.createObjectBuilder()
	        				.add("option", optName));
			        writer.println(op + space + repo +  line.substring(0, start) + "\"" + optName + "\"" + space + target);
	        	}
	        }	        
	        writer.close();
	        
	        p = Runtime.getRuntime().exec("sh src/main/scripts/exportSchemaScript.sh");
	        p.waitFor();
	        while ((line = reader.readLine())!= null) {
	        	System.out.println(line);
	        }
	        reader.close();
	        
	        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	        Date dateobj = new Date();
	        JsonObject schemalist = factory.createObjectBuilder()
	        							.add("date", df.format(dateobj))
	        							.add("options", xsdOptsBuilder).build();
	        
//	        namesJson = xsdOptsBuilder.build().toString();
	        namesJson = schemalist.toString();
	        namesWriter.print(namesJson);
	        namesWriter.close();

	        JSONObject namesObj = (JSONObject) new JSONParser().parse(new FileReader("src/main/scripts/schemaFileNames.txt"));
	        
	        System.out.println("from file date: " + namesObj.get("date").toString() + "\nfrom file: " + namesObj.get("options").toString());
	        

	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    	
	    }

	    System.out.println(namesJson);
	}
}
