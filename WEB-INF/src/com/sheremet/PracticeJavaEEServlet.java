package com.sheremet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class PracticeJavaEEServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try{
			if (checkCookie(req.getCookies())!=null){
				return;
			}
			String act = req.getParameter("act");
			String login = req.getParameter("login");
			String pass = req.getParameter("pass");
			String pass_rep = req.getParameter("pass_rep");
			if (act!=null){
				LoginRegUtils utils = new LoginRegUtils();
				if (act.equalsIgnoreCase("REGISTER")){
					if (login!=null&&pass!=null&&pass_rep!=null){
						String hash = utils.register(login, pass, pass_rep);
						if (hash!=null)
							resp.addCookie(new Cookie("token", hash));
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
		LoginRegUtils utils = new LoginRegUtils();
		if (cookies==null)return null;
		for(Cookie c: cookies){
			if (c.getName().equals("token")){
				return utils.check(c.getValue());
			}
		}
		return null;
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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
		resp.getWriter().println("<html><body>");
		resp.getWriter().println("Hello, "+login+"!");
		resp.getWriter().println("<br><a href='do?act=logout'>LOGOUT</a>");
		resp.getWriter().println("</body></html>");
	}
}
