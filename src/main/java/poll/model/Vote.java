package poll.model;

/**
 * @author Yeehong Ho
 * @date 30/5/2017
 */

public class Vote {
	private String voteId;
	private String pId;
	private String participantName;
	private String chosenOption;

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
	
	//Constructor
	public Vote() {
	}
	
	public String toString(){
		return voteId+" | "+
			   pId+" | "+
			   participantName+" | "+
			   chosenOption;
	}
}
