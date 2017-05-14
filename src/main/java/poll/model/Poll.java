package poll.model;

import java.util.LinkedList;
import java.util.List;

public class Poll {
	private String pId;
	private String pollTitle;
	private String description;
	private String pollOptionType;
	private List<String> options;
	private String comments;
	private String finalChoice;
	
	private List<Vote> votesInPoll;
	
	//Getters & Setters
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getPollTitle() {
		return pollTitle;
	}
	public void setPollTitle(String pollTitle) {
		this.pollTitle = pollTitle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPollOptionType() {
		return pollOptionType;
	}
	public void setPollOptionType(String pollOptionType) {
		this.pollOptionType = pollOptionType;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getFinalChoice() {
		return finalChoice;
	}
	public void setFinalChoice(String finalChoice) {
		this.finalChoice = finalChoice;
	}
	
	public List<Vote> getVotesInPoll() {
		return votesInPoll;
	}
	public void setVotesInPoll(List<Vote> votesInPoll) {
		this.votesInPoll = votesInPoll;
	}
	
	//Constructor
	public Poll() {	
	}
	public Poll(String pollTitle) {	
		this.pollTitle = pollTitle;
	}
	
	public String toString(){
		return pId+" | "+
			   pollTitle+" | "+
			   description+" | "+
			   pollOptionType+" | "+
			   options+" | "+
			   comments+" | "+
			   finalChoice;
	}
}
