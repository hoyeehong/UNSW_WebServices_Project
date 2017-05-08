package poll.model;

public class Poll {
	private String pId;
	private String pollTitle;
	private String description;
	private String pollOptionType;
	private String options;
	private String comments;
	private String finalChoice;
	
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
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
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
	
	//Constructor
	public Poll() {
	}
}
