package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name="Drivetrain Standalone Control", group="Testing")
public class Drivetrain extends LinearOpMode {

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;

    /**
     * Constructor used by MainTeleOp to initialize the chassis seamlessly as a sub-mechanism.
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
     * Corrected hardware direction layout.
     */
    private void initializeHardware(HardwareMap hwMap) {
        // Control Hub ports 0, 1, 2, 3
        leftFront  = hwMap.get(DcMotor.class, "left_front");
        rightFront = hwMap.get(DcMotor.class, "right_front");
        leftBack   = hwMap.get(DcMotor.class, "left_back");
        rightBack  = hwMap.get(DcMotor.class, "right_back");

        // Left side motors are standard REVERSE
        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);

        // ADJUSTED RIGHT SIDE: Setting both right motors to FORWARD to ensure they rotate together
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        // Enforce instant braking when power drops to zero
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Standard Mecanum Kinematic Equations.
     * Preserves exact method signature so MainTeleOp logic functions properly.
     */
    public void driveRobotCentric(double y, double x, double rx) {
        double leftFrontPower  = y + x + rx;
        double leftBackPower   = y - x + rx;
        double rightFrontPower = y - x - rx;
        double rightBackPower  = y + x - rx;

        // Normalize vector distributions so they never exceed power constraints (-1.0 to 1.0)
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);

        leftFront.setPower(leftFrontPower / denominator);
        leftBack.setPower(leftBackPower / denominator);
        rightFront.setPower(rightFrontPower / denominator);
        rightBack.setPower(rightBackPower / denominator);
    }

    /**
     * Execution loop that runs ONLY when you select this program directly on the Driver Hub.
     */
    @Override
    public void runOpMode() {
        initializeHardware(hardwareMap);

        telemetry.addData("Status", "Standalone Drivetrain Control Initialized!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // MIRRORED CONTROLS: Combine both joysticks together.
            // Pushing either stick up will drive forward. Pushing either stick left/right will strafe.
            double drive = (-gamepad1.left_stick_y) + (-gamepad1.right_stick_y);
            double strafe = gamepad1.left_stick_x + gamepad1.right_stick_x;

            // Temporary turning variable set to 0 to prioritize straight directional verification
            double turn = 0.0;

            // Execute driving math
            driveRobotCentric(drive, strafe, turn);

            telemetry.addData("Standalone Test Mode", "RUNNING");
            telemetry.addData("Calculated Drive", "Y: %.2f | X: %.2f", drive, strafe);
            telemetry.update();
        }
    }
}
