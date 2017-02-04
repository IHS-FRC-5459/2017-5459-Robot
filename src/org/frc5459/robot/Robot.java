/* Created Sat Jan 14 09:56:00 EST 2017 */
package org.frc5459.robot;

import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.components.Gyroscope;
import org.strongback.components.Solenoid;
import org.strongback.components.Solenoid.Direction;
import org.strongback.components.ui.FlightStick;
import org.strongback.control.TalonController;
import org.strongback.control.TalonController.ControlMode;
import org.strongback.hardware.Hardware;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	private FlightStick operator;
	private Solenoid bucket;
	private SwitchReactor reactor;
	private TalonController alphaMC;
	private TalonController betaMC;
	private TalonController charlieMC;
	private TalonController deltaMC;
	private TalonController echoMC;
	private TalonController foxtrotMC;
	private TalonController gammaMC;
	private Gyroscope gyro;
	private Encoder encoderRight;
	private Encoder encoderLeft
	


    @Override
    public void robotInit() {
    	//User Interfaces
    	operator = Hardware.HumanInterfaceDevices.logitechAttack3D(0);
    	reactor = Strongback.switchReactor();
    	
    	//Manipulator 
    	bucket = Hardware.Solenoids.doubleSolenoid(0, 1, Direction.EXTENDING);
    	
    	//Motors and Controllers
    	alphaMC = Hardware.Controllers.talonController(1, 11.37, 0.0); //TalonSRX #1
    	betaMC = Hardware.Controllers.talonController(2, 11.37, 0.0); //TalonSRX #2
    	charlieMC = Hardware.Controllers.talonController(3, 11.37, 0.0); //TalonSRX #3
    	deltaMC = Hardware.Controllers.talonController(4, 11.37, 0.0); //TalonSRX #4
    	echoMC = Hardware.Controllers.talonController(5, 11.37, 0.0); //TalonSRX #5
    	foxtrotMC = Hardware.Controllers.talonController(6, 11.37, 0.0); //TalonSRX #6
    	gammaMC = Hardware.Controllers.talonController(7, 11.37, 0.0); //TalonSRX #7
    	
    	//Setting Followers
    	//alphaMC is Left Side Master TalonSRX #1
    	betaMC.setControlMode(ControlMode.FOLLOWER);//TalonSRX #2
    	betaMC.withTarget(alphaMC.getDeviceID());
    	charlieMC.setControlMode(ControlMode.FOLLOWER); //TalonSRX #3
    	charlieMC.withTarget(alphaMC.getDeviceID());
    	//deltaMC is the manipulator (TalonSRX #4)
    	//echoMC is Right Side Master (TalonSRX #5)
    	foxtrotMC.setControlMode(ControlMode.FOLLOWER); //TalonSRX #6
    	foxtrotMC.withTarget(echoMC.getDeviceID());
    	gammaMC.setControlMode(ControlMode.FOLLOWER); //TalonSRX #7
    	gammaMC.withTarget(echoMC.getDeviceID());
    	
    	//Encoders
    	encoderRight = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    	encoderLeft = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    	
    	//Sensors
    	gyro = Hardware.AngleSensors.gyroscope(SPI.Port.kOnboardCS0);
    	
  
    }   
    
    @Override
    public void teleopInit() {
        // Start Strongback functions ...
        Strongback.start();
    }

    @Override
    public void teleopPeriodic() {
    	reactor.onTriggered(operator.getButton(5), () -> Strongback.submit(new BucketExtendCommand(bucket)));
    	reactor.onTriggered(operator.getButton(3), () -> Strongback.submit(new BucketRetractCommand(bucket)));
    	
    	
    	
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }

}
