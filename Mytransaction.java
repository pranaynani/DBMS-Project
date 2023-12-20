package acidproperties;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Mytransaction {

	public static void main(String args[]) throws SQLException, IOException, 
	ClassNotFoundException {

		// Load the MYSQL driver
		// Connect to the default database with credentials
		// You will have to change your credentials
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/acidproperties", "root", "1234");

		// For atomicity
		conn.setAutoCommit(false);	

		// For isolation	
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

		Statement stmt1 = null;
		try {
			// Create statement object
			stmt1 = conn.createStatement();
			
			//Creating the tables product, stock, and depot
			stmt1.executeUpdate ("create table product(prod_id char(10), pname varchar(20), price integer)");
			stmt1.executeUpdate ("create table stock(prod_id char(10), dep_id char(20), quantity integer)");
			stmt1.executeUpdate ("create table depot(dep_id char(10), addr char(20), volume integer)");

			//Creating Primary key for table product 
			stmt1.executeUpdate("ALTER TABLE Product ADD CONSTRAINT pk_product_id PRIMARY KEY (prod_id)");
			
			//Creating Primary key for table depot 
			stmt1.executeUpdate("ALTER TABLE Depot ADD CONSTRAINT pk_depot PRIMARY KEY (dep_id)");
			
			//Creating Primary key for table stock as well as foreign key constraints
			stmt1.executeUpdate("ALTER TABLE Stock ADD CONSTRAINT pk_stock PRIMARY KEY (prod_id, dep_id)");
			stmt1.executeUpdate("ALTER TABLE Stock ADD CONSTRAINT fk_stock_depot FOREIGN KEY (dep_id) REFERENCES"
					+ " depot(dep_id) ON DELETE CASCADE");
			stmt1.executeUpdate("ALTER TABLE Stock ADD CONSTRAINT fk_stock_product FOREIGN KEY (prod_id) REFERENCES"
					+ " Product (prod_id) ON DELETE CASCADE");
			
			// Inserting data into Table Product
			stmt1.executeUpdate("insert into product values ('p1', 'tape', 2.5)");
			stmt1.executeUpdate("insert into product values ('p2', 'tv', 250)");
			stmt1.executeUpdate("insert into product values ('p3', 'vcr', 80)");

			
			//Inserting data into Table depot
			stmt1.executeUpdate("insert into depot values ('d1', 'New York', 9000)");
			stmt1.executeUpdate("insert into depot values ('d2', 'Syracuse', 6000)");
			stmt1.executeUpdate("insert into depot values ('d4', 'New York', 2000)");

			
			//Inserting data into Table stock
			stmt1.executeUpdate("insert into stock values ('p1', 'd1', 1000)");
			stmt1.executeUpdate("insert into stock values ('p1', 'd2',-100)");
			stmt1.executeUpdate("insert into stock values ('p1', 'd4', 1200)");
			stmt1.executeUpdate("insert into stock values ('p3', 'd1', 3000)");
			stmt1.executeUpdate("insert into stock values ('p3', 'd4', 2000)");
			stmt1.executeUpdate("insert into stock values ('p2', 'd4', 1500)");
			stmt1.executeUpdate("insert into stock values ('p2', 'd1', -400)");
			stmt1.executeUpdate("insert into stock values ('p2', 'd2', 2000)");

			
			System.out.println("Initial table creation \n");
			System.out.println("Product table \n");
			System.out.println("................\n");
			//Lisiting all data from table product
			ResultSet rs = stmt1.executeQuery ("select * from product");
			while (rs.next() ){
				System.out.println(rs.getString("prod_id") + ", " + rs.getString("pname") + ", " + rs.getInt("price"));	
			}
			System.out.println("................\n");
			
			System.out.println("Stock table \n");
			System.out.println("................\n");
			//Lisiting all data from table stock
			ResultSet rs1 = stmt1.executeQuery ("select * from stock");
			while (rs1.next() ){
				System.out.println(rs1.getString("prod_id") + ", " + rs1.getString("dep_id") + ", " + rs1.getInt("quantity"));		
			}
			
			System.out.println("................\n");

			//Lisiting all data from table depot
			System.out.println("Depot table \n");
			System.out.println("................\n");
			ResultSet rs2 = stmt1.executeQuery ("select * from depot");
			while (rs2.next() ){
				System.out.println(rs2.getString("dep_id") + ", " + rs2.getString("addr") + ", " + rs2.getInt("volume"));		
			}
			System.out.println("................\n");
			
			//Query 1
			//This is a prepared statement that is deleting p1 from product table
			String deleteProductString = "delete from product where prod_id = ?";
			PreparedStatement deleteProductstmt = conn.prepareStatement(deleteProductString);
			deleteProductstmt.setString(1,"p1");
			deleteProductstmt.executeUpdate();
			
			//Query 2
			//This is a prepared statement that is deleting d1 from depot table
			String deleteDepotString = "delete from depot where dep_id = ?";
			PreparedStatement deleteDepotstmt = conn.prepareStatement(deleteDepotString);
			deleteDepotstmt.setString(1,"d1");
			deleteDepotstmt.executeUpdate();
				
			System.out.println("Data after execution of query 1 and qeury 2 \n");
			System.out.println("................\n");
			
			//Lisiting all data from table product
			System.out.println("Product table \n");
			System.out.println("................\n");
			ResultSet rsa = stmt1.executeQuery ("select * from product");
			while (rsa.next() ){
				System.out.println(rsa.getString("prod_id") + ", " + rsa.getString("pname") + ", " + rsa.getInt("price"));	
			}
			System.out.println("................\n");

			//Lisiting all data from table stock
			System.out.println("Stock table \n");
			System.out.println("................\n");
			ResultSet rsa1 = stmt1.executeQuery ("select * from stock");
			while (rsa1.next() ){
				System.out.println(rsa1.getString("prod_id") + ", " + rsa1.getString("dep_id") + ", " + rsa1.getInt("quantity"));		
			}
			
			System.out.println("................\n");

			//Lisiting all data from table depot
			System.out.println("Depot table \n");
			System.out.println("................\n");
			ResultSet rsa2 = stmt1.executeQuery ("select * from depot");
			while (rsa2.next() ){
				System.out.println(rsa2.getString("dep_id") + ", " + rsa2.getString("addr") + ", " + rsa2.getInt("volume"));		
			}
			System.out.println("................\n");
			
			//Query 5
			//This is a prepared statement that is adding new data into the product table
			String addProductString = "insert into product values (?, ?, ?)";
					PreparedStatement addProductStmt = conn.prepareStatement(addProductString);
					addProductStmt.setString(1,"p100");
					addProductStmt.setString(2, "cd");
					addProductStmt.setInt(3, 5);
					addProductStmt.executeUpdate();

			//This is a prepared statement that is adding new data into the stock table
			String addStockString = "insert into stock values (?, ?, ?)";
					PreparedStatement addStockStmt = conn.prepareStatement(addStockString);
					addStockStmt.setString(1,"p100");
					addStockStmt.setString(2, "d2");
					addStockStmt.setInt(3, 50);
					addStockStmt.executeUpdate();
					
			//Query 6		
					//This is a prepared statement that is adding new data into the depot table
			String addDepotString = "insert into depot values (?, ?, ?)";
					PreparedStatement addDepotStmt = conn.prepareStatement(addDepotString);
					addDepotStmt.setString(1,"d100");
					addDepotStmt.setString(2, "Chicago");						
					addDepotStmt.setInt(3, 100);
				addDepotStmt.executeUpdate();
				
				stmt1.executeUpdate("insert into product values ('p1', 'tape', 2.5)");
				//This is a prepared statement that is adding new data into the stock table
				String addStockdString = "insert into stock values (?, ?, ?)";
						PreparedStatement addStockdStmt = conn.prepareStatement(addStockdString);
						addStockdStmt.setString(1,"p1");
						addStockdStmt.setString(2, "d100");
						addStockdStmt.setInt(3, 100);
						addStockdStmt.executeUpdate();
			

			System.out.println("Data after execution of query 5 and qeury 6 \n");
			System.out.println("................\n");
			//Lisiting all data from table product
			System.out.println("Product table \n");
			System.out.println("................\n");
			ResultSet rsb = stmt1.executeQuery ("select * from product");
			while (rsb.next() ){
				System.out.println(rsb.getString("prod_id") + ", " + rsb.getString("pname") + ", " + rsb.getInt("price"));	
			}
			System.out.println("................\n");

			//Lisiting all data from table stock
			System.out.println("Stock table \n");
			System.out.println("................\n");
			ResultSet rsb1 = stmt1.executeQuery ("select * from stock");
			while (rsb1.next() ){
				System.out.println(rsb1.getString("prod_id") + ", " + rsb1.getString("dep_id") + ", " + rsb1.getInt("quantity"));		
			}
			
			System.out.println("................\n");

			//Lisiting all data from table depot
			System.out.println("Depot table \n");
			System.out.println("................\n");
			ResultSet rsb2 = stmt1.executeQuery ("select * from depot");
			while (rsb2.next() ){
				System.out.println(rsb2.getString("dep_id") + ", " + rsb2.getString("addr") + ", " + rsb2.getInt("volume"));		
			}
				
	
		} catch (SQLException e) {
			System.out.println("An exception was thrown");
			e.printStackTrace();
			// For atomicity
			conn.rollback();
			stmt1.close();
			conn.close();
			return;
		} 
		conn.commit();
		stmt1.close();
		conn.close();
	}
}
