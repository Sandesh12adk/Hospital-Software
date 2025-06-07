package com.example.HMS_UI.dto;

import com.example.HMS_UI.constant.APPOINTMENT_STATUS;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTOA {
        private int id;
        private String doctorName;
        private LocalDate date;
        private LocalTime time;
        private String reason;
        private String status;
        private String departmentName;
        private String paymentStatus;
}
