package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Official Robot TeleOp", group="Linear Opmode")
public class MainTeleOp extends LinearOpMode {

    // Declare instance variables for our custom subsystem classes
    private Drivetrain drivetrain;
    private Transfer transfer;
    private Shooter shooter;

    @Override
    public void runOpMode() {
        // Initialize all subsystems using the active hardwareMap
        drivetrain = new Drivetrain(hardwareMap);
        transfer = new Transfer(hardwareMap);
        shooter = new Shooter(hardwareMap);

        telemetry.addData("Status", "Initialized. Waiting for Start...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // ==================== DRIVETRAIN CONTROLS (Gamepad 1) ====================
            // Gamepad joysticks are inverted vertically by default, so we reverse Y values
            double drive = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

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
                shooter.runShooter(1.0);  // Fire at maximum speed
            } else if (gamepad2.y) {
                shooter.runShooter(0.0);  // Power down launcher wheels
            }

            // ==================== OPERATOR TELEMETRY ====================
            telemetry.addData("Drive Power Vector", "Y: %.2f | X: %.2f | R: %.2f", drive, strafe, turn);
            telemetry.addData("Shooter Status", gamepad2.x ? "ACTIVE (LAUNCH)" : "IDLE");
            telemetry.update();
        }
    }
}
