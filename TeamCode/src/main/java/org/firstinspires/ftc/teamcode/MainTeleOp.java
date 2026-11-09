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
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Refreshes background tracking data inside your drivetrain class
            drivetrain.update();

            // ==================== DRIVETRAIN CONTROLS (Gamepad 1) ====================
            double drive = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            // Calls the robot-centric method inside your corrected Game Manual 0 drivetrain class
            drivetrain.driveRobotCentric(drive, strafe, turn);

            // ==================== TRANSFER CONTROLS (Gamepad 1 Override) ====================
            double targetIntake = 0.0;
            double targetConveyor = 0.0;

            if (gamepad1.right_bumper) {
                targetIntake = 1.0;      // Spin intake inward
                targetConveyor = 0.5;    // Move conveyor up
            } else if (gamepad1.left_bumper) {
                targetIntake = -1.0;     // Purge intake outward
                targetConveyor = -1.0;   // Move conveyor backward
            } else if (gamepad1.a) {
                targetConveyor = 0.5;    // Stage/Feed ball independently without running intake
            }

            transfer.runTransfer(targetIntake, targetConveyor);

            // ==================== SHOOTER CONTROLS (Gamepad 1 Override) ====================
            if (gamepad1.x) {
                shooter.runShooter(0.7);  // Power up both flywheel motors to max speed
            } else if (gamepad1.y) {
                shooter.runShooter(0.0);  // Spin down both flywheels safely
            }

            // ==================== CONTROLLER LAYOUT VISUALS FOR DRIVER HUB ====================
            telemetry.addLine("=== MECHANISM CONTROL INTERFACE LAYOUT ===");
            telemetry.addLine("Right Bumper [RB] -> RUN INTAKE (1.0) & FEED CONVEYOR (0.5)");
            telemetry.addLine("Left Bumper  [LB] -> PURGE INTAKE (-1.0) & REVERSE CONVEYOR (-1.0)");
            telemetry.addLine("Button       [A]  -> RUN CONVEYOR INDEPENDENTLY (0.5)");
            telemetry.addLine("Button       [X]  -> SPIN FLYWHEELS (0.7 Wall-Shot Power)");
            telemetry.addLine("Button       [Y]  -> KILL FLYWHEELS (0.0 Power)");
            telemetry.addLine("------------------------------------------------");

            // ==================== TELEMETRY FEEDBACK ====================
            telemetry.addData("Drive Active", "Y: %.2f | X: %.2f", drive, strafe);

            // These methods pull diagnostics safely from your Shooter and Drivetrain variables
            telemetry.addData("Shooter Status", shooter.isRunning() ? "SPINNING" : "IDLE");
            telemetry.addData("Shooter Velocity (ticks/s)", "L: %.0f | R: %.0f",
                    shooter.getLeftVelocity(), shooter.getRightVelocity());
            telemetry.addData("Position (in)", "X: %.1f | Y: %.1f | Heading: %.1f",
                    drivetrain.getX(), drivetrain.getY(), Math.toDegrees(drivetrain.getHeading()));

            double drive2 = -gamepad2.left_stick_y;
            double strafe2 = gamepad2.left_stick_x;
            double turn2 = gamepad2.right_stick_x;

            // Calls the robot-centric method inside your corrected Game Manual 0 drivetrain class
            drivetrain.driveRobotCentric(drive2, strafe2, turn2);

            // ==================== TRANSFER CONTROLS (Gamepad 2) ====================
            double targetIntake2 = 0.0;
            double targetConveyor2 = 0.0;

            if (gamepad2.right_bumper) {
                targetIntake2 = 1.0;      // Spin intake inward
                targetConveyor2 = 0.5;    // Move conveyor up
            } else if (gamepad2.left_bumper) {
                targetIntake2 = -1.0;     // Purge intake outward
                targetConveyor2 = -1.0;   // Move conveyor backward
            } else if (gamepad2.a) {
                targetConveyor2 = 0.5;    // Stage/Feed ball independently without running intake
            }

            transfer.runTransfer(targetIntake2, targetConveyor2);

            // ==================== SHOOTER CONTROLS (Gamepad 2) ====================
            if (gamepad2.x) {
                shooter.runShooter(0.7);  // Power up both flywheel motors to max speed
            } else if (gamepad2.y) {
                shooter.runShooter(0.0);  // Spin down both flywheels safely
            }

            // ==================== TELEMETRY FEEDBACK ====================
            telemetry.addData("Drive 2 Active", "Y: %.2f | X: %.2f", drive2, strafe2);

            // These methods pull diagnostics safely from your Shooter and Drivetrain variables
            telemetry.addData("Shooter Status", shooter.isRunning() ? "SPINNING" : "IDLE");
            telemetry.addData("Shooter Velocity (ticks/s)", "L: %.0f | R: %.0f",
                    shooter.getLeftVelocity(), shooter.getRightVelocity());
            telemetry.addData("Position (in)", "X: %.1f | Y: %.1f | Heading: %.1f",
                    drivetrain.getX(), drivetrain.getY(), Math.toDegrees(drivetrain.getHeading()));
            telemetry.update();
        }
    }
}
