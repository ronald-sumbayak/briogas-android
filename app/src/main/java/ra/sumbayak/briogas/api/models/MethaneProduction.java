package ra.sumbayak.briogas.api.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class MethaneProduction implements Serializable {
    
    @SerializedName ("value")
    public int value;
    
    @SerializedName ("date")
    public Date date;
}
