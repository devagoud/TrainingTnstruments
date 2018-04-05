package ro.titus.player;

public class SeekObject {

	long time = 0;
	int seek = 0;
	int breaking = 0;
	int finished = 0;

	public void setTime(long time) {
		this.time = time;
	}

	public void setBreaking(boolean b) {
		if (b) {
			breaking = 1;
		} else {
			breaking = 0;
		}
	}

	public void setSeek(boolean b) {
		if (b) {
			seek = 1;
		} else {
			seek = 0;
		}
	}

	public void setFinished(boolean b) {
		if (b) {
			finished = 1;
		} else {
			finished = 0;
		}
	}

	public boolean seek() {
		return seek == 1;
	}

	public long getTime() {
		return time;
	}

	public boolean finished() {
		return finished == 1;
	}

	public boolean breaking() {
		return breaking == 1;
	}

}
