package kr.doridos.dosticket.domain.user;

public enum UserType {
    TICKET_MANAGER(Authority.USER),
    USER(Authority.TICKET_MANAGER);

    private final String authority;

    UserType(final String authority) {
        this.authority = authority;
    }
    public String getAuthority() {
        return authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String TICKET_MANAGER = "ROLE_TICKET_MANAGER";
    }
}
