package com.ai.robot.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "helpdesk_tickets")
public class HelpDeskTicket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "username")
	private String username;

	@Column(name = "issue")
	private String issue;

	@Column(name = "status")
	private String status;

	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "eta")
	private LocalDateTime eta;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
