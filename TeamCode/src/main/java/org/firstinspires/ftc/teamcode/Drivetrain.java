package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name="Drivetrain Standalone Control", group="Testing")
public class Drivetrain extends LinearOpMode {

    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;
    private IMU imu;

    /**
     * Constructor used by MainTeleOp to initialize the chassis as a sub-mechanism.
     */
    public Drivetrain(HardwareMap hardwareMap) {
        initializeHardware(hardwareMap);
    }

    /**
     * Empty constructor required by the FTC SDK to run this file directly as an independent OpMode.
     */
    public Drivetrain() {
        // Kept empty intentionally
    }

    /**
     * Unified hardware direction layout pulled straight from Game Manual 0.
     */
    private void initializeHardware(HardwareMap hwMap) {
        // Fallback checks handle profile name variances automatically
        try {
            frontLeftMotor  = hwMap.dcMotor.get("frontLeftMotor");
            frontRightMotor = hwMap.dcMotor.get("frontRightMotor");
            backLeftMotor   = hwMap.dcMotor.get("backLeftMotor");
            backRightMotor  = hwMap.dcMotor.get("backRightMotor");
        } catch (Exception e) {
            // Safe fallback if names match your original layout profile mapping
            frontLeftMotor  = hwMap.dcMotor.get("left_front");
            frontRightMotor = hwMap.dcMotor.get("right_front");
            backLeftMotor   = hwMap.dcMotor.get("left_back");
            backRightMotor  = hwMap.dcMotor.get("right_back");
        }

        // Reversing the right side motors per standard Game Manual 0 geometry mapping
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Retrieve and initialize the IMU sensor for field-centric orientation tracking
        imu = hwMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);
    }

    /**
     * Robot-Centric Driving method required to maintain seamless compatibility with MainTeleOp.
     * Maps perfectly to the mathematical constraints of the Game Manual 0 algorithm.
     */
    public void driveRobotCentric(double y, double x, double rx) {
        // Compute standard holonomic scaling factors matching the pure math parameters
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);

        double frontLeftPower  = (y + x + rx) / denominator;
        double backLeftPower   = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower  = (y + x - rx) / denominator;

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);
    }

    /**
     * Optional utility method if you ever want to leverage the IMU field-centric code inside MainTeleOp.
     */
    public void driveFieldCentric(double y, double x, double rx, boolean resetYaw) {
        if (resetYaw) {
            imu.resetYaw();
        }

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Rotate the movement direction counter to the bot's rotation
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX = rotX * 1.1;  // Counteract imperfect strafing

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1.0);

        frontLeftMotor.setPower((rotY + rotX + rx) / denominator);
        backLeftMotor.setPower((rotY - rotX + rx) / denominator);
        frontRightMotor.setPower((rotY - rotX - rx) / denominator);
        backRightMotor.setPower((rotY + rotX - rx) / denominator);
    }

    /**
     * Execution loop that runs ONLY when you select this program directly on the Driver Hub.
     */
    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware(hardwareMap);

        telemetry.addData("Status", "Standalone Game Manual 0 Drivetrain Ready!");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // Run the pure Game Manual 0 Field Centric system loop directly
            driveFieldCentric(y, x, rx, gamepad1.options);

            telemetry.addData("Driving Mode", "Field Centric Active");
            telemetry.update();
        }
    }
}
