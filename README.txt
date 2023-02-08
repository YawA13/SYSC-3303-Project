# SYSC-3303-Project

Team Members:
Asamoah, Yaw
Bashir, Faareh
Crowley, Triton
Sivathasan, Tharsan
Zhu, Ziheng

ITERATION 1

For this project we have the following java classes:

ElevatorSubsystem.java
This class represents the Elevator Subsystem which emulates an elevator car. It makes calls to the scheduler and receives works to be done from the scheduler.

FloorSubsystem.java
This class represents the Floor Subsystem which emulates a floor in a building. The floor Subsystem exchanges messages with the scheduler.

Scheduler.java
This class receives instructions form the floor and send it to elevator class. It is used to schedule the elevator cars. 

Instruction.java
This class represents the instructions to the elevator using the format shown as: time, floor, elevator number, and button status(up or down).

ElevatorSimulation.java
This class is the simulation of the process of running the elevator. This class contains the main method used to run the code.

ButtonStatus.java
This class is a Enumerator class to hold an elevator button's possible options (up or down)

IterationOneTest.java
This class is a Junit test class to make sure the project can can read the input file and pass the data back and forth between elevator and floor classes.

InputInstructions.txt
Instructions text file that is used by floor class to input instructions.

Set up instructions to run the project: 
Import the project from Git to eclipse
Run ElevatorSimulation.java
(If you would like to change the input text file it is in the project directory as 'InputInstructions.txt' which can be swapped for another file)
