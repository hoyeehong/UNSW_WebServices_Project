package poll.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import poll.model.Vote;

public class VoteDao {

	public LinkedList<Vote> getVotesByPid(String id) throws ClassNotFoundException{
			
		Class.forName("org.sqlite.JDBC");
		
		LinkedList<Vote> vs = new LinkedList<>();
		Connection connection = null;
	    try
	    {
	      connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
	      Statement statement = connection.createStatement();
	      
	      ResultSet rs = statement.executeQuery("select * from vote where id='"+id+"'");
	      while(rs.next())	      
	      {	
	    	  Vote v = new Vote();
	    	  v.setpId(rs.getString(1));
	    	  v.setVoteId(rs.getString(2));
	    	  v.setParticipantName(rs.getString(3));
	    	  v.setChosenOption(rs.getString(4));
	    	  
	    	  vs.add(v);
	      }      
	      if(id == null) return null;		
		}
		catch(SQLException e){      
	    	System.err.println(e.getMessage());
	    }	
	    return vs;
	}
	
	
}
