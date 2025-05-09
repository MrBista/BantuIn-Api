package com.exercie.exercies.model;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class UserRole {
    @Column("user_id")
    private Long userId;
    @Column("role_id")
    private Long roleId;
}
