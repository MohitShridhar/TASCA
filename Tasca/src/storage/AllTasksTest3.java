package storage;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class AllTasksTest3 {
	private AllTasks test;

	@Test
	public void test() {
		test = new AllTasks();
		Calendar taskDate = setCalFromMilli(1397696400000L);
		Task task = new Task(0,0,1,taskDate, taskDate, false ,false, false,"Last day of School", "National University of Singapore");
		test.addTask(0, task);
		save();
		
		String result = getStringFromLnNum(1);
		assertEquals(result,"0 1 2014 3 17 9 0 2014 3 17 9 0 false false false Last day of School");
		
		String result2 = getStringFromLnNum(2);
		assertEquals(result2,"National University of Singapore");
	}

	@Test
	public void test2() {
		test = new AllTasks();
		Calendar startDate = setCalFromMilli(1396400400000L);
		Calendar endDate = setCalFromMilli(1397696400000L);
		Task task = new Task(0,0,1, startDate, endDate, false ,false, false,"Revise Schoolwork", "Home");
		test.addTask(0, task);
		save();
		
		String result = getStringFromLnNum(1);
		assertEquals(result,"0 1 2014 3 2 9 0 2014 3 17 9 0 false false false Revise Schoolwork");
		
		String result2 = getStringFromLnNum(2);
		assertEquals(result2,"Home");
	}
	
	// methods to help on testing

	public void save() {
		try {
			test.saveData();
		} catch (FileNotFoundException e) {
			fail("File not found");
		}
	}

	public Calendar setCalFromMilli(long millisec) {
		Calendar taskDate = Calendar.getInstance();
		Date taskTime = new Date(millisec);
		taskDate.setTime(taskTime);
		return taskDate;
	}
	
	public int getNumOfLinesFromFile() {

		int lines = 0;
		try {
			FileReader fileReader = new FileReader("system_saved_tasks.txt");
			BufferedReader bufferReader = new BufferedReader(fileReader);

			String lineText = bufferReader.readLine();

			while (lineText != null) {
				lines++;
			}

			bufferReader.close();

		} catch (IOException ex) {
			fail("File not found");
		}

		return lines;
	}

	@SuppressWarnings("resource")
	public String getStringFromLnNum(int lnNum) {
		
		String result = null;
		int lineIndex = 0;

		try {
			FileReader fileReader = new FileReader("system_saved_tasks.txt");
			BufferedReader bufferReader = new BufferedReader(fileReader);

			String lineText = bufferReader.readLine();
			
			while (lineText != null) {
				lineIndex++;
				if (lineIndex == lnNum) {
					return lineText;
				}
				lineText = bufferReader.readLine();
			}

			bufferReader.close();
		} catch (IOException ex) {
			fail("File not found");
		}

		return result;
	}
}
