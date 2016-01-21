package com.sheremet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class PracticeJavaEEServlet extends HttpServlet {
	private static LoginRegUtils utils = new LoginRegUtils();
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		System.out.println("POST:\nip="+req.getRemoteAddr()+";\nq: "+req.getQueryString()+"; p: "+req.getServletPath());
		try{
			String acc = checkCookie(req.getCookies());
			String act = req.getParameter("act");
			String login = req.getParameter("login");
			String pass = req.getParameter("pass");
			String pass_rep = req.getParameter("pass_rep");
			if (act!=null){
				if (act.equalsIgnoreCase("REGISTER")){
					if (login!=null&&pass!=null&&pass_rep!=null){
						String hash = utils.register(login, pass, pass_rep);
						if (hash!=null)
							resp.addCookie(new Cookie("token", hash));
					}
				}
				if (act.equalsIgnoreCase("DELETE")){
					if (acc!=null){
						if(utils.delete(acc)){
							Cookie delete = new Cookie("token", "deleted");
							delete.setMaxAge(0);
							resp.addCookie(delete); 
						}
					}
				}
				if (act.equalsIgnoreCase("LOGIN")){
					if (login!=null&&pass!=null){
						utils = new LoginRegUtils();
						String hash = utils.login(login, pass);
						if (hash!=null)
							resp.addCookie(new Cookie("token", hash));
					}
				}
			}
		}finally{
			resp.sendRedirect("/EE1/do");
		}
	}
	private String checkCookie(Cookie[] cookies) {

		if (cookies==null)return null;
		try{
			for(Cookie c: cookies){
				if (c.getName().equals("token")){
					return utils.check(c.getValue());
				}
			}
		}catch(NullPointerException e){}
		return null;
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("GET:\nip="+req.getRemoteAddr()+";\nq: "+req.getQueryString()+"; p: "+req.getServletPath());
		String act =req.getParameter("act");
		if (act!=null&&act.equalsIgnoreCase("logout")){
			Cookie delete = new Cookie("token", "deleted");
			delete.setMaxAge(0);
			resp.addCookie(delete); 
			resp.sendRedirect("/EE1/login.html");
		}
		resp.setContentType("text/html");
		String login = checkCookie(req.getCookies());
		if (login==null){
			resp.sendRedirect("/EE1/login.html");
			return;
		}
		resp.getWriter().println("<html><head><title>"+login+"</title><style>body{font-family: Tahoma;} .outer {width: 100%;text-align: center;} \r\n inner {display: inline-block;}</style></head><body>");
		resp.getWriter().println("<div class='outer'><div class='inner'><h1>Hello, "+login+"!</h1><br>");
		resp.getWriter().println("<h3>You can</h3><br>");
		resp.getWriter().println("<a href='do?act=logout'>LOGOUT</a>");
		resp.getWriter().println("<br><br>");
		resp.getWriter().println("<form action='do?act=delete' method='post'><input type='submit' value='DELETE ACCOUNT'></form></div></div>");
		resp.getWriter().println("</body></html>");
	}
}
