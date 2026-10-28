package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shooter {
    private DcMotor shooterLeft;
    private DcMotor shooterRight;

    public Shooter(HardwareMap hardwareMap) {
        shooterLeft = hardwareMap.get(DcMotor.class, "shooter_left");
        shooterRight = hardwareMap.get(DcMotor.class, "shooter_right");

        shooterLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Reverse one side because the motors face each other physically in the CAD layout
        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // FLOAT behavior safely protects high-speed launcher gearboxes when spinning down
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    /**
     * Drives both shooter flywheel motors simultaneously.
     * @param power Target motor power (-1.0 to 1.0)
     */
    public void runShooter(double power) {
        shooterLeft.setPower(power);
        shooterRight.setPower(power);
    }
}


