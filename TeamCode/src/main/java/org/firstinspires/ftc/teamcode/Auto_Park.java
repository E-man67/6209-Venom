package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Mecanum Park Auto", group="Autonomous")
public class Auto_Park extends LinearOpMode {

    // Declare the 4 Mecanum motors
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        // Initialize motors using exact names from your Driver Station configuration
        leftFront  = hardwareMap.get(DcMotor.class, "left_front");
        rightFront = hardwareMap.get(DcMotor.class, "right_front");
        leftBack   = hardwareMap.get(DcMotor.class, "left_back");
        rightBack  = hardwareMap.get(DcMotor.class, "right_back");

        // Typically, left motors remain FORWARD, right motors are REVERSED
        // so that positive power drives all wheels forward.
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Ready to Run");
        telemetry.update();

        // Wait for the game to start (Driver presses PLAY)
        waitForStart();
        runtime.reset();

        // Step 1: Drive forward at 50% pwr
        leftFront.setPower(0.5);
        leftBack.setPower(0.5);
        rightFront.setPower(0.5);
        rightBack.setPower(0.5);

        // Run for 2.0 seconds (adjust this duration to fit your starting position)
        while (opModeIsActive() && (runtime.seconds() < 2.0)) {
            telemetry.addData("Path", "Driving Forward: %2.1f S", runtime.seconds());
            telemetry.update();
        }

        // Step 2: Brake and stop all motors
        leftFront.setPower(0.0);
        leftBack.setPower(0.0);
        rightFront.setPower(0.0);
        rightBack.setPower(0.0);

        telemetry.addData("Status", "Complete");
        telemetry.update();
    }
}
