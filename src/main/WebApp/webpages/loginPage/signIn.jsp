<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Enter OTP</title>
</head>
<body>
<p>${data.get("msg")}</p>
<form action="/auth/verify" method="POST">
	<label for="identifier">${data.get('medium')}</label><br>
	<input type="text" id="identifier" name="identifier" value="${data.get('identifier')}" readonly><br>
	<input type="password" id="otp" name="otp" placeholder="Enter OTP"><br>
	<input type="hidden" id="medium" name="medium" value="${data.get('medium')}"><br>
	<input type="submit" value="${data.get('submit')}">
</form>

</body>
</html>