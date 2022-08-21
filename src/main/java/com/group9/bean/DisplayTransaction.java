package com.group9.bean;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class DisplayTransaction {
	private int sNo;
	private String sourceStationName;
	private LocalDateTime dateAndTimeOfBoarding;
	private String destinationStationName;
	private LocalDateTime dateAndTimeOfExit;
    private double fare;

}
