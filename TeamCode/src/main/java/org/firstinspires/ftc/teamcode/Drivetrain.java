package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Mecanum drivetrain subsystem (4 motors) plus the goBILDA Pinpoint odometry computer.
 * This is a plain helper class, not an OpMode: MainTeleOp, Teleop_DriveTrain and
 * Auto_Park all create one with {@code new Drivetrain(hardwareMap)}.
 */
public class Drivetrain {

    // Pinpoint pod offsets from the robot's center of rotation, in millimeters.
    // X offset: how far left (+) / right (-) the forward (X) pod is.
    // Y offset: how far forward (+) / back (-) the strafe (Y) pod is.
    // Measure these on the real robot / CAD; heading is accurate even if they are 0.
    public static double PINPOINT_X_OFFSET_MM = 0.0;
    public static double PINPOINT_Y_OFFSET_MM = 0.0;

    private final DcMotor frontLeftMotor;
    private final DcMotor backLeftMotor;
    private final DcMotor frontRightMotor;
    private final DcMotor backRightMotor;

    // Background tracking system variables restored for TeleOp compatibility
    private IMU imu;
    private GoBildaPinpointDriver pinpoint;

    public Drivetrain(HardwareMap hwMap) {
        frontLeftMotor = hwMap.get(DcMotor.class, "fl");
        frontRightMotor = hwMap.get(DcMotor.class, "fr");
        backLeftMotor = hwMap.get(DcMotor.class, "bl");
        backRightMotor = hwMap.get(DcMotor.class, "br");

        // Motor directions preserved exactly from your configuration tuning
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        for (DcMotor motor : new DcMotor[]{frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor}) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        // Initialize background sensors for orientation mapping
        imu = hwMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD)));

        // Safe setup loop for the goBILDA Pinpoint device
        pinpoint = hwMap.tryGet(GoBildaPinpointDriver.class, "pinpoint");
        if (pinpoint != null) {
            pinpoint.setOffsets(PINPOINT_X_OFFSET_MM, PINPOINT_Y_OFFSET_MM, DistanceUnit.MM);
            pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
            pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
            pinpoint.resetPosAndIMU();
        }
    }

    // ==================== ADDED INTERNAL HELPER METHODS ====================

    /** Must be called once per loop so odometry stays current. */
    public void update() {
        if (pinpoint != null) {
            pinpoint.update();
        }
    }

    /** Returns true if the pinpoint device is active. */
    public boolean hasPinpoint() {
        return pinpoint != null;
    }

    /** Robot heading in radians, counter-clockwise positive. */
    public double getHeading() {
        if (pinpoint != null) {
            return pinpoint.getHeading(AngleUnit.RADIANS);
        }
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    /** Resets the heading orientation tracker down to zero. */
    public void resetHeading() {
        if (pinpoint != null) {
            pinpoint.setHeading(0, AngleUnit.RADIANS);
        }
        imu.resetYaw();
    }

    /** Distance driven forward since start, in inches (0 without a Pinpoint). */
    public double getX() {
        return pinpoint != null ? pinpoint.getPosX(DistanceUnit.INCH) : 0.0;
    }

    /** Distance strafed left since start, in inches (0 without a Pinpoint). */
    public double getY() {
        return pinpoint != null ? pinpoint.getPosY(DistanceUnit.INCH) : 0.0;
    }

    /** Robot-centric mecanum drive calculations loop. */
    public void driveRobotCentric(double y, double x, double rx) {
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);

        frontLeftMotor.setPower((y + x + rx) / denominator);
        backLeftMotor.setPower((y - x + rx) / denominator);
        frontRightMotor.setPower((y - x - rx) / denominator);
        backRightMotor.setPower((y + x - rx) / denominator);
    }

    /** Field-centric mecanum drive using the Pinpoint (or IMU) heading calculations. */
    public void driveFieldCentric(double y, double x, double rx) {
        double botHeading = getHeading();

        // Rotate the movement direction counter to the bot's rotation vectors
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX = rotX * 1.1;  // Counteract imperfect friction tracking during strafing

        driveRobotCentric(rotY, rotX, rx);
    }
}
