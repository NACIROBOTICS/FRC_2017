package team2935.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MiddleGearAuto extends CommandGroup {

    public MiddleGearAuto() {
    	addParallel(new DriveToDistanceOnHeading(7.6,0.3,6));
    	addSequential(new AutoPositionGearUp(6));
    	addSequential(new AutoScoreGear(0.1));
    	addSequential(new DriveToDistanceOnHeading(1.0,-0.3,4));
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
