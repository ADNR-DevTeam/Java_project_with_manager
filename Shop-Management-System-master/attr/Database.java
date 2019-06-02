package attr;

import java.sql.*;
 

public class Database  {
	private static Database instance = null;
	protected static Connection c = null;
	private Statement stmt = StatementDB();
	
	
	private Database() throws SQLException {
		
		openDB();

		try{
			stmt.executeQuery("select * from login");
		}
		catch(Exception e){
			
		String sql = "CREATE TABLE IF NOT EXISTS 'login' "
				+"  (userId varchar(12) NOT NULL,"
				+"  password varchar(12) NOT NULL,"
				+"  status int(1) NOT NULL)" ;
		stmt.executeUpdate(sql);
		String ins = "INSERT INTO login (	userId, password, status) VALUES" + 
				"('e001', 'e001', 0)," + 
				"('e002', 'e002', 0)," + 
				"('c001', 'c001', 1)," +
				"('Baba', 'Baba', 2)," + 
				"('deba', 'aaaa', 1);";
		stmt.executeUpdate(ins);
		}

		try{
			stmt.executeQuery("select * from customer");
		}
		catch(Exception e) {
		String sql = "CREATE TABLE IF NOT EXISTS `customer`" 
				  +"(`userId` varchar(12) NOT NULL,"
				  +"`customerName` varchar(30) NOT NULL,"
				  +"`phoneNumber` varchar(14) NOT NULL,"
				  +"`address` varchar(50) NOT NULL)";
		
		stmt.executeUpdate(sql);
		
		String ins = "INSERT INTO `customer` (`userId` , `customerName`, `phoneNumber`, `address`) VALUES" + 
				"('c001', 'Customer', '+9721234567890', 'banani')," + 
				"('deba', 'Debashish', '+9721763923789', 'Kuril');";
		stmt.executeUpdate(ins);
		}
		
		try {
			stmt.executeQuery("select * from employee");
		}
		catch(Exception e){	
		String sql = "CREATE TABLE IF NOT EXISTS `employee`"
				 + "(`userId` varchar(12) NOT NULL,"
				 +"`employeeName` varchar(50) NOT NULL,"
				  +"`phoneNumber` varchar(14) NOT NULL,"
				  +"`role` varchar(8) NOT NULL,"
				 + "`salary` double(8,2) NOT NULL)";
		stmt.executeUpdate(sql);
		
		String ins = "INSERT INTO `employee` (`userId`, `employeeName`, `phoneNumber`, `role`, `salary`) VALUES" + 
				"('e001', 'Employee1', '+9721234567890', 'General', 50000.00)," + 
				"('Baba', 'Baba', '+9721234567890', 'Manager', 70000.00),"+
				"('e002', 'Employee2', '+9721234567891', 'General', 30000.00);";
		stmt.executeUpdate(ins);
		}
		
		try {
			stmt.executeQuery("select * from product");
		}
		catch(Exception e) {
			
		String sql = "CREATE TABLE IF NOT EXISTS `product` ("
				  +"`productId` INTEGER PRIMARY KEY AUTOINCREMENT ,"
				  +"`productName` varchar(50) NOT NULL,"
				  +"`price` double(8,2) NOT NULL,"
				  +"`quantity` int(8) NOT NULL)";
		stmt.executeUpdate(sql);
		
		String ins = "INSERT INTO `product` ( `productName`, `price`, `quantity`) VALUES" + 
				"('Jack Daniels', 66.00, 7)," + 
				"('GlennLivet', 2000.00, 5)," + 
				"('Black Label', 55.00, 21)," + 
				"('BushMills', 89999.00, 5)," + 
				"('Vodka Imperial', 2500.00, 6);";
		stmt.executeUpdate(ins);
		}
		
		try {
			stmt.executeQuery("select * from purchaseinfo");
		}
		catch(Exception e) {
			
		String sql = "CREATE TABLE IF NOT EXISTS `purchaseinfo` ("
				  +"`purchaseId` INTEGER PRIMARY KEY AUTOINCREMENT,"
				  +"`userId` varchar(12) NOT NULL,"
				  +"`productId` varchar(12) NOT NULL,"
				  +"`cost` double(12,2)  NOT NULL,"
				  +"`amount` int(8)  NOT NULL,"
				  +"`date` varchar(18) NOT NULL)";
		stmt.executeUpdate(sql);
		System.out.println("Database created!");
		
		String ins = "INSERT INTO `purchaseinfo` (`purchaseId`, `userId`, `productId`, `cost`, `amount`, `date`) VALUES" + 
				"(00001, 'deba', '00003', 55.00, 1, '2018-09-26')," + 
				"(00002, 'c001', '00005', 2500.00, 1, '2018-09-28')," + 
				"(00006, 'c001', '00003', 110.00, 2, '2018-09-28');";
		stmt.executeUpdate(ins);
		}
		

	}

	
	public static Database getInstance() throws SQLException

	{
		if (instance == null)
		{
			instance = new Database();
		}
			return instance;
	}
	
	private static Connection openDB()
	{
		if (c != null)
			return c;
		try {
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:Shop.db");
		System.out.println("Connected to DB");
		return c;
		}
		catch (Exception e) {
			c = null; 
			System.out.println("Error! Could not connect to DB");
			return null;
		}
	}
	
	public static void closeDB()

	{
		try {
			
			if (c != null)
			{
				c.close();
				c = null;
				System.out.println("Connection to DB closed");
			}
		}
		catch(Exception e) {
			System.out.println("There is no open connection");
		}
	}
	
	private Statement StatementDB()
	{
		openDB();
		try {
			Statement stmt = c.createStatement();
			System.out.println("Statement created!");
			return stmt;
		}
		catch(Exception e)
		{
			System.out.println("Error! Could not create statement!");
			return null;
		}
	}
	
	
	public ResultSet DBquery(String query)  //Receive an SQL query and returns the result 
	{
	
		ResultSet res = null;
		
		System.out.println("SQL: "+query);

		try {
		
		res = stmt.executeQuery(query);
		}
        catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
			ex.printStackTrace();
	}
		return res;
}
	
	public boolean DBupdate(String update)  //Receive an SQL command, update the DB and returns if the update successes
	{
		
		int res = 0;

		try {
		
		res = stmt.executeUpdate(update);
		System.out.println("SQL: "+update);
		
		}
        catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
			ex.printStackTrace();
	}
		if (res > 0)
			return true;
		return false;
		
	}
	
	/*public ResultSet DBexecute(String exec) //Receive an SQL command, execute it and returns the result
	{
		try {
		System.out.println("SQL: "+exec);
		return stmt.executeQuery(exec);
		
		}
        catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
			ex.printStackTrace();
			return null;
	}
	}*/
	
	@Override
	protected void finalize() throws Throwable //In case the connection to the DB is not closed yet.
	{
		if (stmt != null)
		{
			stmt.close();
			
		}
		if (c != null)
		{
			c.close();
			System.out.println("Connection to DB closed");
		}
	}
}

