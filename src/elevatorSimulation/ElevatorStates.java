package elevatorSimulation;


/**
 * Enumerator class to hold an elevator possible States
 * 
 * @author Group 3, SYSC 3303
 * @version Milestone 2
 * @since 2023-02-16
 *
 */
public enum ElevatorStates {
	DoorsOpening, 
	DoorsOpen,
	DoorsClosing,
	MovingDown,
	MovingUp,
	Arriving
}