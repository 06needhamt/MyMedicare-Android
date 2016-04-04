package com.needham.thomas.medicare.root.Classes;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Thomas Needham on 02/04/2016.
 * This class represents a single record within the app
 */
public class Record implements Serializable, IAppConstants {
    private User owner;
    private EnumRiskLevel riskLevel;
    private EnumTemperatureUnit temperatureUnit;
    private double temperature;
    private int bloodPressureLow;
    private int bloodPressureHigh;
    private int heartRate;
    private Date timeTaken;

    public Record(User owner, EnumRiskLevel riskLevel, EnumTemperatureUnit temperatureUnit,
                  double temperature, int bloodPressureLow, int bloodPressureHigh,
                  int heartRate, Date timeTaken) {
        this.owner = owner;
        this.riskLevel = riskLevel;
        this.temperatureUnit = temperatureUnit;
        this.temperature = temperature;
        this.bloodPressureLow = bloodPressureLow;
        this.bloodPressureHigh = bloodPressureHigh;
        this.heartRate = heartRate;
        this.timeTaken = timeTaken;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public EnumRiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(EnumRiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public EnumTemperatureUnit getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(EnumTemperatureUnit temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getBloodPressureLow() {
        return bloodPressureLow;
    }

    public void setBloodPressureLow(int bloodPressureLow) {
        this.bloodPressureLow = bloodPressureLow;
    }

    public int getBloodPressureHigh() {
        return bloodPressureHigh;
    }

    public void setBloodPressureHigh(int bloodPressureHigh) {
        this.bloodPressureHigh = bloodPressureHigh;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public Date getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Date timeTaken) {
        this.timeTaken = timeTaken;
    }

    /**
     * Returns a string containing a concise, human-readable description of this
     * object. Subclasses are encouraged to override this method and provide an
     * implementation that takes into account the object's type and data. The
     * default implementation is equivalent to the following expression:
     * <pre>
     *   getClass().getName() + '@' + Integer.toHexString(hashCode())</pre>
     * <p>See <a href="{@docRoot}reference/java/lang/Object.html#writing_toString">Writing a useful
     * {@code toString} method</a>
     * if you intend implementing your own {@code toString} method.
     *
     * @return a printable representation of this object.
     */
    @Override
    public String toString() {
        String unit;
        if(temperatureUnit.name().equals("CELCIUS"))
            unit = "\u2103";
        else if(temperatureUnit.name().equals("FARENHEIT"))
            unit = "\u2109";
        else
            unit = "UNKNOWN";
        return "Temperature " + temperature + " " +  unit + "\n"
                + "Blood Pressure Low " + bloodPressureLow + "\n"
                + "Blood Pressure High " + bloodPressureHigh + "\n"
                + "Heart Rate " + heartRate + " bpm " + "\n"
                + "Date " + timeTaken.toString() + "\n";
    }
}
