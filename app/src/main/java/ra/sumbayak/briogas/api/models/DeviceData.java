package ra.sumbayak.briogas.api.models;

import com.google.gson.annotations.SerializedName;

public class DeviceData {
    
    @SerializedName ("id") public int id;
    @SerializedName ("owner") public String owner;
    
    @SerializedName ("methane") public int methane;
    @SerializedName ("pressure") public int pressure;
    @SerializedName ("content") public int content;
    @SerializedName ("temperature") public int temperature;
    @SerializedName ("power") public boolean power;
    
    @SerializedName ("today_methane_production")
    public int methane_production;
}
