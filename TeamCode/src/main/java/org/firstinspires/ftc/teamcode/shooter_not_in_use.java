package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name="DECODE Shooter PIDF Test", group="TeleOp")
public class shooter_not_in_use extends LinearOpMode {

    private DcMotorEx shooterMotor;
    private FlywheelController_not_in_use pidfController;

    // Start tuning with kF first, keeping others at 0.0
    public static double P = 0.005;
    public static double I = 0.001;
    public static double D = 0.0001;
    public static double F = 0.00035; // Rough estimate based on motor specifications

    @Override
    public void runOpMode() {
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter_motor");

        // Ensure motor runs without internal SDK constraints interfering with our PIDF calculations
        shooterMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        pidfController = new FlywheelController_not_in_use(P, I, D, F);

        waitForStart();

        while (opModeIsActive()) {
            // Live update values if configuring live parameters via dashboard or testing scripts
            pidfController.updateGains(P, I, D, F);

            // Press 'A' to spin up to scoring velocity, release or press 'B' to turn off
            if (gamepad1.a) {
                pidfController.setTargetVelocity(1800); // Ticks per second example value
            } else if (gamepad1.b) {
                pidfController.setTargetVelocity(0);
            }

            // Retrieve current actual encoder speed
            double currentTicksPerSec = shooterMotor.getVelocity();

            // Calculate correction power and apply to system hardware
            double calculatedPower = pidfController.calculatePower(currentTicksPerSec);
            shooterMotor.setPower(calculatedPower);

            telemetry.addData("Target Velocity", 1800);
            telemetry.addData("Actual Velocity", currentTicksPerSec);
            telemetry.addData("Applied Power", calculatedPower);
            telemetry.update();
        }
    }
}
