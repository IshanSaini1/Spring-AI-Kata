package com.ai.robot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.robot.entity.HelpDeskTicket;

@Repository()
public interface HelpDeskTicketRepository extends JpaRepository<HelpDeskTicket, Integer> {

	Optional<HelpDeskTicket> findById(Integer id);
	List<HelpDeskTicket> findAll();
	List<HelpDeskTicket> findByUsername(String username);
}
