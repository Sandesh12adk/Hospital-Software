package com.example.HMS_UI.service;


import com.example.HMS_UI.constant.USER_ROLE;
import com.example.HMS_UI.dto.DoctorDetail;
import com.example.HMS_UI.dto.DoctorDashboardDTO;
import com.example.HMS_UI.dto.PatientDashboardDTO;
import com.example.HMS_UI.exception.ResourceNotFoundException;
import com.example.HMS_UI.repo.UserRepo;
import com.example.HMS_UI.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService{
    @Autowired
    private UserRepo userRepo;
    public User save(User user){
        return userRepo.save(user);
    }
    public List<User> findAll(){
        return userRepo.findAll();
    }
    public Optional<User> findById(int id){
        return userRepo.findById(id);
    }
    public Optional<User> findByUserName(String userName){ return userRepo.findByName(userName);}

    public Optional<User> findByEmail(String email) { return userRepo.findByEmail(email); }
    public int doctorCount(USER_ROLE role){return userRepo.findByRole(USER_ROLE.DOCTOR).size();}
    public int patientCount(USER_ROLE role){return userRepo.findByRole(USER_ROLE.PATIENT).size();}
    public List<PatientDashboardDTO> patientDashboardDTOList(){
       return  userRepo.findAll().stream()
                .filter(user -> {return user.getRole()==USER_ROLE.PATIENT;})
               .filter(user-> {return user.getPatient()!=null;})
                .filter(user -> {return user.getPatient().getAppointments()!=null;})
                .filter(user -> {return user.getPatient().getAge()!=0;})
                .filter(user -> {return user.getPatient().getGender()!=null;})
                .map(patientUser->{
                    PatientDashboardDTO patientDashboardDTO= new PatientDashboardDTO();
                    patientDashboardDTO.setAge(patientUser.getPatient().getAge());
                    patientDashboardDTO.setPatientId(patientUser.getPatient().getId());
                    patientDashboardDTO.setGender(patientUser.getPatient().getGender());
                    patientDashboardDTO.setName(patientUser.getName());
                    patientDashboardDTO.setEmail(patientUser.getEmail());
                    patientDashboardDTO.setAppointmentCount(patientUser.getPatient().getAppointments().size());
                    return patientDashboardDTO;
                }).collect(Collectors.toList());
    }
    public List<DoctorDashboardDTO> DoctorDashboardDTOList(){
        return  userRepo.findAll().stream()
                .filter(user -> {return user.getRole()==USER_ROLE.DOCTOR;})
                .filter(user-> {return user.getDoctor()!=null;})
                .filter(user -> {return user.getDoctor().getDepartment()!=null;})
                .filter(user -> {return user.getDoctor().getSpecialization()!=null;})
                .map(doctorUser->{
                    DoctorDashboardDTO doctorDashboardDTO= new DoctorDashboardDTO();
                    doctorDashboardDTO.setDoctorId(doctorUser.getDoctor().getId());
                    doctorDashboardDTO.setSpecialization(doctorUser.getDoctor().getSpecialization());
                    doctorDashboardDTO.setEmail(doctorUser.getEmail());
                    doctorDashboardDTO.setDepartmentName(doctorUser.getDoctor().getDepartment().getName());
                    doctorDashboardDTO.setAppointmentCount(doctorUser.getDoctor().getAppointments().size());
                    doctorDashboardDTO.setDepartmentId(doctorUser.getDoctor().getDepartment().getId());
                    doctorDashboardDTO.setName(doctorUser.getName());
                    return doctorDashboardDTO;
                }).toList();
    }
    public DoctorDetail getDoctorDetail(int doctorId){
        System.out.println(doctorId);
        User doctorUser1 = userRepo.findAll()
                .stream()
                .filter(user -> {return user.getDoctor()!=null;})
                .filter(user -> {return user.getDoctor().getId()==doctorId;})
                .toList().get(0);

        List<User> singleDoctorUserList= new ArrayList<>();
        singleDoctorUserList.add(doctorUser1);
       return singleDoctorUserList
                .stream()
                .filter(user -> {return user.getRole()==USER_ROLE.DOCTOR;})
                .filter(user-> {return user.getDoctor()!=null;})
                .filter(user -> {return user.getDoctor().getDepartment()!=null;})
                .filter(user -> {return user.getDoctor().getSpecialization()!=null;})
                .map(doctorUser->{
                    DoctorDetail doctorDetail= new DoctorDetail();
                    doctorDetail.setDoctorId(doctorUser.getDoctor().getId());
                    doctorDetail.setUserId(doctorUser.getId());
                    doctorDetail.setRole(doctorUser.getRole().name());
                    doctorDetail.setDescription("Doctor in Harmony Hospital");
                    doctorDetail.setName(doctorUser.getName());
                    doctorDetail.setEmail(doctorUser.getEmail());
                    doctorDetail.setDepartmentId(doctorUser.getDoctor().getDepartment().getId());
                    doctorDetail.setDepartmentName(doctorUser.getDoctor().getDepartment().getName());
                    doctorDetail.setSpecialization(doctorUser.getDoctor().getSpecialization());
                    return doctorDetail;
                }).toList().get(0);
                }
}