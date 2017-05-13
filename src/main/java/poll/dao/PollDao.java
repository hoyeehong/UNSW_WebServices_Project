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
	      statement.setQueryTimeout(30); //set timeout to 30 sec.

	      statement.executeUpdate("drop table if exists poll");
	      statement.executeUpdate("create table poll (id string,"
	    		  									+"title string,"
	      											+"description string,"
	      											+"pollOptionType string,"
	      											+"comments string,"
	      											+"finalChoice string)");
	      
	      statement.executeUpdate("insert into poll values('"+pId+"',"
	      												+ "'"+p.getPollTitle()+"',"
	      												+ "'"+p.getDescription()+"',"
	      												+ "'"+p.getPollOptionType()+"',"
	      												+ "'"+p.getComments()+"',"
	      												+ "'"+p.getFinalChoice()+"')");
	      
	      statement.executeUpdate("create table poll_options (id string,options string)");
	      for(String opts : p.getOptions()){
	    	  statement.executeUpdate("insert into poll_options values ('"+pId+"','"+opts+"')");
	      }
	    }
	    catch(SQLException e)
	    {
	      //if the error message is "out of memory", 
	      //it probably means no database file is found
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
	        //connection close failed.
	        System.err.println(e);
	      }
	    }
		return pId;
	}
	
	public LinkedList<Poll> getPollCollection()
	{
		LinkedList<Poll> listOfPolls = new LinkedList<>();	
		Connection connection = null;
	    try
	    {
	      connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
	      Statement statement = connection.createStatement();
		
	      ResultSet rs = statement.executeQuery("select * from poll");    	      
	      while(rs.next())	      
	      {	    	    	  
	    	  Poll p = new Poll();
		    	
		      p.setpId(rs.getString(1));
		      p.setPollTitle(rs.getString(2));
		      p.setDescription(rs.getString(3));
		      p.setPollOptionType(rs.getString(4));
		        
		      p.setOptions(findPollOptions(rs.getString(1))); //id
		        
		      p.setComments(rs.getString(5));
		      p.setFinalChoice(rs.getString(6));
		        
		      VoteDao votesdao = new VoteDao(); 	      
			  p.setVotesInPoll(votesdao.getVotesByPid(rs.getString(1))); //id
		      
			  listOfPolls.add(p);	      
	      }    
	    }	    
	    catch(SQLException | ClassNotFoundException e)	    
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
		return listOfPolls;
	}
	
	public LinkedList<String> findPollOptions(String pId)
	{	
		LinkedList<String> listOfPollsOpts = new LinkedList<>();
		Connection connection = null;
	    try
	    {
	      connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
	      Statement statement = connection.createStatement();
	      
	      ResultSet rs = statement.executeQuery("select * from poll_options where id='"+pId+"'");
	      while(rs.next()){
	    	  listOfPollsOpts.add(rs.getString(2));
	      }
	    }catch(SQLException e)
	    {		      
	    	System.err.println(e.getMessage());		    
	    }	
		return listOfPollsOpts;
	}
	
	public String updatePoll(Poll p)
	{
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
		return "";	
	}
	public boolean votesExistInPoll(String pId)
	{	
		boolean exist = false;	
		Connection connection = null;
	    try   
	    {
	      connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
	      Statement statement = connection.createStatement();
	      if(pId != null && !pId.isEmpty())
	      {
	    	  ResultSet rs = statement.executeQuery("select count(*) from poll p left join vote v on p.id = v.pId where p.id='"+pId+"'");
		      int count = rs.getInt(1);
		      if(count != 0){
		    	  exist = true;
		      }    	  
	      } 
	    }	
	    catch(SQLException e)    	
	    {		      	    		
	    	System.err.println(e.getMessage());	    
	    }		
	    return exist;
	}
	
	public String deletePoll(String pId)
	{
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
				statement.executeUpdate("delete from poll_options where id ='"+pId+"'");	
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
		return "";
	}
	
	
}
