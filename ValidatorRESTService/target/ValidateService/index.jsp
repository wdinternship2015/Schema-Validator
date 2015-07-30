<html>
<body>
    <h2>Jersey RESTful Web Application!</h2>
    <p><a href="webapi/upload">Jersey resource</a>
    
    
    	<h1>File Upload with Jersey</h1>
 
	<form action="webapi/runSchema/byFileName" method="post" enctype="multipart/form-data">
 
	   <p>
		Select a file : <input type="file" name="inputFile" size="45" />
	   </p>
	   <p>
		Select schema file : <input type="file" name="schemaFile" size="45" />
	   </p>
	   <p><select name="direction">
				  <option value="txt To XML">Text to XML</option>
				  <option value="CSV To XML">CSV to XML</option>
				  <option value="XML To Txt">XML to text</option>
			</select>
		</p>
 		<p>
		String input : <input type="text" name="direction2" size="45"/>
	   </p>
	   <input type="submit" value="processInput" />
	</form>
 
</body>
</html>
