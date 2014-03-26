package storage;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class AllTasksTest2 {
	private AllTasks test;

	@Test
	public void test() {
		test = new AllTasks();
		Calendar taskDate = setCalFromMilli(1397696400000L);
		Task task = new Task(0,1,taskDate, taskDate, false ,false, false,"Last day of School", "National University of Singapore");
		test.addTask(0, task);

		assertTrue(test.getTask(0) == task);
	}

	@Test
	public void test2() {
		test = new AllTasks();
		Calendar taskDate = setCalFromMilli(1397696400000L);
		Task task1 = new Task(0,1,taskDate, taskDate, false ,false, false,"Last day of School", "National University of Singapore");
		Task task2 = new Task(0,1,taskDate, taskDate, false ,false, false,"Holiday to Australia", "Changi Airport");
		test.addTask(0, task1);
		test.addTask(0, task2);

		assertTrue(test.getTask(0) == task2);
		assertTrue(test.getTask(1) == task1);
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
