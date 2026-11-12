package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Official Robot TeleOp", group="Linear Opmode")
public class MainTeleOp extends LinearOpMode {

    private Drivetrain drivetrain;
    private int velocity = 750;
    private Transfer transfer;
    private Shooter shooter;

    double targetIntake = 0.0;
    double targetConveyor = 0.0;

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


            if (gamepad1.right_bumper || gamepad2.right_bumper) {
                transfer.runTransfer(1,0.5);      // Spin intake inward
                
            } else if (gamepad1.left_bumper || gamepad2.left_bumper) {
                transfer.runTransfer(-1,-1);// Move conveyor backward
            } else if (gamepad1.a || gamepad2.a) {
                targetConveyor = 0.5; // Stage/Feed ball independently without running intake
            }


            // ==================== SHOOTER CONTROLS (Gamepad 1 Override) ====================
            if (gamepad1.x || gamepad2.x) {
                shooter.runShooter(velocity);  // Power up both flywheel motors to max speed
            } else if (gamepad1.y || gamepad2.y) {
                shooter.runShooter(0.0);  // Spin down both flywheels safely
            }

            if (gamepad1.dpad_down || gamepad2.dpad_down){
                velocity -= 50;
            }
            if (gamepad1.dpad_up || gamepad2.dpad_up){
                velocity += 50;
            }

            // ==================== CONTROLLER LAYOUT VISUALS FOR DRIVER HUB ====================
            telemetry.addLine("=== MECHANISM CONTROL INTERFACE LAYOUT ===");
            telemetry.addLine("Right Bumper [RB] -> RUN INTAKE (1.0) & FEED CONVEYOR (0.5)");
            telemetry.addLine("Left Bumper  [LB] -> REVERSE INTAKE (-1.0) & REVERSE CONVEYOR (-1.0)");
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

            telemetry.update();



        }
    }
}
