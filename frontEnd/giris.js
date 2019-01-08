
$(function(){
var id;
var token;
$( "#sign" ).click(function() {		  
	console.log("abi ben calisiyom");
	var settings = {
	"async": true,
	"crossDomain": true,
	"url": "http:/localhost:9000/user",
	"method": "POST",
	"headers": {
	  "Content-Type": "application/json",
	  "Cache-Control": "no-cache"
	},
	"processData": false,
	"data": "{\n  \"username\": \"" + $("#username").val() + "\",\n  \"password\": \"" + $("#password").val() + "\"\n}"
  }
  
  $.ajax(settings).done(function (response) {
		 if (response.type == "doctor"){
			window.location.replace("doctor.html?" + response.token + "&" + response.id);
		  } 
		  else if (response.type == "nurse"){
			window.location.replace("nurse.html?" + response.token + "&" + response.id);
		  } 
		  else if (response.type == "relative"){
			window.location.replace("relative.html?" + response.token + "&" + response.id);
		  } 
		  
		  else if (response.type == "admin"){
			window.location.replace("admin.html?" + response.token + "&" + response.id);
		  } 
		  
		  else if (response.type == "patient"){
			window.location.replace("patient.html?" + response.token + "&" + response.id);
		  } 
		  else {
			  document.write("incorrect");
		  }
		}).fail(function(error) {
			alert("Invalid Username Or Pasword")
  	});
		  
  
	  
  });


});