package classscheduler;

public class Block {
	
	
	Course[][] blocks; 
	
	private final int BLOCKSIZE = 2;
	private final int WEEKDAYS = 5;
	private int capLeft; 
	
	
	public Block(){
		blocks = new Course[BLOCKSIZE][WEEKDAYS];
		capLeft = BLOCKSIZE * WEEKDAYS;
	}
	
	public void addCourse(Course course, int x, int y){
		blocks[x][y] = course;
	}
	
	public void addCourse(Course c, ClassFormat f){
		int start, skip, numOfDays;
		if(f == ClassFormat.MWF){
			start = 0;
			skip = 2;
			numOfDays = 3;
		}else if(f == ClassFormat.TTH){
			start = 1;
			skip = 2;
			numOfDays = 3;
		}else{
			start = 0;
			skip = 1;
			numOfDays = 7;
		}
		
		for(start = 0; start < WEEKDAYS; start = start + skip){
			double eachDay = c.getCreditHour() * 1.0/ numOfDays;
			int i = 0;
			
			while(eachDay > 0.0){
				blocks[i][start] = c;
				eachDay -= 1.0;
				capLeft -= 1.0;
				i++;
			}
		}
		System.out.println(capLeft);
		
	}
	public boolean isMergeCompatible(Block block){
		return capLeft >= block.capLeft;
		
	}
	public void merge(Block block){
		
		
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int j = 0; j < blocks.length; j++){
			for(int i = 0; i < blocks[0].length; i++){
				sb.append(String.format("|    %s   |", blocks[j][i]));
			}
			sb.append("\n");
		}
		return sb.toString();
		
	}
	public static void main(String[] args){
		Block blk = new Block();
		blk.addCourse(new Course("Chem", "101", 4), ClassFormat.MWF);
		System.out.println(blk);
	}
	
}
