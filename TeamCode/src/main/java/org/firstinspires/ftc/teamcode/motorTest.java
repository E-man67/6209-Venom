package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;



    @TeleOp(name = "Motor ID Tester", group = "Testing")
    public class motorTest extends LinearOpMode {

        @Override
        public void runOpMode() {

            // Change these names to match the names in your Robot Configuration
            DcMotor motor0 = hardwareMap.get(DcMotor.class, "motor0");
            DcMotor motor1 = hardwareMap.get(DcMotor.class, "motor1");
            DcMotor motor2 = hardwareMap.get(DcMotor.class, "motor2");
            DcMotor motor3 = hardwareMap.get(DcMotor.class, "motor3");

            telemetry.addLine("=== MOTOR ID TESTER ===");
            telemetry.addLine("Press START to begin");
            telemetry.addLine("");
            telemetry.addLine("Controls:");
            telemetry.addLine("A = Motor 0");
            telemetry.addLine("B = Motor 1");
            telemetry.addLine("X = Motor 2");
            telemetry.addLine("Y = Motor 3");
            telemetry.addLine("");
            telemetry.addLine("Selected motor runs while button is held.");
            telemetry.update();

            waitForStart();

            while (opModeIsActive()) {

                // Stop all motors first
                motor0.setPower(0);
                motor1.setPower(0);
                motor2.setPower(0);
                motor3.setPower(0);

                String selected = "None";

                if (gamepad1.a) {
                    motor0.setPower(0.3);
                    selected = "Motor 0";
                }

                if (gamepad1.b) {
                    motor1.setPower(0.3);
                    selected = "Motor 1";
                }

                if (gamepad1.x) {
                    motor2.setPower(0.3);
                    selected = "Motor 2";
                }

                if (gamepad1.y) {
                    motor3.setPower(0.3);
                    selected = "Motor 3";
                }

                telemetry.addLine("=== MOTOR ID TESTER ===");
                telemetry.addData("Selected", selected);
                telemetry.addLine("");
                telemetry.addLine("A = motor0");
                telemetry.addLine("B = motor1");
                telemetry.addLine("X = motor2");
                telemetry.addLine("Y = motor3");
                telemetry.addLine("");
                telemetry.addData("motor0 encoder", motor0.getCurrentPosition());
                telemetry.addData("motor1 encoder", motor1.getCurrentPosition());
                telemetry.addData("motor2 encoder", motor2.getCurrentPosition());
                telemetry.addData("motor3 encoder", motor3.getCurrentPosition());

                telemetry.update();
            }

            // Make absolutely sure everything stops
            motor0.setPower(0);
            motor1.setPower(0);
            motor2.setPower(0);
            motor3.setPower(0);
        }
    }

