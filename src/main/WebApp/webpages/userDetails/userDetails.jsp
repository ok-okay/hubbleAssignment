<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User</title>
</head>
<body>
<h1>Hello ${userData.get("firstName")}!</h1>
<form method="POST" action="${userData.get('userId')}">
<table>
<tr>
<th>Personal Details</th>
<th>Address Details</th>
</tr>
<tr>
<td>
  <label for="firstName">First name:</label><br>
  <input type="text" id="firstName" name="firstName" value="${userData.get('firstName')}" required><br>
  <label for="lastName">Last name:</label><br>
  <input type="text" id="lastName" name="lastName" value="${userData.get('lastName')}"><br>
  <label for="email">Email ID:</label><br>
  <input type="text" id="email" name="email" value="${userData.get('email')}"><br>
  <label for="phone">Phone Number:</label><br>
  <input type="text" id="phone" name="phone" value="${userData.get('phone')}" readonly><br>
</td>
<td>
  <label for="houseDetails">House Details:</label><br>
  <input type="text" id="houseDetails" name="houseDetails" value="${userData.get('houseDetails')}"><br>
  <label for="locality">Locality:</label><br>
  <input type="text" id="locality" name="locality" value="${userData.get('locality')}"><br>
  <label for="country">Country:</label><br>
  <input type="text" id="country" name="country" value="${userData.get('country')}"><br>
  <label for="pincode">Pincode:</label><br>
  <input type="text" id="pincode" name="pincode" value="${userData.get('pincode')}"><br>
</td>
</tr>
</table>

  <input type="submit" value="Update">
</form>
</body>
</html>