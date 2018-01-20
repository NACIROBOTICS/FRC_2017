package team2935.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightGearShoot extends CommandGroup{
	
	public RightGearShoot(){
		addSequential(new DriveToDistanceOnHeading(5.7,0.5,3));
    	addSequential(new TurnToAngle(-43,2));
    	addParallel(new DriveToDistanceOnHeading(3.1,0.3,2.5));
    	addSequential(new AutoPositionGearUp(2.5));
    	addSequential(new AutoScoreGear(0.2));
    	addSequential(new DriveToDistanceOnHeading(5.0,-0.7,3));
    	addSequential(new TurnToAngle(180,2));
    	addParallel(new AutoShoot(3));
    	addSequential(new DriveToDistanceOnHeading(3.0,0.3,2));
	}
}
