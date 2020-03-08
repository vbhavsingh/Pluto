package com.log.server.model.graphics;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.log.server.LocalConstants;

/**
 * 
 * @author Vaibhav Pratap Singh
 *
 */
public class GraphicsData implements Serializable{
	
	private static final long serialVersionUID = 4803174096125773853L;

	List<GraphRowElement> dateFrequencyChart;
	
	List<GraphRowElement> nodeFrequencyChart;
	
	List<GraphRowElement> fileFrequencyChart;

	public String getDateFrequencyChart() {
		return convertToGoogleDateChartArray(dateFrequencyChart);
	}

	public void setDateFrequencyChart(List<GraphRowElement> dateFrequencyChart) {
		this.dateFrequencyChart = dateFrequencyChart;
	}

	public String getNodeFrequencyChart() {
		return convertToGoogleChartArray(nodeFrequencyChart);
	}

	public void setNodeFrequencyChart(List<GraphRowElement> nodeFrequencyChart) {
		this.nodeFrequencyChart = nodeFrequencyChart;
	}
	

	public String getFileFrequencyChart() {
		return convertToGoogleChartArray(fileFrequencyChart);
	}

	public void setFileFrequencyChart(List<GraphRowElement> fileFrequencyChart) {
		this.fileFrequencyChart = fileFrequencyChart;
	}

	private String convertToGoogleChartArray(List<GraphRowElement> rows) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i=1;
		for (GraphRowElement row : rows) {
			builder.append("[");
			builder.append("\"");
			builder.append(row.getLabel());
			builder.append("\",");
			builder.append(row.getCount());
			builder.append("]");
			if(i < rows.size()) {
				builder.append(",");
			}
			i++;
		}
		builder.append("]");
		return builder.toString();
	}
	
	
	private String convertToGoogleDateChartArray(List<GraphRowElement> rows) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i=1;
		for (GraphRowElement row : rows) {
			builder.append("[\"");
			builder.append("new Date(");
			builder.append(toDate(row.getLabel()));
			builder.append(")\",");
			builder.append(row.getCount());
			builder.append("]");
			if(i < rows.size()) {
				builder.append(",");
			}
			i++;
		}
		builder.append("]");
		return builder.toString();
	}
	
	private String toDate(String date) {
		try {
			Date d = LocalConstants.BARCHART_DATE_FORMATTER.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			return year+","+month+","+day;
			
		} catch (ParseException e) {
			
		}
		return date;
	}

}
