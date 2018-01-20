package team2935.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import team2935.robot.Robot;
import team2935.robot.RobotConst;
import team2935.robot.subsystems.ChassisSubsystem.States;


public class GameControllerDriveCommand extends Command {
	
	enum ButtonState { PRESSED, RELEASED };
	
	public ButtonState shiftState = ButtonState.RELEASED;
	public ButtonState toggleDirectionButton = ButtonState.RELEASED;
	public States toggleDirectionState = States.OFF;

	public GameControllerDriveCommand() {
		requires(Robot.chassisSubsystem);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		double speed,turn;
		double joystickDriveSpeedInput = Robot.oi.getDriveSpeed(); 
		double joystickTurnSpeedInput = Robot.oi.getTurnSpeed(); 
		
		if(Robot.oi.shifterEngaged()){
			Robot.chassisSubsystem.shiftHigh();
		}else{
			Robot.chassisSubsystem.shiftLow();
		}	
		if(Math.abs(joystickTurnSpeedInput) > RobotConst.CONTROLLER_TURN_THRESHOLD){
			Robot.chassisSubsystem.setDifferentMotorSpeeds(-joystickTurnSpeedInput, joystickTurnSpeedInput);
		}else if(Math.abs(joystickDriveSpeedInput)>RobotConst.CONTROLLER_DRIVE_THRESHOLD){
			Robot.chassisSubsystem.setAllMotorSpeeds(joystickDriveSpeedInput);
		}else{
			Robot.chassisSubsystem.setAllMotorSpeeds(0);
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		Robot.chassisSubsystem.prev_distance = (int)Robot.chassisSubsystem.getEncoderDistance();
		return false;
	}
	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
