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
    private final IMU imu;
    private final GoBildaPinpointDriver pinpoint; // null if not in the robot configuration

    public Drivetrain(HardwareMap hwMap) {
        frontLeftMotor  = hwMap.get(DcMotor.class, "fl");
        frontRightMotor = hwMap.get(DcMotor.class, "fr");
        backLeftMotor   = hwMap.get(DcMotor.class, "bl");
        backRightMotor  = hwMap.get(DcMotor.class, "br");

        // Motor directions. With the right side REVERSED (the Game Manual 0 default) this robot
        // spun in circles on forward stick, so both sides are FORWARD here.
        // If it now drives backwards on forward stick, change all four to REVERSE.
        // If just one wheel spins the wrong way, flip only that motor.
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        for (DcMotor motor : new DcMotor[]{frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor}) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }