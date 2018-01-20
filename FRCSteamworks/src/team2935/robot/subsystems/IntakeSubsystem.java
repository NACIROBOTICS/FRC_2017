package team2935.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import team2935.robot.RobotMap;
import team2935.robot.commands.intake.GearRollerCommand;

public class IntakeSubsystem extends Subsystem {
	
	Spark intakeMotor = new Spark(RobotMap.INTAKE_MOTOR);
	
    public void initDefaultCommand() {
        setDefaultCommand(new GearRollerCommand());
    }
    public void runIntake(double speed){
    	intakeMotor.set(speed);
    }
}

