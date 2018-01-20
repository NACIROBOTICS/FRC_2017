package team2935.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import team2935.robot.Robot;

public class GearRollerCommand extends Command {

    public GearRollerCommand() {
        requires(Robot.intakeSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	boolean intakeGear = Robot.oi.intakeGear();
    	boolean outtakeGear = Robot.oi.outtakeGear();
    	if(intakeGear){
    		Robot.intakeSubsystem.runIntake(0.5);
    	}else if(outtakeGear){
    		Robot.intakeSubsystem.runIntake(-0.5);
    	}else{
    		Robot.intakeSubsystem.runIntake(0);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
