package attr;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class Employee extends User {
	private String employeeName;
	private String phoneNumber;
	private String role = "General";
	public static String[] columnNames = {"EmployeeID", "EmployeeName", "PhoneNumber", "Role", "Salary"};
	//public static String[] roles = {"General", "Manager"};
	private double salary;
	public Employee(String userId) {
		super(userId);
		this.setStatus(0);
	}
	
	public void setEmployeeName(String name) {
		if (!name.isEmpty())
			this.employeeName = name;
		else
			throw new IllegalArgumentException("Fill in the name");
	}
	public void setPhoneNumber(int num) {
		this.phoneNumber = "+972"+num;
	}
	/*public void setRole(String role) {
		this.role = role;
	}*/
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public String getRole() {
		return role;
	}
	public double getSalary() {
		return salary;
	}
	
	public void createEmployee() {
		String query1 = "INSERT INTO `login` VALUES ('"+userId+"','"+password+"',"+status+");";
		String query2 = "INSERT INTO `employee` VALUES ('"+userId+"','"+employeeName+"','"+phoneNumber+"','"+role+"', '"+salary+"');";
        try {
    		Database db = Database.getInstance();
        	db.DBupdate(query1);
        	db.DBupdate(query2);
			System.out.println("data inserted");
			JOptionPane.showMessageDialog(null,"Account Created!");
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Failed to create account!");
			System.out.println("Exception : " +ex.getMessage());
        }
	}
	
	public void fetch() {
		String query = "SELECT `userId`, `employeeName`, `phoneNumber`, `role`, `salary` FROM `employee` WHERE userId='"+this.userId+"';";     
        try {
    		Database db = Database.getInstance();
        	ResultSet rs = db.DBquery(query);
			while(rs.next()) {
				this.employeeName = rs.getString("employeeName");
				this.phoneNumber = rs.getString("phoneNumber");
				this.role = rs.getString("role");
				this.salary = rs.getDouble("salary");
			}
		}
        catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
        }
	}
	
	public void updateEmployee(String name, int phone, String role, double salary) {
		String query = "UPDATE `employee` SET `employeeName`='"+name+"', `phoneNumber`='+972"+phone+"', `role`='"+role+"', `salary`="+salary+" WHERE `userId`='"+this.userId+"';";
        try {
        	Database db = Database.getInstance();
        	db.DBupdate(query);
			JOptionPane.showMessageDialog(null,"Information Updated!");
			this.employeeName = name;
			this.phoneNumber = "+972"+phone;
			this.role = role;
			this.salary = salary;
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Failed to update account!");
			System.out.println("Exception : " +ex.getMessage());
        }
	}
	
	public void deleteEmployee() {
		String query1 = "DELETE FROM `login` WHERE `userId`='"+this.userId+"';";
		String query2 = "DELETE FROM `employee` WHERE `userId`='"+this.userId+"';";
        try {
        	Database db = Database.getInstance();
        	db.DBupdate(query1);
        	db.DBupdate(query2);
			System.out.println("data deleted");
			JOptionPane.showMessageDialog(null,"Account Deleted!");
			this.userId = "";
			this.employeeName = "";
			this.phoneNumber = "";
			this.role = "";
			this.salary = 0;
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Failed to delete account!");
			System.out.println("Exception : " +ex.getMessage());
        }
	}
	
	public static DefaultTableModel searchEmployee(String keyword, String byWhat) {
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columnNames);
		String query = "SELECT * FROM `employee` WHERE `userId`='"+keyword+"';";
		if (byWhat.equals("By Name"))
			query = "SELECT * FROM `employee` WHERE `employeeName` LIKE '%"+keyword+"%';";
		else {}
		ResultSet rs = null;
        try {
        	Database db = Database.getInstance();
        	rs = db.DBquery(query);
			System.out.println("results received");
			
			while(rs.next()) {
				model.addRow(new Object[]{rs.getString("userId"), rs.getString("employeeName"), rs.getString("phoneNumber"), rs.getString("role"), rs.getString("salary")});
			}
		}
        catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
        }
		return model;
	}
}