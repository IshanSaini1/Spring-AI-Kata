package com.ai.robot.tools;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class TimeTools {

	private static final Logger log = LoggerFactory.getLogger(TimeTools.class);
	
	private static final String zoneIdList = "EST - -05:00, HST - -10:00, MST - -07:00, ACT - Australia/Darwin, AET - Australia/Sydney, AGT - America/Argentina/Buenos_Aires, ART - Africa/Cairo, AST - America/Anchorage, BET - America/Sao_Paulo, BST - Asia/Dhaka, CAT - Africa/Harare, CNT - America/St_Johns, CST - America/Chicago, CTT - Asia/Shanghai, EAT - Africa/Addis_Ababa, ECT - Europe/Paris, IET - America/Indiana/Indianapolis, IST - Asia/Kolkata, JST - Asia/Tokyo, MIT - Pacific/Apia, NET - Asia/Yerevan, NST - Pacific/Auckland, PLT - Asia/Karachi, PNT - America/Phoenix, PRT - America/Puerto_Rico, PST - America/Los_Angeles, SST - Pacific/Guadalcanal, VST - Asia/Ho_Chi_Minh";
	private static final String zoneListDescription = "Get the current time in the specified timezone from the list " + zoneIdList;
	
	@Tool(name = "getCurrentLocalTime", description = "Get the current time in the user's timezone.") 
	public String getCurrentLocalTime() {
		LocalDateTime dateTime = LocalDateTime.now();
		log.info("{}",dateTime.toString()); 
		return dateTime.toString();
	}
	
	@Tool(name = "getCurrentTimeInTimezone", description = zoneListDescription) 
	public String getCurrentTimeInTimezone(@ToolParam(description = "Value representing the current time zone") String timezone) {
		LocalDateTime dateTime = LocalDateTime.now(ZoneId.of(timezone));
		log.info("{} in timezone {}",dateTime.toString(),timezone);
		return dateTime.toString();
	}
}
