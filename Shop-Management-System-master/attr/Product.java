package attr;

import java.util.*;
import java.sql.*;
import javax.swing.table.*;
import java.text.*;
import javax.swing.*;

public class Product {
	private String productId;
	private String productName;
	private double price;
	private int quantity;
	public static String[] columnNames = {"PID", "Name", "Price", "AvailableQuantity"};
	
	public Product() {}
	public Product(String productId) {
		if (!productId.isEmpty())
			this.productId = productId;
		else
			throw new IllegalArgumentException("Fill in the ID");
	}
	
	public void setProductName(String name) {
		if (!name.isEmpty())
			this.productName = name;
		else
			throw new IllegalArgumentException("Fill in the name");
	}
	public void setPrice(double p) {
		this.price = p;
	}
	public void setQuantity(int q) {
		this.quantity = q;
	}
	public String getProductId() {
		return productId;
	}
	public String getProductName() {
		return productName;
	}
	public double getPrice() {
		return price;
	}
	public int getQuantity() {
		return quantity;
	}
	
	public void fetch() {
		String query = "SELECT `productId`, `productName`, `price`, `quantity` FROM `product` WHERE productId='"+this.productId+"';";     
		ResultSet rs = null;
        try {
        	Database db = Database.getInstance();
			rs = db.DBquery(query);//getting result
			System.out.println("results received");
			
			while(rs.next()) {
				this.productName = rs.getString("productName");
				this.price = rs.getDouble("price");
				this.quantity = rs.getInt("quantity");
			}
		}
        catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
        }
	}
	
	public void sellProduct(String uid, int amount) {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		String query = "INSERT INTO `purchaseInfo` (`userId`, `productId`, `amount`, `date`, `cost`) VALUES ('"+uid+"','"+this.productId+"',"+amount+", '"+date+"', "+(amount*this.price)+");";
        try {
			if (this.quantity-amount >= 0) {
	        	Database db = Database.getInstance();
	        	db.DBupdate(query);//insert
				System.out.println("data inserted");
			updateProduct(this.productName, this.price, this.quantity-amount);
			}
			else
			JOptionPane.showMessageDialog(null,"You dont have enough of this product!"); 	
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Customer doesn't exist!"); 
			System.out.println("Exception : " +ex.getMessage());
        }
	}
	
	public void updateProduct(String name, double price, int quantity) {
		String query = "UPDATE `product` SET `productName`='"+name+"', `price`="+price+", `quantity`="+quantity+" WHERE `productId`='"+this.productId+"';";
        try {
        	Database db = Database.getInstance();
			db.DBupdate(query);//insert
			System.out.println("data inserted");
			JOptionPane.showMessageDialog(null,"Done!");
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Failed!");
			System.out.println("Exception : " +ex.getMessage());
        }
	}
	
	public void createProduct() {
		String query = "INSERT INTO `product` (`productName`, `price`, `quantity`) VALUES ('"+this.productName+"','"+this.price+"','"+this.quantity+"');";
        try {
        	Database db = Database.getInstance();
        	db.DBupdate(query);
			System.out.println("data inserted");
			JOptionPane.showMessageDialog(null,"Product Created!");
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Failed to add Product!");
			System.out.println("Exception : " +ex.getMessage());
        }
	}
	
	public static DefaultTableModel searchProduct(String keyword, String byWhat) {
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columnNames);
		String query = "SELECT `productId`, `productName`, `price`, `quantity` FROM `product` WHERE `productId`='"+keyword+"'  and `quantity` > 0;";
		if (byWhat.equals("By Name"))
			query = "SELECT `productId`, `productName`, `price`, `quantity` FROM `product` WHERE `productName` LIKE '%"+keyword+"%' and `quantity` > 0 ;";
		ResultSet rs = null;
        try {
        	Database db = Database.getInstance();
			rs = db.DBquery(query);//getting result
			System.out.println("results received");
			
			while(rs.next()) {
				model.addRow(new Object[]{rs.getString("productId"), rs.getString("productName"), rs.getDouble("price"), rs.getInt("quantity")});
			}
		}
        catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
        }
		return model;
	}
	
	public void deleteProduct() {
		String query1 = "DELETE FROM `product` WHERE `productId`='"+this.productId+"';";
        try {
        	Database db = Database.getInstance();
			db.DBupdate(query1);
			System.out.println("data deleted");
			JOptionPane.showMessageDialog(null,"Product Deleted!");
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Failed to delete product!");
			System.out.println("Exception : " +ex.getMessage());
        }

	}
}