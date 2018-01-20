package team2935.robot.subsystems;

import java.text.DecimalFormat;

import com.kauailabs.navx.frc.AHRS;
import com.toronto.subsystems.T_Subsystem;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team2935.robot.Robot;
import team2935.robot.RobotConst;
import team2935.robot.RobotMap;
import team2935.robot.commands.drive.GameControllerDriveCommand;
import team2935.utils.DrivePIDController;
import team2935.utils.GyroPIDController;

public class ChassisSubsystem extends T_Subsystem {
	//Definition of the motors used on the drive_train subsystem
	private VictorSP leftMotor1 = new VictorSP(RobotMap.DRIVE_LEFT_MOTOR1);
	private VictorSP leftMotor2 = new VictorSP(RobotMap.DRIVE_LEFT_MOTOR2);
	private VictorSP leftMotor3 = new VictorSP(RobotMap.DRIVE_LEFT_MOTOR3);
	private VictorSP rightMotor1 = new VictorSP(RobotMap.DRIVE_RIGHT_MOTOR1);
	private VictorSP rightMotor2 = new VictorSP(RobotMap.DRIVE_RIGHT_MOTOR2);
	private VictorSP rightMotor3 = new VictorSP(RobotMap.DRIVE_RIGHT_MOTOR3);
	private Solenoid shifterHigh = new Solenoid(RobotMap.SOLENOID_SHIFTER_HIGH);
	private Solenoid shifterLow  = new Solenoid(RobotMap.SOLENOID_SHIFTER_LOW);
	
	//Definition of the sensors used on the drive_train subsystem
	private Encoder leftEncoder = new Encoder(RobotMap.DRIVE_LEFT_ENCODER_A,RobotMap.DRIVE_LEFT_ENCODER_B);
	private Encoder rightEncoder = new Encoder(RobotMap.DRIVE_RIGHT_ENCODER_A,RobotMap.DRIVE_RIGHT_ENCODER_B,true);
	private AHRS gyro = new AHRS(SerialPort.Port.kUSB);
	//private AnalogGyro gyro = new AnalogGyro(SPI.Port.kOnboardCS0);
	//Definition of PID controllers used for drive train control
	private DrivePIDController pidController = new DrivePIDController(RobotConst.DRIVE_LOW_PID_P,RobotConst.DRIVE_LOW_PID_D,
																	  RobotConst.DRIVE_HIGH_PID_P,RobotConst.DRIVE_HIGH_PID_D);	
	private GyroPIDController gyroController = new GyroPIDController(0.2);

	//Declaration of variables used to track the current state the robot is in
	private States shiftedState;
	public States transmissionState;
	public States robotDirection;
	private States pidState;
	public Motions motion;
	
	//Declaration of variables that need to be tracked for the AUTO SHIFTER controller
	public int prev_distance;
	private double powerInput;
	
	public enum States{	LOW,HIGH,HAS_SHIFTED,NOT_SHIFTED,ON,OFF,GEAR,CLIMBER };
	public enum Motions{ STRAIGHT,TURN,ROTATE };
	public void initDefaultCommand() {
		setDefaultCommand(new GameControllerDriveCommand());
	}
	public void robotInit(){
		prev_distance = 0;
		powerInput = 0;
		rightMotor1.setInverted(true);
		rightMotor2.setInverted(true);
		rightMotor3.setInverted(true);
		shiftedState = States.NOT_SHIFTED;
		motion = Motions.ROTATE;
		shiftLow();
		transmissionState = States.LOW;
		robotDirection = States.GEAR;
	//	gyro.reset();
	//	gyro.setAngleAdjustment(180);
		resetEncoders();
	}
	public void disabledInit(){
		prev_distance = 0;
		powerInput = 0;
	//	gyro.reset();
		resetEncoders();
	}
	public void resetEncoders(){
		leftEncoder.reset();
		rightEncoder.reset();
	}
	public void resetGyro(){
//		gyro.reset();
	}
	public double getAngle(){
		double angle = gyro.getAngle() % 360;
		if (angle < 0) {
			angle += 360.0;
		}
		return angle;
	}
	public double getEncoderDistance(){
		return (getLeftEncoderDistance() + getRightEncoderDistance())/2;
	}
	public double getLeftEncoderDistance(){
		return leftEncoder.getDistance();
	}
	public double getRightEncoderDistance(){
		return rightEncoder.getDistance();
	}
	public double getVelocity(){
		double velocity = ((getEncoderDistance() - prev_distance)/RobotConst.DRIVE_ENCODER_COUNTS_PER_FT)/ 0.027;
		DecimalFormat df = new DecimalFormat("#.0");
		return Double.valueOf(df.format(velocity));
	}
	/*public void speedController(double speed){
		if(shiftedState.compareTo(States.NOT_SHIFTED) == 0){
			if(getVelocity() > RobotConst.DRIVE_LOW_SHIFT_THRESHOLD && transmissionState.equals(States.LOW)){
				setAllMotorSpeeds(speed - RobotConst.DRIVE_PID_POWER_DROP);
				shiftHigh();
				transmissionState = States.HIGH;
				shiftedState = States.HAS_SHIFTED;
			}else if(getVelocity() < RobotConst.DRIVE_HIGH_SHIFT_THRESHOLD && transmissionState.equals(States.HIGH)){
				setAllMotorSpeeds(speed - RobotConst.DRIVE_PID_POWER_DROP);
				shiftLow();
				transmissionState = States.LOW;
				shiftedState = States.HAS_SHIFTED;
			}
			setAllMotorSpeeds(speed);
		}else if(shiftedState.compareTo(States.HAS_SHIFTED) == 0){
			setAllMotorSpeeds(speed);
			shiftedState = States.NOT_SHIFTED;
		}
	}*/
	public void setAllMotorSpeeds(double speed){
		setLeftMotorSpeeds(speed);
		setRightMotorSpeeds(speed);
	}
	public void setDifferentMotorSpeeds(double leftSpeed, double rightSpeed){
		setLeftMotorSpeeds(leftSpeed);
		setRightMotorSpeeds(rightSpeed);
	}
	public void setLeftMotorSpeeds(double speed){
		if(transmissionState.compareTo(States.LOW)==0){
			powerInput = speed/RobotConst.MAX_LOW_DRIVE_SPEED;
		}else{
			powerInput = speed/RobotConst.MAX_HIGH_DRIVE_SPEED;
		}
		if(powerInput > 1.0){powerInput = 1.0;}
		else if(powerInput < -1.0){powerInput = -1.0;}
		
		leftMotor1.set(speed);
		leftMotor2.set(speed);
		leftMotor3.set(speed);
	}
	public void setRightMotorSpeeds(double speed){
		if(transmissionState.compareTo(States.LOW)==0){
			powerInput = speed/RobotConst.MAX_LOW_DRIVE_SPEED;
		}else{
			powerInput = speed/RobotConst.MAX_HIGH_DRIVE_SPEED;
		}
		if(powerInput > 1.0){powerInput = 1.0;}
		else if(powerInput < -1.0){powerInput = -1.0;}
		rightMotor1.set(speed);
		rightMotor2.set(speed);
		rightMotor3.set(speed);
	}
	public States togglePID(){
		if(Robot.oi.getDriveSpeed() <= 0.2){
			pidState = States.OFF;
		}else{
			pidState = States.ON;
		}
		return pidState;
	}	
	public double toggleDirection(double power){
		if(robotDirection.compareTo(States.CLIMBER)==0){
			return power *-1;
		}
		return power;
	} 
	public void shiftHigh(){
		shifterHigh.set(true);
		shifterLow.set(false);
		transmissionState = States.HIGH;
	}
	public void shiftLow(){
		shifterHigh.set(false);
		shifterLow.set(true);
		transmissionState = States.LOW;
	}
	public void actuateShift(){
		if(transmissionState.compareTo(States.LOW)==0){
			shiftHigh();
		}else{
			shiftLow();
		}
	}
	@Override
	public void updatePeriodic() {
    	SmartDashboard.putData("Gyro", gyro);
    	SmartDashboard.putNumber("Gyro", getAngle());
    	SmartDashboard.putNumber("Gyro rate", gyro.getRate());
    	SmartDashboard.putNumber("Velocity",getVelocity());
    	SmartDashboard.putString("Transmission", transmissionState.toString());
    	SmartDashboard.putString("Robot Direction", robotDirection.toString());
	}
}
