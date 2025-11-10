package com.ai.robot.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ai.robot.entity.HelpDeskTicket;
import com.ai.robot.model.TicketRequest;
import com.ai.robot.repository.HelpDeskTicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelpdeskTicketService {

	private final HelpDeskTicketRepository helpDeskTicketRepository;
	
	public HelpDeskTicket createTicket(TicketRequest ticketInput, String username) {
		HelpDeskTicket ticket = HelpDeskTicket.builder()
				.issue(ticketInput.issue())
				.username(username)
				.status("OPEN")
				.eta(LocalDateTime.now().plusDays(7))
				.build();
		return helpDeskTicketRepository.save(ticket);
	}
	
	public List<HelpDeskTicket> getTicketsByUsername(String username){
		return helpDeskTicketRepository.findByUsername(username);
	}
	
}
