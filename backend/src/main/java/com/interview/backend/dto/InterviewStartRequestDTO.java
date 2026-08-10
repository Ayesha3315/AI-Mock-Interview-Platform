package com.interview.backend.dto;

public class InterviewStartRequestDTO {
    private Integer roleId;

    public InterviewStartRequestDTO() {
    }

    public InterviewStartRequestDTO(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}