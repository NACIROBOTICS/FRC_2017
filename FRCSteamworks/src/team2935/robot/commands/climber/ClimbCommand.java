package team2935.robot.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import team2935.robot.Robot;

public class ClimbCommand extends Command {
	
	enum ButtonState { PRESSED, RELEASED };
	
	private ButtonState climbButton = ButtonState.RELEASED;
	
    public ClimbCommand() {
       requires(Robot.climberSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	switch(climbButton){
		case RELEASED:
			if(Robot.oi.startClimb()){
				Robot.climberSubsystem.actuateClimb(-1.0);
				climbButton = ButtonState.PRESSED;
	    		break;
			}	
		case PRESSED:
			if (!Robot.oi.startClimb()) {
    			climbButton = ButtonState.RELEASED;
    		}
    		break;
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
