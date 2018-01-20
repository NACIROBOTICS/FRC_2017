package team2935.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team2935.robot.RobotMap;
import team2935.robot.commands.gear.GearIntakeCommand;

public class GearSubsystem extends Subsystem {

	//Initialization of all solenoid objects used for the gear mechanism 
	private Solenoid openClaw = new Solenoid(RobotMap.SOLENOID_OPEN_CLAW);
	private Solenoid closeClaw = new Solenoid(RobotMap.SOLENOID_CLOSE_CLAW);
	private Solenoid pushClaw = new Solenoid(RobotMap.SOLENOID_CLAW_UP);
	private Solenoid pullClaw = new Solenoid(RobotMap.SOLENOID_CLAW_DOWN);
	
	//State variables that monitor the positions that the arm is in
	public States clawState = null;
	public States clawPosition = null;
	
	public enum States{ SCORING_POSITION,GROUND_POSITION,CLAW_CLOSED,CLAW_OPEN };
	
    public void initDefaultCommand() {
        setDefaultCommand(new GearIntakeCommand());
    }
    public void robotInit(){
    	if(clawState == null){
    		closeClaw();
    		clawState = States.CLAW_CLOSED;
    	}
    	if(clawPosition == null){
    		extendClaw();
    		clawPosition = States.SCORING_POSITION;
    	}
    }
    public void closeClaw(){
    	closeClaw.set(true);
    	openClaw.set(false);
    }
    public void openClaw(){
    	openClaw.set(true);
    	closeClaw.set(false);
    }
    public void extendClaw(){
    	pushClaw.set(true);
    	pullClaw.set(false);
    }
    public void retractClaw(){
    	pullClaw.set(true);
    	pushClaw.set(false);
    }
    public void actuateClaw(){
    	if(clawState.compareTo(States.CLAW_OPEN)==0){
    		closeClaw();
    		clawState = States.CLAW_CLOSED;
    	}else{
    		openClaw();
    		clawState = States.CLAW_OPEN;
    	}
    }
    public void positionClaw(){
    	if(clawPosition.compareTo(States.GROUND_POSITION)==0){
    		if(clawState.compareTo(States.CLAW_OPEN)==0){
    			closeClaw();
    			clawState = States.CLAW_CLOSED;
    			positionClaw();
    		}
    		extendClaw();
			clawPosition = States.SCORING_POSITION;
    	}else{
    		if(clawState.compareTo(States.CLAW_OPEN)==0){
    			closeClaw();
    			clawState = States.CLAW_CLOSED;
    		}
    		retractClaw();
			clawPosition = States.GROUND_POSITION;
    	}
    }
    public void updatePeriodic(){
    	SmartDashboard.putString("Claw State", clawState.toString());
    	SmartDashboard.putString("Claw Position", clawPosition.toString());
    }
}

