package attr;

import java.sql.*;
import javax.swing.*;
import activity.*;

public abstract class User {
	protected String userId;
	protected String password;
	protected int status;
	public User(String userId) {
		if (!userId.isEmpty())
			this.userId = userId;
		else
			throw new IllegalArgumentException("Fill in the User ID");
	}
	
	public abstract void fetch();
	
	public String getUserId() {
		return userId;
	}
	public void setStatus(int stts) {
		this.status = stts;
	}
	public void setPassword(String passwd) {
		if (!passwd.isEmpty())
			this.password = passwd;
		else
			throw new IllegalArgumentException("Fill in the password");
	}
	
	public static int checkStatus(String uid, String pass) {
		int result = -1;
		String query = "SELECT `userId`, `password`, `status` FROM `login`;";     
		ResultSet rs = null;
		try {
			Database db = Database.getInstance();
			rs = db.DBquery(query);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			while(rs.next()) {
			    String userId = rs.getString("userId");
			    String password = rs.getString("password");
				int status = rs.getInt("status");
				if(userId.equals(uid) && password.equals(pass)) {
					rs.close();
					result = status;
				}
			}
				if (rs != null) {
					rs.close();
				}
			}
		catch (SQLException e) {	
			e.printStackTrace();
		}

		return result;
		}
	
	public void changePassword(ChangePasswordActivity a, String oldPass, String newPass) {
		String query = "UPDATE `login` SET `password`='"+newPass+"' WHERE (`userId`='"+this.userId+"' AND `password`='"+oldPass+"');";
		Database db;
		boolean res;
		try {
			db = Database.getInstance();
			res = db.DBupdate(query);
		} catch (SQLException e) {
			res = false;
			e.printStackTrace();
		}
		
		if (res==true)
		{
			JOptionPane.showMessageDialog(null,"Password Updated!");
			a.setVisible(false);
		}
		else {
			JOptionPane.showMessageDialog(null,"Password didn't match!");
		}
	}
}