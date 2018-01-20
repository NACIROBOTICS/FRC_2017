package team2935.robot.commands.gear;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import team2935.robot.Robot;

public class GearIntakeCommand extends Command {

	enum ButtonState { PRESSED, RELEASED };
	
	public ButtonState gearGrabbedState = ButtonState.RELEASED;
	public ButtonState moveClawState = ButtonState.RELEASED;
	
    public GearIntakeCommand() {
       requires(Robot.gearSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
  		switch(gearGrabbedState){
			case RELEASED:
				if(Robot.oi.grabGear()){
					Robot.gearSubsystem.actuateClaw();
					gearGrabbedState = ButtonState.PRESSED;
		    		return;
				}	
			case PRESSED:
				if (!Robot.oi.grabGear()) {
	    			gearGrabbedState = ButtonState.RELEASED;
	    		}
	    		break;
  		}
		switch(moveClawState){
			case RELEASED:
				if(Robot.oi.moveClaw()){
					Robot.gearSubsystem.positionClaw();
					moveClawState = ButtonState.PRESSED;
		    		return;
				}	
			case PRESSED:
				if (!Robot.oi.moveClaw()) {
					moveClawState = ButtonState.RELEASED;
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
