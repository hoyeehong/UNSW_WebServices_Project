package poll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.LinkedList;

import poll.model.Vote;
import poll.model.Poll;
import poll.dao.PollDao;

/**
 * @author Yeehong Ho
 * @date 30/5/2017
 */

public class PollingServicesTest {
	
	public static void main(String[] args) throws Exception  
	{		
	    Class.forName("org.sqlite.JDBC");
	    Connection connection = null;
	    try
	    {
	      connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
	      Statement statement = connection.createStatement();	      
	    }
	    catch(SQLException e)
	    {
	      System.err.println(e.getMessage());
	    }
	    finally
	    {      
	    	try
	    	{	        
	    		if(connection != null)	          
	    			connection.close();      
	    	}
	    	catch(SQLException e)
	    	{	                
	    		System.err.println(e);      
	    	}    
	    }
	}

	public static boolean votesExistInPoll(String pId)
	{
		boolean exist = false;	
		Connection connection = null;
	    try
	    {	      
	    	connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");	      
	    	Statement statement = connection.createStatement();      
	    	statement.setQueryTimeout(30);      
	    	if(pId != null && !pId.isEmpty())
	    	{    	  
	    		ResultSet rs = statement.executeQuery("select count(*) from poll p left join vote v on p.id = v.pId where p.id='"+pId+"'");	      
	    		int count = rs.getInt(1);	      
	    		if(count != 0){
		    	  exist = true;		      
	    		}    	  	      
	    	}	    
	    }	    
	    catch(SQLException e){		      	    		
	    	System.err.println(e.getMessage());	    
	    }		
	    return exist;
	}
	
}
