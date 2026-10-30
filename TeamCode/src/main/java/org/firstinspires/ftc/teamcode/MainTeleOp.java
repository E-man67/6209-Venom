

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Official Robot TeleOp", group="Linear Opmode")
public class MainTeleOp extends LinearOpMode {

    private Drivetrain drivetrain;
    private Transfer transfer;
    private Shooter shooter;

    @Override
    public void runOpMode() {
        // Initialize all subsystems using the active hardwareMap
        drivetrain = new Drivetrain(hardwareMap);
        transfer = new Transfer(hardwareMap);
        shooter = new Shooter(hardwareMap);

        telemetry.addData("Status", "Initialized 8 Total Motors (4 Drive, 4 Expansion)");
        telemetry.addData("Pinpoint", drivetrain.hasPinpoint() ? "found" : "NOT FOUND");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            drivetrain.update();

            // ==================== DRIVETRAIN CONTROLS (Gamepad 1) ====================
            double drive = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            // Calls the robot-centric method inside your corrected Game Manual 0 drivetrain class
            drivetrain.driveRobotCentric(drive, strafe, turn);

            // ==================== TRANSFER CONTROLS (Gamepad 2) ====================
            double targetIntake = 0.0;
            double targetConveyor = 0.0;

            if (gamepad2.right_bumper) {
                targetIntake = 0.8;      // Spin intake inward
                targetConveyor = 0.7;    // Move conveyor up
            } else if (gamepad2.left_bumper) {
                targetIntake = -0.8;     // Purge intake outward
                targetConveyor = -0.7;   // Move conveyor backward
            } else if (gamepad2.a) {
                targetConveyor = 0.7;    // Stage/Feed ball independently without running intake
            }

            transfer.runTransfer(targetIntake, targetConveyor);

            // ==================== SHOOTER CONTROLS (Gamepad 2) ====================
            if (gamepad2.x) {
                shooter.runShooter(1.0);  // Power up both flywheel motors to max speed
            } else if (gamepad2.y) {
                shooter.runShooter(0.0);  // Spin down both flywheels safely
            }

            // ==================== TELEMETRY FEEDBACK ====================
            telemetry.addData("Drive Active", "Y: %.2f | X: %.2f", drive, strafe);
            telemetry.addData("Shooter Status", shooter.isRunning() ? "SPINNING" : "IDLE");
            telemetry.addData("Shooter Velocity (ticks/s)", "L: %.0f | R: %.0f",
                    shooter.getLeftVelocity(), shooter.getRightVelocity());
            telemetry.addData("Position (in)", "X: %.1f | Y: %.1f | Heading: %.1f",
                    drivetrain.getX(), drivetrain.getY(), Math.toDegrees(drivetrain.getHeading()));
            telemetry.update();
        }
    }
}

