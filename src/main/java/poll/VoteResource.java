package poll;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import poll.model.Vote;
import poll.dao.VoteDao;

@Path("/votes")
public class VoteResource {
	@Context
	UriInfo uriInfo;
	
	/**
	 * Creates a vote
	 * @param pid
	 * @param participantName
	 * @param chosenOption
	 * @return location of the vote
	 */
	@POST
	@Path("{pid}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response newVote(
			@PathParam("pid") String pid,
			@FormParam("participantname") String participantName,
			@FormParam("chosenoption") String chosenOption) throws IOException, URISyntaxException, ClassNotFoundException
	{
		Vote v = new Vote();
		
		v.setpId(pid);
		v.setParticipantName(participantName);
		v.setChosenOption(chosenOption);
		
		VoteDao vDao = new VoteDao();
		String vId = vDao.createVote(v);

		return Response.created(new URI(uriInfo.getBaseUri()+"votes/"+ vId)).entity(vId).build();
	}
	
	/**
	 * Get a vote
	 * @param id
	 * @return vote
	 */
	@GET
	@Path("{vid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getVote(@PathParam("vid") String id)
	{	
		Vote v = null;
		VoteDao vDao = new VoteDao();
		try {
			v = vDao.getVote(id);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(v == null)
		{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(v).build();
	}
	
	/**
	 * Get a poll's vote(s)
	 * @param id
	 * @return vote(s) that belong to the poll
	 */
	@GET
	@Path("/polls/{pid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getVotesByPid(@PathParam("pid") String id)
	{
		LinkedList<Vote> vs = new LinkedList<>();
		VoteDao vDao = new VoteDao();
		try {
			vs = vDao.getVotesByPid(id);
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if(vs == null || vs.isEmpty()){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(vs).build();
	}
	
	/**
	 * Update a vote
	 * @param vId
	 * @param pId
	 * @param participantName
	 * @param chosenOption
	 * @return updated vote
	 */
	@PUT
	@Path("{vid}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateVote(
			@PathParam("vid") String vId,
			@FormParam("pid") String pId,
			@FormParam("participantname") String participantName,
			@FormParam("chosenoption") String chosenOption) 
	{
		VoteDao votesdao = new VoteDao();
		Vote v = new Vote();
		
		v.setVoteId(vId);
		v.setpId(pId);
		v.setParticipantName(participantName);
		v.setChosenOption(chosenOption);
		
		String id;
		try {
			id = votesdao.updateVote(v);
			
			if(id.equals("Vote ID not exist")){
				return Response.status(Response.Status.NOT_FOUND).build();
			}else if(id.equals("Already has a final choice")){
				return Response.status(Response.Status.PRECONDITION_FAILED).build();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		return Response.ok(v).build();
	}
	
}
