package com.example.Hospital_Management_System.controller;

import com.example.Hospital_Management_System.constant.USER_ROLE;
import com.example.Hospital_Management_System.dto.DoctorDTO;
import com.example.Hospital_Management_System.dto.DoctorSaveDTO;
import com.example.Hospital_Management_System.exception.ResourceNotFoundException;
import com.example.Hospital_Management_System.model.Appointment;
import com.example.Hospital_Management_System.model.Department;
import com.example.Hospital_Management_System.model.Doctor;
import com.example.Hospital_Management_System.model.User;
import com.example.Hospital_Management_System.repo.DoctorRepo;
import com.example.Hospital_Management_System.service.AppointmentService;
import com.example.Hospital_Management_System.service.DepartmentService;
import com.example.Hospital_Management_System.service.GrantAccess;
import com.example.Hospital_Management_System.service.UserService;
import com.example.Hospital_Management_System.service.securityservice.JWTService;
import com.example.Hospital_Management_System.service.securityservice.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/doctor/")
@CrossOrigin(origins = "*") // Only for local development!
@Tag(
        name = "Doctor APIs",
        description = "Endpoints for doctor registration (public), retrieving doctors (authenticated users), " +
                "and managing appointment statuses (doctor only)."
)

public class DoctorController {
    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private GrantAccess grantAccess;
    @Autowired
    private DoctorRepo doctorRepo;

    //permitall
    @Operation(
            summary = "Register new doctor",
            description = "Registers a new doctor, this api is publicly accessable wihout any authentication needed.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Operation"),
                    @ApiResponse(responseCode = "400", description = "Bad Request - Invalid ID or parameters"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to access this resource"),
                    @ApiResponse(responseCode = "404", description = "Not Found - No resource found with given ID"),
                    @ApiResponse(responseCode = "409", description = "Conflict - Resource already exists or violates constraints"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")}
    )

    @PreAuthorize("permitAll()")
    @PostMapping("register")
    public ResponseEntity<DoctorDTO> save(@Valid @RequestBody DoctorSaveDTO doctorSaveDTO){
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(5);
        Department department= departmentService.findById(doctorSaveDTO.getDepartmentId())
                .orElseThrow(()-> new ResourceNotFoundException("Department Not Found"));
        User user= new User();
        user.setName(doctorSaveDTO.getName());
        user.setPassword(encoder.encode(doctorSaveDTO.getPassword()));
        user.setRole(doctorSaveDTO.getRole());
        user.setEmail(doctorSaveDTO.getEmail());
        Doctor doctor= new Doctor();
        doctor.setDepartment(department);
        doctor.setSpecialization(doctorSaveDTO.getSpecialization());
        doctor.setUser(user);
        user.setDoctor(doctor);


        userService.save(user);
        DoctorDTO doctorDTO= createDoctorDTO(user);
        return ResponseEntity.ok(doctorDTO);
    }

    //permitall-authenticated
    @Operation(
            summary = "Get all doctors",
            description = "Returns a list of all registered doctors. Accessible by authenticated users.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Operation"),
                    @ApiResponse(responseCode = "400", description = "Bad Request - Invalid ID or parameters"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to access this resource"),
                    @ApiResponse(responseCode = "404", description = "Not Found - No resource found with given ID"),
                    @ApiResponse(responseCode = "409", description = "Conflict - Resource already exists or violates constraints"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")}
    )
    @GetMapping("findall")
    public ResponseEntity<List<DoctorDTO>> findAll() {
        List<DoctorDTO> doctorDTOList = userService.findAll().stream().filter((user) ->
                        user.getRole() == USER_ROLE.DOCTOR && user.getDoctor() != null)  //user.getDoctor() != null

                .map((user) -> {
                            return createDoctorDTO(user);
                        }
                ).collect(Collectors.toList());
        if(doctorDTOList.size()==0){
            throw new ResourceNotFoundException("Doctors List is Empty");
        }
        return ResponseEntity.ok(doctorDTOList);
    }

    //permitall-authenticated
    @Operation(
            summary = "Get doctor by ID",
            description = "Fetch doctor details by doctor ID. Accessible by authenticated users.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Operation"),
                    @ApiResponse(responseCode = "400", description = "Bad Request - Invalid ID or parameters"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to access this resource"),
                    @ApiResponse(responseCode = "404", description = "Not Found - No resource found with given ID"),
                    @ApiResponse(responseCode = "409", description = "Conflict - Resource already exists or violates constraints"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")}
    )
    @GetMapping("{doctorId}")
    public ResponseEntity<DoctorDTO> findById(@PathVariable int doctorId) {
        Doctor doctor= doctorRepo.findById(doctorId).orElseThrow(() ->   // map return optional but .orElseThrow return value or the exception
                new ResourceNotFoundException("Dotctor Not Found with id" + doctorId)
        );
        DoctorDTO doctorDTOOptional= createDoctorDTO(doctor.getUser());
        return ResponseEntity.ok(doctorDTOOptional);
    }
    //Doctor only
    @Operation(
            summary = "Mark appointment as scheduled",
            description = "Marks an appointment as scheduled. Accessible only by the doctor assigned to that appointment.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Operation"),
                    @ApiResponse(responseCode = "400", description = "Bad Request - Invalid ID or parameters"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to access this resource"),
                    @ApiResponse(responseCode = "404", description = "Not Found - No resource found with given ID"),
                    @ApiResponse(responseCode = "409", description = "Conflict - Resource already exists or violates constraints"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")}
    )
    @PutMapping("make_as_schelduded/{appointmentId}")
    public ResponseEntity<String> markAppointmentAsSchelduded(@PathVariable int appointmentId)throws AccessDeniedException {
        User user = grantAccess.getAuthenticationUser();
            Appointment appointment = appointmentService.findById(appointmentId).orElseThrow(() ->
                    new ResourceNotFoundException("Cannot find the appoint with provided id")
            );
                if(appointment.getDoctor().getId()== user.getDoctor().getId()){
                appointmentService.updateAppointmentStatusToSchelduded(appointmentId);
                    return ResponseEntity.ok("Appointment marked as scheduled.");
        }
                throw new AccessDeniedException("Sorry, Access Denied");
    }
//Doctor only
@Operation(
        summary = "Mark appointment as canceled",
        description = "Marks an appointment as canceled. Accessible only by the doctor assigned to that appointment.",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
                @ApiResponse(responseCode = "200", description = "Successful Operation"),
                @ApiResponse(responseCode = "400", description = "Bad Request - Invalid ID or parameters"),
                @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to access this resource"),
                @ApiResponse(responseCode = "404", description = "Not Found - No resource found with given ID"),
                @ApiResponse(responseCode = "409", description = "Conflict - Resource already exists or violates constraints"),
                @ApiResponse(responseCode = "500", description = "Internal Server Error")}

)

@PutMapping("make_as_canceled/{appointmentId}")
    public ResponseEntity<String> markAppointmentAsCancled(@PathVariable int appointmentId)throws AccessDeniedException{
        User user = grantAccess.getAuthenticationUser();
        Appointment appointment= appointmentService.findById(appointmentId).orElseThrow(()->
                new ResourceNotFoundException("Cannot find the appointment with provided id")
        );
      if(appointment.getDoctor().getId()==user.getDoctor().getId()) {
            appointmentService.updateAppointmentStatusToCancled(appointmentId);
            return ResponseEntity.ok("Appointment Marked as Canceled");
        }
        throw new AccessDeniedException("Sorry, Access Denied");
    }
        private DoctorDTO createDoctorDTO(User user) {
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setUserId(user.getId());
            doctorDTO.setDoctorId(user.getDoctor().getId());
            doctorDTO.setName(user.getName());
            doctorDTO.setEmail(user.getEmail());
            doctorDTO.setRole(user.getRole().name());
            if(user.getDoctor().getSpecialization()!=null){
                doctorDTO.setSpecialization(user.getDoctor().getSpecialization());
            }
            if(user.getDoctor().getDepartment()!=null){
                doctorDTO.setDepartmentId(user.getDoctor().getDepartment().getId());
                doctorDTO.setDeparementName(user.getDoctor().getDepartment().getName());
            }
           if(user.getDoctor().getDepartment().getDescription()!=null){
               doctorDTO.setDescription(user.getDoctor().getDepartment().getDescription());
           }

            return doctorDTO;
        }

}
