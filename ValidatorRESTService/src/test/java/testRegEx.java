import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.JsonObject;

import com.workday.jersey.schemaFilesService;


public class testRegEx {
	
	public static void main(String[] args) {
		
		JsonObject schemas = null;
		try {
//			schemas = new SVNUtil().updateSchemas();
			System.out.println("main: " + schemas.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
