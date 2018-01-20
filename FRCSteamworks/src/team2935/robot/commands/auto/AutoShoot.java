package team2935.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import team2935.robot.Robot;

/**
 *
 */
public class AutoShoot extends Command {
	
	private boolean speedUp;
	double timeout;

    public AutoShoot(double timeout) {
        requires(Robot.shooterSubsystem);
        this.timeout = timeout;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(!speedUp){
    		Robot.shooterSubsystem.actuateShooter(1.0);
    		speedUp = true;
    		return;
    	}else if(speedUp){
    		Robot.shooterSubsystem.actuateRegulator(-1.0);
    	}else{
    		speedUp = false;
    		Robot.shooterSubsystem.actuateRegulator(0);
    		Robot.shooterSubsystem.actuateShooter(0);
    	}
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
