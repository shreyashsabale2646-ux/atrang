package in.ac.iiitt.atrang.dto;

public class LoginResponse {
    private boolean authenticated;
    private String message;
    private AdminUserDto user;

    public LoginResponse() {}

    public LoginResponse(boolean authenticated, String message, AdminUserDto user) {
        this.authenticated = authenticated;
        this.message = message;
        this.user = user;
    }

    public boolean isAuthenticated() { return authenticated; }
    public void setAuthenticated(boolean authenticated) { this.authenticated = authenticated; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public AdminUserDto getUser() { return user; }
    public void setUser(AdminUserDto user) { this.user = user; }
}
