package com.example.HMS_UI.controller;

import com.example.HMS_UI.constant.USER_ROLE;
import com.example.HMS_UI.dto.PatientDTO;
import com.example.HMS_UI.dto.PatientSaveDTO;
import com.example.HMS_UI.exception.ResourceNotFoundException;
import com.example.HMS_UI.model.Patient;
import com.example.HMS_UI.model.User;
import com.example.HMS_UI.repo.PatientRepo;
import com.example.HMS_UI.service.AppointmentService;
import com.example.HMS_UI.service.GrantAccess;
import com.example.HMS_UI.service.MedicalRecordService;
import com.example.HMS_UI.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Tag(name = "Patient APIs", description = "New Register, FindAll, Find-By-Id")
public class PatientController {
    @Autowired
    private UserService userService;
    @Autowired
    private GrantAccess grantAccess;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private MedicalRecordService medicalRecordService;
    //permit all
    //final

    @GetMapping("/register_patient")
    public String showForm(Model model) {
        model.addAttribute("patient", new PatientSaveDTO());
        return "patient_register_page";
    }

    @PostMapping("/patient_save")
    public String save(  @ModelAttribute("patient") @Valid  PatientSaveDTO patientSaveDTO){
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(5);
       User user= new User();
       user.setPassword(encoder.encode(patientSaveDTO.getPassword()));
       user.setName(patientSaveDTO.getName());
       user.setRole(patientSaveDTO.getRole());
       user.setEmail(patientSaveDTO.getEmail());

       Patient patient= new Patient();
       patient.setAge(patientSaveDTO.getAge());
       patient.setGender(patientSaveDTO.getGender());
       patient.setUser(user);
       user.setPatient(patient);
       userService.save(user);   //Behind the scene .save(user) is setting the Id of user and doctor
        //before saving to database so after saving if we do user.getId() it return he valid id in database
        return "login";
    }
    @PostMapping("/patient_save_by_admin")
    public String saveByAdmin(  @ModelAttribute("patient") @Valid  PatientSaveDTO patientSaveDTO){
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(5);
        User user= new User();
        user.setPassword(encoder.encode(patientSaveDTO.getPassword()));
        user.setName(patientSaveDTO.getName());
        user.setRole(patientSaveDTO.getRole());
        user.setEmail(patientSaveDTO.getEmail());

        Patient patient= new Patient();
        patient.setAge(patientSaveDTO.getAge());
        patient.setGender(patientSaveDTO.getGender());
        patient.setUser(user);
        user.setPatient(patient);
        userService.save(user);   //Behind the scene .save(user) is setting the Id of user and doctor
        //before saving to database so after saving if we do user.getId() it return he valid id in database
       return "redirect:/admin/dashboard";
    }

    @GetMapping("/patient/dashboard")
    public String patinetDashboard(Model model){
        model.addAttribute("patientId", new GrantAccess().getAuthenticationUser().getPatient().getId());
        Patient patient = new GrantAccess().getAuthenticationUser().getPatient();
        model.addAttribute("patientName", patient.getUser().getName());
        model.addAttribute("upcomingAppointments",appointmentService.findByPatientAndDateGreaterThanEqual(
                new GrantAccess().getAuthenticationUser().getPatient(), LocalDate.now()
        ).size() );
        model.addAttribute("patientId", patient.getId());
        int patientId= new GrantAccess().getAuthenticationUser().getPatient().getId();
        model.addAttribute("patientId", patientId);
        model.addAttribute("medicalRecordCount",medicalRecordService.getRecordCount(patientId));
        return "patient_dashboard";
    }

   //admin only
   @Operation(
           summary = "Get all patients",
           description = "Fetches a list of all registered patients. Accessible only by admin users.",
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

    @GetMapping("/patient/findall")
    public ResponseEntity<List<PatientDTO>> findAll(){
        List<PatientDTO> patientDTOList= userService.findAll().stream()
                .filter((user)->
                        user.getRole()== USER_ROLE.PATIENT && user.getPatient()!=null
                        ).map((user)-> {
                            return createPatientDTO(user);
                        }
                        ).collect(Collectors.toList());
        if(patientDTOList.size()==0){
            throw new ResourceNotFoundException("Patients List is Empty");
        }
        return ResponseEntity.ok(patientDTOList);
    }
//admin, doctor and that specific patient
@Operation(
        summary = "Find patient by ID",
        description = "Fetches patient information by patient ID. Accessible by admin,or the patient themselves.",
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

@GetMapping("/patient/{patientId}")
    public ResponseEntity<PatientDTO> findById(@PathVariable int patientId)throws AccessDeniedException{
        User user= grantAccess.getAuthenticationUser();
        boolean isAdmin = user.getRole() == USER_ROLE.ADMIN;
        boolean isPatient= user.getPatient()!=null;
        System.out.println(isAdmin);
        if (isAdmin || (isPatient && user.getPatient().getId()==patientId))
        {
            Patient patient = patientRepo.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Patient Not Found"));
            return ResponseEntity.ok(createPatientDTO(patient.getUser()));
        }
        throw new AccessDeniedException("Sorry, Accessed Denied");
    }

    private PatientDTO createPatientDTO(User user) {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName(user.getName());
        patientDTO.setEmail(user.getEmail());
        patientDTO.setRole(user.getRole().name());
        patientDTO.setUserId(user.getId());
        patientDTO.setPatientId(user.getPatient().getId());
        patientDTO.setAge(user.getPatient().getAge());
        patientDTO.setGender((user.getPatient().getGender()));
        patientDTO.setUserId(user.getId());
        return patientDTO;
    }
}
