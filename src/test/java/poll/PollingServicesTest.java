package poll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.LinkedList;
import java.util.UUID;

import poll.model.Vote;
import poll.model.Poll;
import poll.dao.PollDao;

public class PollingServicesTest {
	
	public static void main(String[] args) throws Exception  
	{		
	    Class.forName("org.sqlite.JDBC");
	    
	    String pId = "2cba1d48-bc70-4b89-acc9-43f5535b7fb9";
		String title = "tNEW";
		String description = "dNEW";
		String pollOptionType = "date and time";
		
		LinkedList<String> options = new LinkedList<>();
		options.add("7pm");
		options.add("8pm");
		options.add("9pm");
		
		String comments = "cNEW";
		String finalChoice = "N/A";
		
	    Connection connection = null;
	    try
	    {
	      connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");
	      Statement statement = connection.createStatement();	      

	      /*statement.executeUpdate("drop table if exists vote");
	      statement.executeUpdate("create table vote (id string,"
													+"pId string,"
													+"participantName string,"
													+"chosenOption string)");

	      statement.executeUpdate("insert into vote values('"+vId+"',"
														+ "'"+pId+"',"
														+ "'"+participantName+"',"
														+ "'"+chosenOption+"')");*/
	           
	      /*statement.executeUpdate("create table poll_options (id string,options string)");
	      for(String opts : options){
	    	  statement.executeUpdate("insert into poll_options values ('"+pId+"','"+opts+"')");
	      }*/
	      
	      ResultSet rs = statement.executeQuery("select finalChoice from poll where id='"+pId+"'");
	      while(rs.next())	      
	      {	
	    	  if(rs.getString(1)==null){
	    		  
	    	  }
	      }
	      
	    	  
	      
	    	  /*statement.executeUpdate("update poll set title ='"+title+"',"
	    	  						 +"description ='"+description+"',"
	    	  						 +"pollOptionType ='"+pollOptionType+"',"
	    	  						 +"comments ='"+comments+"',"
	    	  						 +"finalChoice ='"+finalChoice+"' where id ='"+pId+"'");
	    	  
	    	  statement.executeUpdate("delete from poll_options where id ='"+pId+"'");
	    	  for(String opts : options){
		    	  statement.executeUpdate("insert into poll_options values('"+pId+"','"+opts+"')");      
	    	  }*/
	     
	      
	    }
	    catch(SQLException e){
	      System.err.println(e.getMessage());
	    }
	    finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e){	        
	        System.err.println(e);
	      }
	    }
	}

	public static boolean votesExistInPoll(String pId)
	{
		boolean exist = false;	
		Connection connection = null;
	    try{	      
	    	connection = DriverManager.getConnection("jdbc:sqlite:pollingservices.db");	      
	    	Statement statement = connection.createStatement();      
	    	statement.setQueryTimeout(30);      
	    	if(pId != null && !pId.isEmpty()){
	    	  
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
