package team2935.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import team2935.robot.Robot;
import team2935.robot.subsystems.GearSubsystem.States;

/**
 *
 */
public class AutoCloseGear extends Command {
	double timeout;
    public AutoCloseGear(double timeout) {
        requires(Robot.gearSubsystem);
        this.timeout = timeout;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.gearSubsystem.closeClaw();
    	Robot.gearSubsystem.clawState = States.CLAW_CLOSED;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(timeSinceInitialized() >= timeout){return true;}
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
