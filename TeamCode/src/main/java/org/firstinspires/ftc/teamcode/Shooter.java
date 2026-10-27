package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shooter {
    private DcMotor shooterMotor;

    public Shooter(HardwareMap hardwareMap) {
        shooterMotor = hardwareMap.get(DcMotor.class, "shooter_motor");
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // FLOAT behavior safely protects high-speed launcher gearboxes when spinning down
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    /**
     * Directly controls shooter wheel velocity.
     * @param power Target motor power (-1.0 to 1.0)
     */
    public void runShooter(double power) {
        shooterMotor.setPower(power);
    }
}

