package com.api.parkingcontrol.controllers.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
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
