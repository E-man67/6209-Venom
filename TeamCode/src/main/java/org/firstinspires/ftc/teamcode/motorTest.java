package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;



    @TeleOp(name = "Motor ID Tester", group = "Testing")
    public class motorTest extends LinearOpMode {

        @Override
        public void runOpMode() {

            // Change these names to match the names in your Robot Configuration
            DcMotor motor0 = hardwareMap.get(DcMotor.class, "fl");
            DcMotor motor1 = hardwareMap.get(DcMotor.class, "fr");
            DcMotor motor2 = hardwareMap.get(DcMotor.class, "bl");
            DcMotor motor3 = hardwareMap.get(DcMotor.class, "br");

            telemetry.addLine("=== MOTOR ID TESTER ===");
            telemetry.addLine("Press START to begin");
            telemetry.addLine("");
            telemetry.addLine("Controls:");
            telemetry.addLine("A = frontLeft");
            telemetry.addLine("B = frontRight");
            telemetry.addLine("X = backLeft");
            telemetry.addLine("Y = backRight");
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
                    motor0.setPower(1.0);
                    selected = "frontLeft";
                }

                if (gamepad1.b) {
                    motor1.setPower(1.0);
                    selected = "frontRight";
                }

                if (gamepad1.x) {
                    motor2.setPower(1.0);
                    selected = "backLeft";
                }

                if (gamepad1.y) {
                    motor3.setPower(1.0);
                    selected = "backRight";
                }

                telemetry.addLine("=== MOTOR ID TESTER ===");
                telemetry.addData("Selected", selected);
                telemetry.addLine("");
                telemetry.addLine("A = frontLeft");
                telemetry.addLine("B = frontRight");
                telemetry.addLine("X = backLeft");
                telemetry.addLine("Y = backRight");
                telemetry.addLine("");
                telemetry.addData("frontLeft encoder", motor0.getCurrentPosition());
                telemetry.addData("frontRight encoder", motor1.getCurrentPosition());
                telemetry.addData("backLeft encoder", motor2.getCurrentPosition());
                telemetry.addData("backRight encoder", motor3.getCurrentPosition());

                telemetry.update();
            }

            // Make absolutely sure everything stops
            motor0.setPower(0);
            motor1.setPower(0);
            motor2.setPower(0);
            motor3.setPower(0);
        }
    }

