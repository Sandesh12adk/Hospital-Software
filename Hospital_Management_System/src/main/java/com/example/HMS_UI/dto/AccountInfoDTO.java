package com.example.HMS_UI.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AccountInfoDTO {
    private int roleId;
    protected String userName;
    protected List<String> roles;
    protected int userId;
}
