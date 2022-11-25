package com.example.uberapp_tim6.models;


import com.example.uberapp_tim6.models.enumerations.VehicleName;

public class VehicleType {
    private int id;
    private VehicleName vehcleName;
    private float priceByKm;

    public VehicleType(int id, VehicleName vehcleName, float priceByKm) {
        this.id = id;
        this.vehcleName = vehcleName;
        this.priceByKm = priceByKm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VehicleName getVehcleName() {
        return vehcleName;
    }

    public void setVehcleName(VehicleName vehcleName) {
        this.vehcleName = vehcleName;
    }

    public float getPriceByKm() {
        return priceByKm;
    }

    public void setPriceByKm(float priceByKm) {
        this.priceByKm = priceByKm;
    }
}



