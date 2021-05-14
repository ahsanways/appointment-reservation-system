package edu.miu.cs.cs544.domain;

import org.springframework.security.core.GrantedAuthority;

public enum RoleType implements GrantedAuthority{
	ADMIN("ROLE_ADMIN"),
    COUNSELOR("ROLE_COUNSELOR"),
    CUSTOMER("ROLE_CUSTOMER");

    private final String authority;

    RoleType(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
