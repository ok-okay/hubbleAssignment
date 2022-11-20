<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Enter OTP</title>
</head>
<body>
<p>${msg}</p>
<form action="/auth/verify" method="POST">
	<label for="identifier">Phone number(With country code):</label><br>
	<input type="text" id="identifier" name="identifier" value="${identifier}" readonly><br>
	<input type="password" id="otp" name="otp" placeholder="Enter OTP"><br>
	<input type="hidden" id="medium" name="medium" value="PHONE"><br>
	<input type="submit" value="SignIn/SignUp">
</form>

</body>
</html>