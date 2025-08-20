<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ page import="java.util.ArrayList" %>
<%
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    if(username != null && password != null) {
        if(username.equals("test") && password.equals("1234")) {
            session.setAttribute("loggedInUser", username);

            if (session.getAttribute("cartList") == null) {
                session.setAttribute("cartList", new ArrayList()); // <> 없이
            }

            response.sendRedirect("main.jsp");
        } else {
            request.setAttribute("errorMsg", "아이디 또는 비밀번호가 잘못되었습니다.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    } else {
        response.sendRedirect("login.jsp");
    }
%>
