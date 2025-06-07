// AppointmentMapper.java
package com.example.HMS_UI.mapper;
import com.example.HMS_UI.dto.AppointmentDTO;
import com.example.HMS_UI.dto.AppointmentDTOA;
import com.example.HMS_UI.model.Appointment;
import org.springframework.stereotype.Component;
@Component
public class AppointmentMapper {
    public AppointmentDTOA toDTO(Appointment appointment) {
        AppointmentDTOA dto = new AppointmentDTOA();
        dto.setId(appointment.getId());

        // Safe handling of doctor information
        if (appointment.getDoctor() != null) {
            dto.setDoctorName(appointment.getDoctor().getUser() != null ?
                    appointment.getDoctor().getUser().getName() : "Unknown Doctor");
        }

        dto.setDate(appointment.getDate());
        dto.setTime(appointment.getTime());
        dto.setReason(appointment.getReason());
        dto.setStatus(appointment.getStatus().name());
        dto.setDepartmentName(appointment.getDoctor().getDepartment().getName());
        dto.setPaymentStatus(appointment.getPaymentStatus());
        return dto;
    }
}
