package team2935.robot.subsystems;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team2935.robot.RobotMap;
import team2935.robot.commands.climber.ClimbCommand;

public class ClimberSubsystem extends Subsystem {
	
	private VictorSP climberMotor = new VictorSP(RobotMap.CLIMBER_MOTOR);
	
	private enum States{CLIMBING, NEUTRAL};
	private States climbState = States.NEUTRAL;
	
    public void initDefaultCommand() {
        setDefaultCommand(new ClimbCommand());
    }
    public void actuateClimb(double speed){
    	if(climbState.compareTo(States.NEUTRAL)==0){
    		actuateClimberMotor(speed);
    		climbState = States.CLIMBING;
    	}else{
    		actuateClimberMotor(0);
    		climbState = States.NEUTRAL;
    	}
    } 
    private void actuateClimberMotor(double speed){
    	climberMotor.set(speed);
    }
    public void updatePeriodic(){
    	SmartDashboard.putString("Climber State", climbState.toString());
    }
}

