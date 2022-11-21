<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home page</title>
</head>
<body>
<p>${msg}</p>
<h1>SignIn/SignUp</h1>
<form action="/auth" method="POST">
	<label for="identifier">Phone number:</label><br>
	<input type="text" id="identifier" name="identifier" placeholder="Enter Phone" required><br>
	<input type="hidden" id="medium" name="medium" value="PHONE">
	<input type="submit" value="Generate OTP">
</form>
</body>
</html>