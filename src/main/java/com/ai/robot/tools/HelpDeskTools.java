package com.ai.robot.tools;

import java.util.List;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.ai.robot.entity.HelpDeskTicket;
import com.ai.robot.model.TicketRequest;
import com.ai.robot.service.HelpdeskTicketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HelpDeskTools {

	private final HelpdeskTicketService service;

	@Tool(name = "createTicket", description = "Create the support ticket.")
	String createTicket(
			@ToolParam(description = "Details of the issue, to create a support ticket.") TicketRequest ticketRequest,
			ToolContext toolContext) {
		try {
			String username = (String) toolContext.getContext().get("username");
			HelpDeskTicket ticket = service.createTicket(ticketRequest, username);
			return "Ticket Number #" + ticket.getId() + " has been created successfully for user "
					+ ticket.getUsername() + ".";
		} catch (Exception e) {
			return "Some Error has occurred in saving the ticket with message " + e.getMessage();
		}
	}
	
	@Tool(name = "getAllTicketsAsPerUsername", description = "fetch the status of open tickets for a user against their username.")
	List<HelpDeskTicket> getAllTicketsAsPerUsername(ToolContext toolContext){
		try {
		String username = (String) toolContext.getContext().get("username");
		List<HelpDeskTicket> tickets = service.getTicketsByUsername(username);
		return tickets;
		} catch(Exception e) {
			log.error("Some Error has occurred while fetching tickets for username");
			e.printStackTrace();
			throw e;
		}
	}

}
