package com.api.parkingcontrol.controllers.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpotResultDto {

    private UUID id;

    private String parkingSpotNumber;

    private String licensePlateCar;

    private String brandCar;

    private String modelCar;

    private String colorCar;

    private LocalDateTime criationDate;

    private LocalDateTime updateDate;

    private String responsibleName;

    private String apartment;

    private String block;
}
