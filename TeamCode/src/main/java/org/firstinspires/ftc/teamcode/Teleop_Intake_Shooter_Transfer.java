
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Controls the intake, transfer rollers, and shooter.
 *
 * This is a helper for your existing Mecanum TeleOp; it intentionally does not
 * contain drivetrain code or its own @TeleOp annotation.
 *
 * The six hardware names below must match your Driver Station Robot Configuration.
 */
public class Teleop_Intake_Shooter_Transfer {

    // Change these names to exactly match your Robot Configuration.
    private final DcMotor intakeLeft;
    private final DcMotor intakeRight;
    private final DcMotor transferLeft;
    private final DcMotor transferRight;
    private final DcMotor shooterLeft;
    private final DcMotor shooterRight;

    // Start with these power limits, then tune for your robot.
    private static final double INTAKE_MAX_POWER = 0.80;
    private static final double INTAKE_REVERSE_POWER = 0.40;
    private static final double TRANSFER_MAX_POWER = 0.75;
    private static final double TRANSFER_REVERSE_POWER = 0.35;
    private static final double SHOOTER_MAX_POWER = 1.00;
    private static final double SHOOTER_REVERSE_POWER = 0.25;

    public Teleop_Intake_Shooter_Transfer(HardwareMap hardwareMap) {
        intakeLeft = hardwareMap.get(DcMotor.class, "intakeLeft");
        intakeRight = hardwareMap.get(DcMotor.class, "intakeRight");
        transferLeft = hardwareMap.get(DcMotor.class, "transferLeft");
        transferRight = hardwareMap.get(DcMotor.class, "transferRight");
        shooterLeft = hardwareMap.get(DcMotor.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotor.class, "shooterRight");

        // These are example directions for mirrored left/right motor mounting.
        // If a pair spins the wrong way, change that pair's directions here.
        configureMotor(intakeLeft, DcMotor.Direction.FORWARD);
        configureMotor(intakeRight, DcMotor.Direction.REVERSE);
        configureMotor(transferLeft, DcMotor.Direction.FORWARD);
        configureMotor(transferRight, DcMotor.Direction.REVERSE);
        configureMotor(shooterLeft, DcMotor.Direction.FORWARD);
        configureMotor(shooterRight, DcMotor.Direction.REVERSE);

        stop();
    }

    private void configureMotor(DcMotor motor, DcMotor.Direction direction) {
        motor.setDirection(direction);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /** Call once per loop of your existing TeleOp, passing gamepad2. */
    public void update(Gamepad gamepad, Telemetry telemetry) {
        // Intake: hold LEFT TRIGGER for forward; hold X to reverse/clear a jam.
        double intakePower = gamepad.left_trigger * INTAKE_MAX_POWER;
        if (gamepad.x) {
intakePower = -INTAKE_REVERSE_POWER;
        }

// Transfer: hold A for forward; hold B for reverse/clear a jam.
double transferPower = 0.0;
        if (gamepad.a) {
transferPower = TRANSFER_MAX_POWER;
        } else if (gamepad.b) {
transferPower = -TRANSFER_REVERSE_POWER;
        }

// Shooter: hold RIGHT TRIGGER for variable forward power; hold Y to reverse.
double shooterPower = gamepad.right_trigger * SHOOTER_MAX_POWER;
        if (gamepad.y) {
shooterPower = -SHOOTER_REVERSE_POWER;
        }

                // Hold START on gamepad 2 to stop all three mechanisms immediately.
                if (gamepad.start) {
intakePower = 0.0;
transferPower = 0.0;
shooterPower = 0.0;
        }

setPairPower(intakeLeft, intakeRight, intakePower);
setPairPower(transferLeft, transferRight, transferPower);
setPairPower(shooterLeft, shooterRight, shooterPower);

        telemetry.addData("Intake power", "%.2f", intakePower);
        telemetry.addData("Transfer power", "%.2f", transferPower);
        telemetry.addData("Shooter power", "%.2f", shooterPower);
    }

private void setPairPower(DcMotor leftMotor, DcMotor rightMotor, double power) {
    // Clamp power so it is always between -1 and 1.
    double safePower = Math.max(-1.0, Math.min(1.0, power));
    leftMotor.setPower(safePower);
    rightMotor.setPower(safePower);
}

/** Call when the OpMode ends. */
public void stop() {
    intakeLeft.setPower(0.0);
    intakeRight.setPower(0.0);
    transferLeft.setPower(0.0);
    transferRight.setPower(0.0);
    shooterLeft.setPower(0.0);
    shooterRight.setPower(0.0);
}
}
