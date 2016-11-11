package classscheduler;

import java.util.List;

public class CourseBlock extends Block{

	Course[][] blocks;

	private final int BLOCKSIZE = 2;
	private final int WEEKDAYS = 5;
	private int maxEnrollment;
	public CourseBlock() {
		blocks = new Course[BLOCKSIZE][WEEKDAYS];
		maxEnrollment = 0;
	}

	public void addCourse(Course c, ClassFormat f) {
		for (int start = f.start(); start < WEEKDAYS; start = start + f.skip()) {
			double eachDay = c.getCreditHour() * 1.0 / f.numOfDays();
			int i = 0;

			while (eachDay > 0.0) {
				blocks[i][start] = c;
				eachDay -= 1.0;
				i++;
			}
		}
		maxEnrollment = Math.max(maxEnrollment, c.getMaxAllowedEnrollment());
	}

	public boolean isMergeCompatible(Course course, ClassFormat format) {
		if (course.getCreditHour() > format.getMaxSpace(BLOCKSIZE))
			return false;
		for (int start = format.start(); start < WEEKDAYS; start = start + format.skip()) {
			for (int j = 0; j < BLOCKSIZE; j++) {
				// if the format in the block is empty
				if (blocks[j][start] != null)
					return false;
			}
		}
		return true;

	}

	public int getMaxEnrollment(){
		return maxEnrollment;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < blocks.length; j++) {
			for (int i = 0; i < blocks[0].length; i++) {
				sb.append(String.format("|    %s   |", blocks[j][i]));
			}
			sb.append("\n");
		}
		return sb.toString();

	}

	public static void main(String[] args) {
		
	}

}
