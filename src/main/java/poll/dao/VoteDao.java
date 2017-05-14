package poll.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.UUID;

import poll.model.Vote;

public class VoteDao {

	public String createVote(Vote v) throws ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		UUID uuid = UUID.randomUUID();
		String vId = uuid.toString();
		 
		Connection connection = null;		    
		try		    
		{		      
			connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");		      
			Statement statement = connection.createStatement();		      
			statement.setQueryTimeout(30);
		    
			statement.executeUpdate("insert into vote values('"+vId+"',"
														   +"'"+v.getpId()+"',"
														   +"'"+v.getParticipantName()+"',"
														   +"'"+v.getChosenOption()+"')");    
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
		return vId;
	}
	
	public Vote getVote(String id)
	{	
		Vote v = null;
		Connection connection = null;
	    try
	    {
	      connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
	      Statement statement = connection.createStatement();
	      
	      ResultSet rs = statement.executeQuery("select * from vote where id='"+id+"'");
	      while(rs.next())
	      {
	    	  v = new Vote();
	    	  v.setVoteId(rs.getString(1));
	    	  v.setpId(rs.getString(2));
	    	  v.setParticipantName(rs.getString(3));
	    	  v.setChosenOption(rs.getString(4));	  
	      }   
	    }catch(SQLException e)	    		    
	    {      		    	
	    	System.err.println(e.getMessage());		    
	    }   
	    return v;
	}
	
	public LinkedList<Vote> getVotesByPid(String id)
	{		
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
	
	public String updateVote(Vote v)
	{
		Connection connection = null;
		try
	    {
			connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
		    Statement statement = connection.createStatement();
			if(!finalChoiceInPoll(v.getpId()))
			{
				ResultSet rs = statement.executeQuery("select id from vote where id = '"+v.getVoteId()+"'");		      
				if(!rs.next()){	    	 
					return "Vote ID not exist";		      
				}
				if(v.getParticipantName() != null){
					statement.executeUpdate("update vote set participantName ='"+v.getParticipantName()+"' where id ='"+v.getVoteId()+"'");
				}
				if(v.getChosenOption() != null){
					statement.executeUpdate("update poll set chosenOption ='"+v.getChosenOption()+"' where id ='"+v.getVoteId()+"'");
				}
			}
			else
			{
				return "Already has a final choice";
			}
	    }
		catch(SQLException e)
	    {		      
	    	System.err.println(e.getMessage());		    
	    }
		
		return "";
	}
	
	public boolean finalChoiceInPoll(String pid)
	{	
		boolean finalChoiceStatus = false;
		Connection connection = null;
	    try
	    {
	      connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
	      Statement statement = connection.createStatement();
	      
	      ResultSet rs = statement.executeQuery("select finalChoice from poll where id='"+pid+"'");
	      while(rs.next())	      
	      {	
	    	  if(rs.getString(1)!=null)
	    	  {
	    		  finalChoiceStatus = true;
	    	  }
	      }
	    }catch(SQLException e){      
	    	System.err.println(e.getMessage());
	    }
		return finalChoiceStatus;
		
	}
}
