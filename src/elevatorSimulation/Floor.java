package elevatorSimulation;

import java.util.ArrayList;

public class Floor 
{
	boolean upFloorLamp;
	boolean downFloorLamp;
	
	boolean upDirectionLamp;
	boolean downDirectionLamp;
	
	boolean upButton;
	boolean downButton;
	
	boolean arrivalLamp;

	public Floor() 
	{
		this.upFloorLamp = false;
		this.downFloorLamp = false;
		this.upDirectionLamp = false;
		this.downDirectionLamp = false;
		this.upButton = false;
		this.downButton = false;
		this.arrivalLamp = false;
	}

	public boolean getUpFloorLamp() {
		return upFloorLamp;
	}

	public void setUpFloorLamp(boolean upFloorLamp) {
		this.upFloorLamp = upFloorLamp;
	}

	public boolean getDownFloorLamp() {
		return downFloorLamp;
	}

	public void setDownFloorLamp(boolean downFloorLamp) {
		this.downFloorLamp = downFloorLamp;
	}

	public boolean getUpDirectionLamp() {
		return upDirectionLamp;
	}

	public void setUpDirectionLamp(boolean upDirectionLamp) {
		this.upDirectionLamp = upDirectionLamp;
	}

	public boolean getDownDirectionLamp() {
		return downDirectionLamp;
	}

	public void setDownDirectionLamp(boolean downDirectionLamp) {
		this.downDirectionLamp = downDirectionLamp;
	}

	public boolean getUpButton() {
		return upButton;
	}

	public void setUpButton(boolean upButton) {
		this.upButton = upButton;
	}

	public boolean getDownButton() {
		return downButton;
	}

	public void setDownButton(boolean downButton) {
		this.downButton = downButton;
	}

	public boolean getArrivalLamp() {
		return arrivalLamp;
	}

	public void setArrivalLamp(boolean arrivalLamp) {
		this.arrivalLamp = arrivalLamp;
	}
	

	

}
