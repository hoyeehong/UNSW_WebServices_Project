package poll;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import poll.model.Vote;
import poll.model.Poll;

@Path("/polls")
public class PollResource {

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String newPoll(
			@FormParam("pollTitle") String pollTitle,
			@FormParam("description") String description,
			@FormParam("pollOptionType") String pollOptionType,
			@FormParam("options") String options,
			@FormParam("comments") String comments,
			@FormParam("finalChoice") String finalChoice
	) 
	throws Exception
	{
		File file = new File("polls.xml");
		if(!file.exists())
		{
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><polls></polls>");
			bufferedWriter.close();
		}
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
		Node polls = doc.getDocumentElement();
		
		//set poll ID node
		UUID uuid = UUID.randomUUID();
		String pId = uuid.toString();
		Element pIdNode = doc.createElement("pId");
		pIdNode.appendChild(doc.createTextNode(pId));
		
		Element pollTitleNode = doc.createElement("pollTitle");
		if(pollTitle == null)
		{
			pollTitleNode.appendChild(doc.createTextNode(""));
		}
		else
		{
			pollTitleNode.appendChild(doc.createTextNode(pollTitle));
		}
		
		Element descriptionNode = doc.createElement("description");
		if(description == null)
		{
			descriptionNode.appendChild(doc.createTextNode(""));
		}
		else
		{
			descriptionNode.appendChild(doc.createTextNode(description));
		}
		
		Element pollOptionTypeNode = doc.createElement("pollOptionType");
		if(pollOptionType == null)
		{
			pollOptionTypeNode.appendChild(doc.createTextNode(""));
		}
		else
		{
			pollOptionTypeNode.appendChild(doc.createTextNode(pollOptionType));
		}
		
		Element optionsNode = doc.createElement("options");
		if(options == null)
		{
			optionsNode.appendChild(doc.createTextNode(""));
		}
		else
		{
			optionsNode.appendChild(doc.createTextNode(options));
		}
		
		Element commentsNode = doc.createElement("comments");
		if(comments == null)
		{
			commentsNode.appendChild(doc.createTextNode(""));
		}
		else
		{
			commentsNode.appendChild(doc.createTextNode(comments));
		}
		
		Element finalChoiceNode = doc.createElement("finalChoice");
		if(finalChoice == null)
		{
			finalChoiceNode.appendChild(doc.createTextNode(""));
		}
		else
		{
			finalChoiceNode.appendChild(doc.createTextNode(finalChoice));
		}
		
		Element votesNode = doc.createElement("votes");
		
		//set poll node
		Element pollNode = doc.createElement("poll");
		polls.appendChild(pollNode);
		pollNode.appendChild(pIdNode);
		pollNode.appendChild(pollTitleNode);
		pollNode.appendChild(descriptionNode);
		pollNode.appendChild(pollOptionTypeNode);
		pollNode.appendChild(optionsNode);
		pollNode.appendChild(commentsNode);
		pollNode.appendChild(finalChoiceNode);
		pollNode.appendChild(votesNode);
		
		//write content to XML file
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(doc), new StreamResult(file));
		
		return "/polls/" + pId;
	}
	
}
