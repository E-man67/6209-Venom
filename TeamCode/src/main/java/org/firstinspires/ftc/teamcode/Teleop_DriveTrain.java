
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Drivetrain-only field-centric test. Uses the same Drivetrain class as MainTeleOp,
 * so motor names and directions only need to be fixed in one place.
 */
@TeleOp(name="Drivetrain Standalone Control", group="Testing")
public class Teleop_DriveTrain extends LinearOpMode {

    @Override
    public void runOpMode() {
        Drivetrain drivetrain = new Drivetrain(hardwareMap);

        telemetry.addData("Status", "Drivetrain Ready");
        telemetry.addData("Pinpoint", drivetrain.hasPinpoint() ? "found" : "NOT FOUND, using IMU");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            drivetrain.update();

            double y = -gamepad1.left_stick_y; // Y stick value is reversed
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // Options (Start on Xbox-style controllers) re-zeroes "forward"
            if (gamepad1.options) {
                drivetrain.resetHeading();
            }

            drivetrain.driveFieldCentric(y, x, rx);

            telemetry.addData("Driving Mode", "Field Centric");
            telemetry.addData("Heading (deg)", "%.1f", Math.toDegrees(drivetrain.getHeading()));
            telemetry.addData("Position (in)", "X: %.1f | Y: %.1f", drivetrain.getX(), drivetrain.getY());
            telemetry.update();
        }
    }
}
