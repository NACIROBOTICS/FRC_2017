
package team2935.robot;

import com.toronto.oi.T_Axis;
import com.toronto.oi.T_Stick;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team2935.oi.OI;
import team2935.robot.commands.drive.GameControllerDriveCommand;
import team2935.robot.subsystems.ChassisSubsystem;

public class Robot extends IterativeRobot {

	//Definition of robot subsystems
	public static final ChassisSubsystem chassisSubsystem = new ChassisSubsystem();
	//public static final GearSubsystem gearSubsystem = new GearSubsystem();
	//public static final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
	//public static final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
	
	//public static ArrayList<T_Subsystem> subsystemList = new ArrayList<>();
	
	public static OI oi;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	@Override
	public void robotInit() {
		oi = new OI();
		chooser.addDefault("Default Auto", new GameControllerDriveCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		chassisSubsystem.robotInit();
		//subsystemList.add(chassisSubsystem);
		//subsystemList.add(intakeSubsystem);
		//subsystemList.add(shooterSubsystem);	
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		if(autonomousCommand != null){
			Scheduler.getInstance().add(autonomousCommand);
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		SmartDashboard.putString("Controller",oi.driverController.toString());
		chassisSubsystem.updatePeriodic();
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
		//chassisSubsystem.testMotors();
	}
}
