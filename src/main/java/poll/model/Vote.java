package poll.model;

import java.util.LinkedList;

public class Vote {
	private String voteId;
	private String pId;
	private String participantName;
	private String chosenOption;
	
	private LinkedList<Poll> votes;

	//Getters & Setters
	public String getVoteId() {
		return voteId;
	}
	public void setVoteId(String voteId) {
		this.voteId = voteId;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getChosenOption() {
		return chosenOption;
	}
	public void setChosenOption(String chosenOption) {
		this.chosenOption = chosenOption;
	}
	public LinkedList<Poll> getVotes() {
		return votes;
	}
	public void setVotes(LinkedList<Poll> votes) {
		this.votes = votes;
	}
	
	//Constructor
	public Vote() {
		votes = new LinkedList<>();
	}
}
