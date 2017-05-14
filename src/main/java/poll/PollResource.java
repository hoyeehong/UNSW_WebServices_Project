package poll;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import poll.model.Vote;
import poll.model.Poll;
import poll.dao.PollDao;
import poll.dao.VoteDao;

@Path("/polls")
public class PollResource {
	@Context
	UriInfo uriInfo;
	
	/**
	 * Creating a poll
	 * 
	 * @param title
	 * @param description
	 * @param pollOptionType
	 * @param options
	 * @param comments
	 * 
	 * @return a link to the resource's location 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response newPoll(
			@FormParam("title") String title,
			@FormParam("description") String description,
			@FormParam("pollOptionType") String pollOptionType,
			@FormParam("options") List<String> options,
			@FormParam("comments") String comments) throws Exception
	{
		Poll p = new Poll(title);
		if(description == null){
			p.setDescription("");
		}
		else{
			p.setDescription(description);
		}
		if(pollOptionType == null){
			p.setPollOptionType("");
		}
		else{
			p.setPollOptionType(pollOptionType);
		}
		p.setOptions(options);
		if(comments == null){
			p.setComments("");
		}
		else{
			p.setComments(comments);
		}
		//p.setVotesInPoll(null);
		p.setFinalChoice("");
		PollDao pDao = new PollDao();
		String pId = pDao.createPoll(p);
		
		return Response.created(new URI(uriInfo.getBaseUri()+"polls/"+pId)).entity(pId).build();
	}
	
	/**
	 * Getting polls(collection)
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllPolls()
	{
		LinkedList<Poll> listOfPolls = new LinkedList<>();
		PollDao pDao = new PollDao();
		try 
		{
			listOfPolls = pDao.getPollCollection();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(listOfPolls != null){
			return Response.ok(listOfPolls).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	/**
	 * Getting a specific poll
	 * @param poll id
	 * @return the specified poll
	 */
	@GET
	@Path("{pid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPoll(@PathParam("pid") String id) throws ClassNotFoundException
	{
		Poll p = null;
		LinkedList<Poll> listOfPolls = new LinkedList<>();
		PollDao pDao = new PollDao();
		listOfPolls = pDao.getPollCollection();
		for(Poll eachPoll : listOfPolls)
		{
			if(eachPoll.getpId().equals(id))
			{
				p = eachPoll;
				break;
			}
		}
		if(p == null){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(p).build(); 
	}
	
	/**
	 * Getting the vote(s) of a specific poll
	 * @param poll id
	 * @return list of votes belonging the poll
	 */
	@GET
	@Path("{pid}/votes")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getVotesByPid(@PathParam("pid") String id) throws ClassNotFoundException
	{
		LinkedList<Vote> listOfVotes = new LinkedList<>();	
		VoteDao votesdao = new VoteDao();	
		listOfVotes = votesdao.getVotesByPid(id);
				
		if(listOfVotes == null || listOfVotes.isEmpty()){
			return Response.status(Response.Status.NOT_FOUND).build();
		}		
		return Response.ok(listOfVotes).build(); 
	}
	
	/**
	 * Getting a particular vote
	 * @param pid
	 * @param vid
	 * @return a vote
	 */
	@GET
	@Path("{pid}/votes/{vid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getVoteByPid(@PathParam("pid") String pid, @PathParam("vid") String vid) throws ClassNotFoundException 
	{	
		VoteDao votesdao = new VoteDao();	
		LinkedList<Vote> vs = new LinkedList<>();
		vs = votesdao.getVotesByPid(pid);

		Vote v = null;	
		for(Vote eachVote : vs){
			if(eachVote.getVoteId().equals(vid)){
				v = eachVote;
				break;
			}
		}
		
		if(v == null){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(v).build(); 
	}
	
	@GET
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response searchPoll(@QueryParam("pid") String pid) 
	{
		Poll p = null;
		try
		{		
			LinkedList<Poll> ps = new LinkedList<>();
			PollDao pDao = new PollDao();
			ps = pDao.getPollCollection();	
			for(Poll eachPoll : ps)
			{
				if(eachPoll.getpId().equals(pid))
				{
					p = eachPoll;
					break;
				}
			}	
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		return Response.ok(p).build();
	}
	
	@PUT
	@Path("{pid}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updatePoll(			
			@PathParam("pid") String pid,
			@FormParam("title") String title,
			@FormParam("description") String description,
			@FormParam("optiontype") String optionType,
			@FormParam("option") LinkedList<String> options,
			@FormParam("comments") String comments,
			@FormParam("finalchoice") String finalChoice) 
	{		
		PollDao pollsdao = new PollDao();	
		Poll p = new Poll();
		
		p.setpId(pid);
		p.setPollTitle(title);
		p.setDescription(description);
		p.setPollOptionType(optionType);
		p.setOptions(options);
		p.setComments(comments);
		p.setFinalChoice(finalChoice);
		
		String idToUpdate;
		try 
		{
			idToUpdate = pollsdao.updatePoll(p);
			if(idToUpdate.equals("Poll ID not exist")){
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			else if(idToUpdate.equals("Votes exist")){
				return Response.status(Response.Status.PRECONDITION_FAILED).build();
			}
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return Response.ok(p).build();
	}
	
	@DELETE
	@Path("{pid}")
	public Response deletePoll(@PathParam("pid") String id) 
	{
		PollDao pollsdao = new PollDao();	
		String idToDelete = null;
		try 
		{
			idToDelete = pollsdao.deletePoll(id);
			if(idToDelete.equals("Poll ID not exist")){
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			else if(idToDelete.equals("Votes exist")){
				return Response.status(Response.Status.PRECONDITION_FAILED).build();
			}
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).build();	
	}
	
}