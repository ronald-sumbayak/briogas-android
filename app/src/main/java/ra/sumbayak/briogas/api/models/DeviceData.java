package ra.sumbayak.briogas.api.models;

import com.google.gson.annotations.SerializedName;

public class DeviceData {
    
    @SerializedName ("methane") public int methane;
    @SerializedName ("oxygen") public int oxygen;
    @SerializedName ("katup") public boolean katup;
}
