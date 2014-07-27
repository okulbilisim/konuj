
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class MySqlTest {

	public static void main(String args[]) throws Exception
	{
		Connection conn = null;

        try
        {
            String userName = "root";
            String password = "";
            String url = "jdbc:mysql://localhost/comet";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, userName, password);
            System.out.println ("Database connection established");
           
            /*
            
            String todo = "INSERT INTO comet.chat_transcripts (transcript) VALUES('sfsfdf')" ;

            try {
                    java.sql.Statement s = conn.createStatement();
                    //int r = s.executeUpdate (todo);
                    s.execute(todo);
            }
            catch (Exception e) {
                    e.printStackTrace();
                    }
            */
            
            PreparedStatement s;
            s = conn.prepareStatement (
                        "INSERT INTO chat_transcripts (transcripts) VALUES(?)");
            s.setString (1, "sdfsff");
            //s.setClob(1, "sfsfdf");
            //s.set
            //s.setString (2, catVal);
            int count = s.executeUpdate ();
            s.close ();
            System.out.println (count + " rows were inserted");
            
            
            
        }
        catch (Exception e)
        {
            System.err.println ("Cannot connect to database server");
            e.printStackTrace();
        }
        finally
        {
        	
            if (conn != null)
            {
                try
                {
                    conn.close ();
                    System.out.println ("Database connection terminated");
                }
                catch (Exception e) { /* ignore close errors */ }
            }
			
	
        }
	}
	
}
