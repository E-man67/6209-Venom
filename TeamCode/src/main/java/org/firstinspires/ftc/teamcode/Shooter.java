


package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shooter {
    private DcMotorEx shooterLeft;
    private DcMotorEx shooterRight;
    private double power = 0.0;

    public Shooter(HardwareMap hardwareMap) {
        shooterLeft = hardwareMap.get(DcMotorEx.class, "shooter_left");
        shooterRight = hardwareMap.get(DcMotorEx.class, "shooter_right");

        shooterLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Reverse one side because the motors face each other physically in the CAD layout
        shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterRight.setDirection(DcMotorSimple.Direction.FORWARD);

        // FLOAT behavior safely protects high-speed launcher gearboxes when spinning down
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    /**
     * Drives both shooter flywheel motors simultaneously.
     * param power Target motor power (-1.0 to 1.0)
     */
    public void runShooter(double v) {
        shooterLeft.setVelocity(v*1.4);
        shooterRight.setVelocity(v);
    }

    public boolean isRunning() {
        return power != 0.0;
    }

    /** Flywheel speed in encoder ticks per second (needs the motor encoder cable plugged in). */
    public double getLeftVelocity() {
        return shooterLeft.getVelocity();
    }

    public double getRightVelocity() {
        return shooterRight.getVelocity();
    }
}


