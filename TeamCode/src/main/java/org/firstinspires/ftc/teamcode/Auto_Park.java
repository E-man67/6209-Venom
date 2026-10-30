
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Mecanum Park Auto", group="Autonomous")
public class Auto_Park extends LinearOpMode {

    // How far to drive forward to park, and a time limit in case odometry isn't working
    private static final double PARK_DISTANCE_INCHES = 24.0;
    private static final double PARK_TIMEOUT_SECONDS = 2.0;
    private static final double PARK_POWER = 0.5;

    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        // Same motor names and directions as TeleOp
        Drivetrain drivetrain = new Drivetrain(hardwareMap);

        telemetry.addData("Status", "Ready to Run");
        telemetry.addData("Pinpoint", drivetrain.hasPinpoint() ? "found" : "NOT FOUND, timed park only");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // Drive forward until the Pinpoint says we've gone far enough, or time runs out
        drivetrain.driveRobotCentric(PARK_POWER, 0, 0);
        while (opModeIsActive() && runtime.seconds() < PARK_TIMEOUT_SECONDS) {
            drivetrain.update();
            if (drivetrain.hasPinpoint() && drivetrain.getX() >= PARK_DISTANCE_INCHES) {
                break;
            }
            telemetry.addData("Path", "Driving Forward: %2.1f S", runtime.seconds());
            telemetry.addData("Distance (in)", "%.1f", drivetrain.getX());
            telemetry.update();
        }

        drivetrain.stop();

        telemetry.addData("Status", "Complete");
        telemetry.update();
    }
}
