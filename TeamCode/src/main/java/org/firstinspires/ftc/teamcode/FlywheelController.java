package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

public class FlywheelController {
    // PIDF Coefficients (To be tuned)
    private double kP, kI, kD, kF;

    private double targetVelocity = 0; // In encoder ticks per second
    private double lastError = 0;
    private double integralSum = 0;

    private ElapsedTime timer = new ElapsedTime();

    public FlywheelController(double kP, double kI, double kD, double kF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        timer.reset();
    }

    public void setTargetVelocity(double target) {
        this.targetVelocity = target;
    }

    public double calculatePower(double currentVelocity) {
        double dt = timer.seconds();
        // Avoid potential division by zero on the very first cycle
        if (dt == 0) dt = 0.001;
        timer.reset();

        double error = targetVelocity - currentVelocity;

        // Proportional term
        double pTerm = kP * error;

        // Integral term (with basic anti-windup clamping)
        integralSum += error * dt;
        if (targetVelocity == 0) integralSum = 0; // Clear error if motor is stopped
        double iTerm = kI * integralSum;

        // Derivative term
        double dTerm = kD * ((error - lastError) / dt);
        lastError = error;

        // Feedforward term (crucial for maintaining speed on high-velocity launchers)
        double fTerm = kF * targetVelocity;

        // Combine all terms into a motor command power (-1.0 to 1.0)
        double outputPower = pTerm + iTerm + dTerm + fTerm;

        // Clamp the final value to safe motor power limits
        if (outputPower > 1.0) return 1.0;
        if (outputPower < -1.0) return -1.0;
        return outputPower;
    }

    public void updateGains(double p, double i, double d, double f) {
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kF = f;
    }
}

