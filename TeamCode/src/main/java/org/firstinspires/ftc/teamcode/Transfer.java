package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Transfer {
    private DcMotor intakeMotor;
    private DcMotor conveyorMotor;

    public Transfer(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake_motor");
        conveyorMotor = hardwareMap.get(DcMotor.class, "conveyor_motor");

        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        conveyorMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        conveyorMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Controls intake and internal conveyor states simultaneously.
     * @param intakePower   Power applied to the intake spinner
     * @param conveyorPower Power applied to the internal conveyor channel
     */
    public void runTransfer(double intakePower, double conveyorPower) {
        intakeMotor.setPower(intakePower);
        conveyorMotor.setPower(conveyorPower);
    }
}

