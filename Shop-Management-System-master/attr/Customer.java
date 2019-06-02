package attr;

import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import activity.*;

public class Customer extends User {
	private String customerName;
	private String phoneNumber;
	private String address;
	public static String[] columnNames = {"PurchaseID", "ProductID", "ProductName", "Amount", "Cost", "Date"};
	public static String[] columnName = {"CustomerID", "CustomerName", "PhoneNumber", "Address"};
	public Customer(String userId){
		super(userId);
		
		this.setStatus(1);
	}
	
	public void setCustomerName(String name) {
		if (!name.isEmpty())
			this.customerName = name;
		else
			throw new IllegalArgumentException("Fill in the name");
	}
	public void setPhoneNumber(int num) {
		this.phoneNumber = "+972"+num;
	}
	public void setAddress(String address) {
		if (!address.isEmpty())
			this.address = address;
		else
			throw new IllegalArgumentException("Fill in the address");
	}
	public String getCustomerName() {
		return customerName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getAddress() {
		return address;
	}

	public void createCustomer(JFrame sa) {
		String query1 = "INSERT INTO `login` VALUES ('"+userId+"','"+password+"',"+status+");";
		String query2 = "INSERT INTO `customer` VALUES ('"+userId+"','"+customerName+"','"+phoneNumber+"','"+address+"');";
		try {
			Database db = Database.getInstance();
			db.DBupdate(query1);
			db.DBupdate(query2);
			System.out.println("data inserted");
			JOptionPane.showMessageDialog(sa,"Account Created!");
			sa.setVisible(false);
			new LoginActivity().setVisible(true);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(sa,"Failed to create account!");
			System.out.println("Exception : " +e.getMessage());
		}
		System.out.println(query1);
		System.out.println(query2);
	}
	
	public void fetch() {
		String query = "SELECT `userId`, `customerName`, `phoneNumber`, `address` FROM `customer` WHERE userId='"+this.userId+"';";     
		ResultSet rs = null;
		Database db;
        try {
			System.out.println("results received");
			db = Database.getInstance();
			rs = db.DBquery(query);
			while(rs.next()) {
				this.customerName = rs.getString("customerName");
				this.phoneNumber = rs.getString("phoneNumber");
				this.address = rs.getString("address");
			}
		}
        catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
        }

	}
	
	public void updateCustomer(String name, int phone, String address) {
		String query = "UPDATE `customer` SET `customerName`='"+name+"', `phoneNumber`='+972"+phone+"', `address`='"+address+"' WHERE `userId`='"+this.userId+"';";
        try {
        	Database db = Database.getInstance();
			db.DBupdate(query);//insert
			System.out.println("data inserted");
			JOptionPane.showMessageDialog(null,"Information Updated!");
			this.customerName = name;
			this.phoneNumber = "+972"+phone;
			this.address = address;
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Failed to update account!");
			System.out.println("Exception : " +ex.getMessage());
        }
	}
	
	public void deleteCustomer() {
		String query1 = "DELETE FROM `login` WHERE `userId`='"+this.userId+"';";
		String query2 = "DELETE FROM `customer` WHERE `userId`='"+this.userId+"';";
        try {
        	Database db = Database.getInstance();
        	db.DBupdate(query1);
        	db.DBupdate(query2);
			System.out.println("data deleted");
			JOptionPane.showMessageDialog(null,"Account Deleted!");
			this.userId = "";
			this.customerName = "";
			this.phoneNumber = "";
			this.address = "";
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Failed to delete account!");
			System.out.println("Exception : " +ex.getMessage());
        }
	}
	
	public DefaultTableModel myProduct() {
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columnNames);
		String query = "SELECT purchaseInfo.purchaseId, purchaseInfo.productId, product.productName, purchaseInfo.cost, purchaseInfo.amount, purchaseInfo.date FROM purchaseInfo, product WHERE (`purchaseInfo`.`userId`='"+this.userId+"' AND `purchaseInfo`.`productId`=`product`.`productId`) ORDER BY `date` DESC;";     
		ResultSet rs = null;
        try {
        	Database db = Database.getInstance();
			rs = db.DBquery(query);//getting result
			System.out.println("results received");
			while(rs.next()) {
				String col1 = rs.getString("purchaseId");
				String col2 = rs.getString("productId");
				String col3 = rs.getString("productName");
				int col4 = rs.getInt("amount");
				double col5 = rs.getDouble("cost");
				String col6 = rs.getString("date");
				model.addRow(new Object[]{col1, col2, col3, col4, col5, col6});
			}
		}
        catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
        }
		return model;
	}
	
	public static DefaultTableModel searchCustomer(String keyword, String byWhat) {
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columnName);
		String query = "SELECT * FROM `customer` WHERE `userId`='"+keyword+"';";
		if (byWhat.equals("By Name"))
			query = "SELECT * FROM `customer` WHERE `customerName` LIKE '%"+keyword+"%';";
		ResultSet rs = null;
        try {
        	Database db = Database.getInstance();
			rs = db.DBquery(query);//getting result
			System.out.println("results received");
			
			while(rs.next()) {
				model.addRow(new Object[]{rs.getString("userId"), rs.getString("customerName"), rs.getString("phoneNumber"), rs.getString("address")});
			}
		}
        catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
        }
		return model;
	}
}