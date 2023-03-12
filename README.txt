# SYSC-3303-Project

Team Members:
Asamoah, Yaw
Bashir, Faareh
Crowley, Triton
Sivathasan, Tharsan
Zhu, Ziheng

ITERATION 3

For this project we have the following java classes:


ButtonStatus.java
This class is a Enumerator class to hold an elevator button's possible options (up or down)

Elevator.java
Holds all elevator related parameters eg elevator car number etc

ElevatorSimulation.java
This class is the simulation of the process of running the elevator. This class contains the main method used to run the code.

ElevatorStates.java
Enum class for all elevator possible states

ElevatorSubsystem.java
This class represents the Elevator Subsystem which emulates an elevator car. It makes calls to the scheduler and receives works to be done from the scheduler.

Floor.java
Holds all elevator related parameters eg floor lamps, elevator direction etc

FloorRequest.java
This class reads instruction input time to simulate button being pressed and sends to floor subsystem then sleeps until next instruction

FloorSubsystem.java
This class represents the Floor Subsystem which emulates a floor in a building. The floor Subsystem exchanges messages with the scheduler.

FloorSubsystemHost.java
This class represents the Floor Subsystem which emulates a floor in a building. The floor Subsystem exchanges messages with the scheduler.

Instruction.java
This class represents the instructions to the elevator using the format shown as: time, floor, elevator number, and button status(up or down).

IterationOneTest.java
This class is a Junit test class to make sure the project can can read the input file and pass the data back and forth between elevator and floor classes.

InputInstructions.txt
Instructions text file that is used by floor class to input instructions.

Scheduler.java
This class receives instructions form the floor and send it to elevator class. It is used to schedule the elevator cars. 

SchedulerElevatorHost.java
UDP class for scheduler to communicate with elevator class

SchedulerFloorHost.java
UDP class for scheduler to communicate with floor class

SchedulerStates.java
Enum class for all scheduler possible states



Set up instructions to run the project: 
Import the project from Git to eclipse
(If you would like to change the input text file it is in the project directory as 'InputInstructions.txt' which can be swapped for another file)

Set scheduler IP in FloorSubsystem main method
Set floorIp and elevator IP in scheduler main method
Set schedulerIp in elevator subsystem main method

Run Scheduler.java class first
Run FloorSubsystem.java class next
Run ElevatorSubsystem.java class last

