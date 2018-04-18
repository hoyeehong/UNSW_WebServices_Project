package poll.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.UUID;

import poll.model.Poll;
import poll.dao.VoteDao;

/**
 * @author Yeehong Ho
 * @date 30/5/2017
 */

public class PollDao {

	public String createPoll(Poll p) throws ClassNotFoundException
	{	
		Class.forName("org.sqlite.JDBC");
		//generate a random poll id
		UUID uuid = UUID.randomUUID();
		String pId = uuid.toString();
	    Connection connection = null;
	    try
	    {
	      //create a database connection
	      connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
	      Statement statement = connection.createStatement();
	      statement.setQueryTimeout(30);
	      statement.executeUpdate("insert into poll values('"+pId+"',"
	      												 +"'"+p.getPollTitle()+"',"
	      												 +"'"+p.getDescription()+"',"
	      												 +"'"+p.getPollOptionType()+"',"
	      												 +"'"+p.getComments()+"',"
	      												 +"'"+p.getFinalChoice()+"')");
	      for(String opts : p.getOptions()){
	      		statement.executeUpdate("insert into poll_options values('"+pId+"','"+opts+"')");
	      }
	    }
	    catch(SQLException e)
	    {
	      System.err.println(e.getMessage());
	    }
		return pId;
	}
	
	public LinkedList<Poll> getPollCollection() throws ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		LinkedList<Poll> listOfPolls = new LinkedList<>();	
		Connection connection = null;
	    try
	    {
	      connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
	      Statement statement = connection.createStatement();
		
	      String sql = "select * from poll";
	      ResultSet rs = statement.executeQuery(sql);    	      
	      while(rs.next())	      
	      {	    	    	  
	    	  Poll p = new Poll();
		    	
		      p.setpId(rs.getString(1));
		      p.setPollTitle(rs.getString(2));
		      p.setDescription(rs.getString(3));
		      p.setPollOptionType(rs.getString(4));
		      p.setOptions(findPollOptions(rs.getString(1)));
		      p.setComments(rs.getString(5));
		      p.setFinalChoice(rs.getString(6));
		        
		      VoteDao votesdao = new VoteDao();
			  p.setVotesInPoll(votesdao.getVotesByPid(rs.getString(1)));
			  listOfPolls.add(p);	      
	      }
	    }	    
	    catch(SQLException e)	    
	    {      
	    	System.err.println(e.getMessage());
	    }
		return listOfPolls;
	}
	
	public LinkedList<String> findPollOptions(String pId) throws ClassNotFoundException
	{	
		Class.forName("org.sqlite.JDBC");
		LinkedList<String> listOfPollsOpts = new LinkedList<>();
		Connection connection = null;
	    try
	    {
	      connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
	      Statement statement = connection.createStatement();
	      
	      String sql = "select * from poll_options where id='"+pId+"'";
	      ResultSet rs = statement.executeQuery(sql);
	      while(rs.next()){
	    	  listOfPollsOpts.add(rs.getString(2));
	      }
	    }catch(SQLException e)
	    {		      
	    	System.err.println(e.getMessage());		    
	    }	
		return listOfPollsOpts;
	}
	
	public String updatePoll(Poll p) throws ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		try
	    {
			connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
		    Statement statement = connection.createStatement();	    
			if(!votesExistInPoll(p.getpId()))
			{
				ResultSet rs = statement.executeQuery("select id from poll where id = '"+p.getpId()+"'");		      
				if(!rs.next()){	    	 
					return "Poll ID not exist";		      
				}
				
				if(p.getPollTitle() != null){
					statement.executeUpdate("update poll set title ='"+p.getPollTitle()+"' where id ='"+p.getpId()+"'");
				}
				if(p.getDescription() != null){
					statement.executeUpdate("update poll set description ='"+p.getDescription()+"' where id ='"+p.getpId()+"'");
				}
				if(p.getPollOptionType() != null){
					statement.executeUpdate("update poll set pollOptionType ='"+p.getPollOptionType()+"' where id ='"+p.getpId()+"'");
				}
				if(p.getOptions() != null){
					statement.executeUpdate("delete from poll_options where id ='"+p.getpId()+"'");
					for(String opts : p.getOptions()){
						statement.executeUpdate("insert into poll_options values('"+p.getpId()+"','"+opts+"')");
					}
				}
				if(p.getComments() != null){
					statement.executeUpdate("update poll set comments ='"+p.getComments()+"' where id ='"+p.getpId()+"'");
				}
				if(p.getFinalChoice() != null){
					statement.executeUpdate("update poll set finalChoice ='"+p.getFinalChoice()+"' where id ='"+p.getpId()+"'");
				}	
			}
			else
			{
				return "Votes exist";
			}
	    }
		catch(SQLException e)
	    {		      
	    	System.err.println(e.getMessage());		    
	    }
		finally 
		{			  
			if (connection != null){		    
				try{		      
					connection.close();
			    } 
				catch (SQLException e){
					System.err.println(e.getMessage());
			    }		  
			}		
		}
		return "";	
	}
	public boolean votesExistInPoll(String pId) 
	{
		boolean notExist = false;	
		Connection connection = null;
	    try   
	    {
	      connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
	      Statement statement = connection.createStatement();
	      if(pId != null && !pId.isEmpty())
	      {
	    	  ResultSet rs = statement.executeQuery("select count(*) from poll p left join vote v on p.id = v.pId where p.id='"+pId+"'");
	    	  int count = rs.getInt(1);
		      if(count == 0)
		      {
		    	  notExist = true;
		      }
	      }
	    }	
	    catch(SQLException e)    	
	    {		      	    		
	    	System.err.println(e.getMessage());	    
	    }
	    finally 
		{			  
			if(connection != null){		    
				try{		      
					connection.close();
			    } 
				catch(SQLException e){
					System.err.println(e.getMessage());
			    }		  
			}		
		}
	    return notExist;
	}
	
	public String deletePoll(String pId) throws ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		try
	    {
			connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
		    Statement statement = connection.createStatement();    
		    if(!votesExistInPoll(pId))
			{
		    	ResultSet rs = statement.executeQuery("select id from poll where id = '"+pId+"'");		      
				if(!rs.next()){	    	 
					return "Poll ID not exist";		      
				}	
				statement.executeUpdate("delete from poll where id ='"+pId+"'");	
			}
		    else
			{				
		    	return "Votes exist";			
			}      
	    }
		catch(SQLException e)
	    {		      
	    	System.err.println(e.getMessage());		    
	    }
		finally 
		{			  
			if(connection != null){		    
				try{		      
					connection.close();
			    } 
				catch(SQLException e){
					System.err.println(e.getMessage());
			    }		  
			}		
		}
		return "";
	}
	
	
}
