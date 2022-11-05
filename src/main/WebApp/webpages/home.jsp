<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home page</title>
</head>
<body>
<h1>Create account/Login</h1>
<form action="/users" method="POST">
	<label for="phone">Phone number(With country code):</label><br>
	<input type="text" id="phone" name="phone" placeholder="Enter Phone" required><br>
	<input type="submit" value="Create user">
</form>
</body>
</html>