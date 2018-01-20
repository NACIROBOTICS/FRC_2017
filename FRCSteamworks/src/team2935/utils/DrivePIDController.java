package team2935.utils;

import team2935.robot.Robot;
import team2935.robot.RobotConst;
import team2935.robot.subsystems.ChassisSubsystem.Motions;
import team2935.robot.subsystems.ChassisSubsystem.States;

public class DrivePIDController {
	//Declaration of PID values
		private double low_k_p;
		private double low_k_d;
		private double high_k_p;
		private double high_k_d;
	//Declaration of variables that need to be tracked in the PID controller
		private double prev_error;
		private double delta_error;
		//private double integral_error;
		
		States transmissionState = States.LOW;
		
	public DrivePIDController(double low_pid_p, double low_pid_d,double high_pid_p, double high_pid_d){
		low_k_p = low_pid_p;
		low_k_d = low_pid_d;
		high_k_p = high_pid_p;
		high_k_d = high_pid_d;
	}
		
	private double calcPValue(double error){
		if(transmissionState.compareTo(States.LOW)==0)
			return error * low_k_p;
		else
			return error * high_k_p;
	}
	private double calcDValue(double error){
		delta_error = prev_error - error;
		if(transmissionState.compareTo(States.LOW)==0)
			return delta_error * low_k_d;
		else
			return delta_error * high_k_d; 
	}
	public double calcPIDValue(double setPoint, double feedback,double powerInput, States transmission, States pidState){
		
		double error;
		this.transmissionState = transmission;
		
		if(pidState.compareTo(States.ON)==0){
			if(Robot.chassisSubsystem.motion.compareTo(Motions.STRAIGHT) == 0){
				if(transmissionState.compareTo(States.LOW)==0)
					error = (setPoint - feedback)/RobotConst.MAX_LOW_DRIVE_SPEED;
				else
					error = (setPoint - feedback)/RobotConst.MAX_HIGH_DRIVE_SPEED;
			}else{
				if(transmissionState.compareTo(States.LOW)==0)
					error = (setPoint - feedback)/RobotConst.MAX_LOW_TURN_SPEED;
				else
					error = (setPoint - feedback)/RobotConst.MAX_HIGH_TURN_SPEED;
			}
			double p_out = calcPValue(error);
	    	double d_out = calcDValue(error);
	     
	    	double output = powerInput + p_out;
	    	
	    	double normalizedOutput  = output;
	    	normalizedOutput = (normalizedOutput > 1.0) ? 1.0 : normalizedOutput;
	    	normalizedOutput = (normalizedOutput < -1.0) ? -1.0 : normalizedOutput;
	    	prev_error = error;
	    	
	    	return normalizedOutput;
		}else{
			if(transmissionState.compareTo(States.HIGH)==0)
				return setPoint/RobotConst.MAX_HIGH_DRIVE_SPEED;
			else
				return setPoint/RobotConst.MAX_LOW_DRIVE_SPEED;
		}
	}
}
