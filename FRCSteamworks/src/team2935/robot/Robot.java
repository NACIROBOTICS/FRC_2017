
package team2935.robot;

import java.util.ArrayList;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team2935.oi.OI;
import team2935.robot.commands.auto.LeftGearAuto;
import team2935.robot.commands.auto.LeftGearShoot;
import team2935.robot.commands.auto.MiddleGearAuto;
import team2935.robot.commands.auto.MiddleGearLeftShoot;
import team2935.robot.commands.auto.MiddleGearRightShoot;
import team2935.robot.commands.auto.RightGearAuto;
import team2935.robot.commands.auto.RightGearShoot;
import team2935.robot.subsystems.ChassisSubsystem;
import team2935.robot.subsystems.ClimberSubsystem;
import team2935.robot.subsystems.GearSubsystem;
import team2935.robot.subsystems.IntakeSubsystem;
import team2935.robot.subsystems.ShooterSubsystem;
import team2935.vision.VisionSystem;

public class Robot extends IterativeRobot {

	//Definition of robot subsystems
	public static final ChassisSubsystem chassisSubsystem = new ChassisSubsystem();
	public static final GearSubsystem gearSubsystem = new GearSubsystem();
	public static final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
	public static final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
	public static final ClimberSubsystem climberSubsystem = new ClimberSubsystem();

	//public static final AutoSelector selector = new AutoSelector();

	public UsbCamera camera;
	
	public static final VisionSystem vision = new VisionSystem();
	
	public static ArrayList<Subsystem> subsystemList = new ArrayList<>();
	
	public static OI oi;

	private Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	@Override
	public void robotInit() {
		oi = new OI();
		subsystemList.add(chassisSubsystem);
		subsystemList.add(gearSubsystem);
		subsystemList.add(intakeSubsystem);
		subsystemList.add(shooterSubsystem);
		camera = CameraServer.getInstance().startAutomaticCapture();
		camera.getVideoMode();
		camera.setResolution(250, 250);
		initRobot();
		SmartDashboard.putData("Scheduler", Scheduler.getInstance());
		chooser.addDefault("Middle Gear",new MiddleGearAuto());
		chooser.addObject("Left Gear", new LeftGearAuto());
		chooser.addObject("Right Gear", new RightGearAuto());
		chooser.addObject("Middle Left Fuel", new MiddleGearLeftShoot());
		chooser.addObject("Middle Right Fuel", new MiddleGearRightShoot());
		chooser.addObject("Left Gear Shoot", new LeftGearShoot());
		chooser.addObject("Right Gear Shoot", new RightGearShoot());
		SmartDashboard.putData("Autonomous Selector", chooser);
		
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		SmartDashboard.putData("Autonomous Selector", chooser);
		Robot.oi.updateSmartDashboard();
		chassisSubsystem.disabledInit();
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		Robot.chassisSubsystem.resetGyro();
		autonomousCommand = chooser.getSelected();
		autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Robot.oi.updateSmartDashboard();
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
		Robot.oi.updateSmartDashboard();
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
	public void initRobot(){
		chassisSubsystem.robotInit();
		gearSubsystem.robotInit();
	}
}
