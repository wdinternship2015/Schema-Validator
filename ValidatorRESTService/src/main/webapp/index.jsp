<!DOCTYPE html>
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
				  <option value="XML To Text">XML to text</option>
			</select>
		</p>
 		<p>
		String input : <input type="text" name="direction2" size="45"/>
	   </p>
	   <input type="submit" value="processInput" />
	</form>
 
 <form name="loginValidation" enctype='application/json'>
  <p>username: <input name='username'></p>
  <p>password: <input name='password'></p>
  <p><button onclick="scriptSubmit()">login</button></p>
  <script type="text/javascript">

			function scriptSubmit() {
				var form = document.forms.namedItem("loginValidation");
				var data = {};
				for (var i = 0, ii = form.length; i < ii; ++i) {
					var input = form[i];
					if (input.name) {
						data[input.name] = input.value;
					}
				}
				var xhr = new XMLHttpRequest();
				xhr.open('POST',
						'webapi/login',
						true);
				  xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
				xhr.onload = function() {
					if (xhr.status == 200) {
						var result = xhr.responseText;
						document.getElementById("loginResult").value = result;
					} else {
						var result = xhr.responseText;
						document.getElementById("loginResult").value = result;
					}
				};
				xhr.onerror = function() {
					alert('Woops, there was an error making the request.');
				};
				xhr.send(JSON.stringify(data));
			}
		</script>
		<p>
		Login Response Text : <textarea id="loginResult"></textarea>
	   </p>
</form>
 
</body>
</html>
