package com.sheremet;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.DatatypeConverter;


public class LoginRegUtils {
	private static final String table = "users123";
	private static final String host = "sql4.freemysqlhosting.net";
	private static final String dbname = "sql4103546";
	private static final String user = "sql4103546";
	private static final String pass = "BDr8Rl3RD8";
	private DBUtils db; 
	public LoginRegUtils() {
		db = new DBUtils(host, dbname, user, pass);
	}
	public String login(String login, String password){
		System.out.println("LOGIN: login="+ login+"; pass="+password);
		try {
			String loginQuery = "select * from "+ table+" where login='"+login.replaceAll("'", "")+"' and hash='"+md5(password)+"'";//DSL.select( DSL.field("hash")).from(DSL.table(table)).where(DSL.field("login").eq(login).and(DSL.field("hash").eq(md5(password)))).getSQL();
			System.err.println(loginQuery);
			ResultSet rs;
			rs = db.queryNoParams(loginQuery);
			if (rs.next()){
				return rs.getString(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String check(String hash){
		System.out.println("CHECK: hash="+ hash);
		try {
			String checkQuery ="select login from "+table+" where hash='"+hash.replaceAll("'", "")+"'"; // DSL.select(DSL.field("login")).from(DSL.table(table)).where("hash",hash).getSQL();
			System.err.println(checkQuery);
			ResultSet rs;
			rs = db.queryNoParams(checkQuery);
			if (rs.next()){
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String register(String login, String password, String passwordConfirm){
		System.out.println("REGISTER: login="+ login+"; pass="+password+"; passConfirm="+passwordConfirm);
		try {
			if (!password.equals(passwordConfirm)) return null;
			String checkQuery = "select * from "+table+" where login='"+login.replaceAll("'", "")+"'"; //DSL.select(DSL.field("login")).from(DSL.table(table)).where("login",login).getSQL();
			System.err.println(checkQuery);
			ResultSet rs;
			rs = db.queryNoParams(checkQuery);
			if (rs.next()){
				return null;
			}
			String registerQuery ="insert into "+table+" values('"+login.replaceAll("'", "")+"','"+md5(password) +"')";//+ DSL.insertInto(DSL.table(table)).columns(DSL.field("login"), DSL.field("hash")).values("login", md5(password)).getSQL();
			System.err.println(registerQuery);
			db.execute(registerQuery);
			return md5(password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static String md5(String s) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		m.update(s.getBytes(),0,s.length());
		return DatatypeConverter.printHexBinary(m.digest());
	}
	public static void main(String[] args) {
		//		String registerQuery=DSL.insertInto(DSL.table(table)).set(DSL.field("login"), "login").set(DSL.field("hash"), "789").getSQL();
		//		System.out.println(registerQuery);
	}
	public boolean delete(String login) {
		System.out.println("DELETE: login="+ login);
		try {
			String delQuery = "delete from "+ table+" where login='"+login.replaceAll("'", "")+"'";
			System.err.println(delQuery);
			db.execute(delQuery);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
