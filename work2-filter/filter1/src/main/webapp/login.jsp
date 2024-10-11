<%--
  Created by IntelliJ IDEA.
  User: mingyue729
  Date: 2024/10/6
  Time: 14:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>登录</title>
  <style>
    body, html {
      height: 100%;
      margin: 0;
      display: flex;
      justify-content: center;
      align-items: center;
      font-family: Arial, sans-serif;
    }
    .login-container {
      width: 300px;
      padding: 20px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      background: #f7f7f7;
    }
    .login-container h2 {
      text-align: center;
      margin-bottom: 20px;
    }
    .login-container .error-message {
      text-align: center;
      color: red;
      margin-bottom: 15px;
    }
    .login-container form div {
      margin-bottom: 15px;
    }
    .login-container label {
      display: block;
      margin-bottom: 5px;
    }
    .login-container input[type="text"],
    .login-container input[type="password"] {
      width: 100%;
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 4px;
      box-sizing: border-box;
    }
    .login-container button {
      width: 100%;
      padding: 10px;
      border: none;
      border-radius: 4px;
      background-color: #3f81e1;
      color: white;
      cursor: pointer;
    }
    .login-container button:hover {
      background-color: #1869bf;
    }
  </style>
</head>
<body>
<div class="login-container">
  <h2>登陆页面</h2>
  <% if (request.getAttribute("errorMessage") != null) { %>
  <p class="error-message"><%= request.getAttribute("errorMessage") %></p>
  <% } %>
  <form action="login" method="post">
    <div>
      <label for="username">用户名:</label>
      <input type="text" id="username" name="username" required>
    </div>
    <div>
      <label for="password">密码:</label>
      <input type="password" id="password" name="password" required>
    </div>
    <div>
      <button type="submit">登陆</button>
    </div>
  </form>
</div>
</body>
</html>