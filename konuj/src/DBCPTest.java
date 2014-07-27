import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.konuj.servlet.DBConnGateway.CtlJDBCClient;
import com.konuj.servlet.DBConnGateway.DbConnGatewayConfig;
import com.mysql.jdbc.Statement;


public class DBCPTest {

	public static CtlJDBCClient	m_jdbcOltpClient = null;
	
	public static void main(String args[])
	{
		try
		{
			System.out.println("Start instantiating DbConnGateway");
			
			 DbConnGatewayConfig oDbConnGwCfg = new DbConnGatewayConfig();
			 oDbConnGwCfg.m_iDBCP_ThreadPoolSize  = 16;
			 oDbConnGwCfg.m_iDBCP_InitialSize     = 4;
			 oDbConnGwCfg.m_iDBCP_MaxConnections  = 16;
			 oDbConnGwCfg.m_iDBCP_MaxIdle         = -1;
			 oDbConnGwCfg.m_strDBCP_ConnectURI    = "jdbc:mysql://localhost:3306/konuj";
		     oDbConnGwCfg.m_strDBCP_DriverClassName = "com.mysql.jdbc.Driver";
		     oDbConnGwCfg.m_strDBCP_UserId 		  = "root";
		     oDbConnGwCfg.m_strDBCP_Password 	  = "";
			 
			 System.out.println("Instantiating DbConnGateway object with ConnectUri [" + oDbConnGwCfg.m_strDBCP_ConnectURI + "]");
			
			if (oDbConnGwCfg != null)
			{
				m_jdbcOltpClient = new CtlJDBCClient();
				m_jdbcOltpClient.init(oDbConnGwCfg);
				
			
				System.out.println("DbConnGateway Instantiated.");
			}
			else
			{
				System.out.println("Failed to instantiate DbConnGateway. Reason, configuration not found");
			}
			
			Connection oConnection = null;
			oConnection = m_jdbcOltpClient.borrowObject();
			
			
			String strQuery = "insert into chat_transcripts(user1_cookie_id, user1_ip, user1_join_time," +
			" user2_cookie_id, user2_ip, user2_join_time, transcript) values(?,?,?,?,?,?,?)";
			
			
			
			PreparedStatement stmtQueryInsert = oConnection.prepareStatement(strQuery, java.sql.Statement.RETURN_GENERATED_KEYS);
			
			//stmtQueryInsert.RETURN_GENERATED_KEYS = 1;
			
			stmtQueryInsert.setString(1, "cookie id");
			stmtQueryInsert.setString(2, "127.0.0.1");
			stmtQueryInsert.setDate(3, new Date(System.currentTimeMillis()));
			stmtQueryInsert.setString(4, "cookie id");
			stmtQueryInsert.setString(5, "127.0.0.1");
			stmtQueryInsert.setDate(6, new Date(System.currentTimeMillis()));			
			stmtQueryInsert.setString(7, "hello");
			stmtQueryInsert.executeUpdate();
			System.out.println("inserted");
			
			ResultSet rs = stmtQueryInsert.getGeneratedKeys();
			rs.next();
			System.out.println(rs.getInt(1));
			
			commit(oConnection);
			
			
/*			
			String strQuery = "SELECT nextval('sq_my_sequence') as next_sequence";
			java.sql.Statement s= oConnection.createStatement();
			ResultSet rs = s.executeQuery(strQuery);
			rs.next();
			System.out.println(rs.getInt(1));
			
*/			

		}
		catch (Exception e)
		{
			System.out.println("Failed to start DbConnGateway");
			e.printStackTrace();
		}

	}
	public static void commit(Connection conn) throws Exception
	{
		CtlJDBCClient.commit(conn);
	}	

}
