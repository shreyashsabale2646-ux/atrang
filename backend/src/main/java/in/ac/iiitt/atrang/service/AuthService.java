package in.ac.iiitt.atrang.service;

import in.ac.iiitt.atrang.dto.AdminUserDto;
import in.ac.iiitt.atrang.dto.LoginRequest;
import in.ac.iiitt.atrang.dto.LoginResponse;
import in.ac.iiitt.atrang.entity.AdminUser;
import in.ac.iiitt.atrang.exception.ResourceNotFoundException;
import in.ac.iiitt.atrang.exception.UnauthorizedException;
import in.ac.iiitt.atrang.repository.AdminUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AdminUserRepository adminUserRepository;
    private final AuditService auditService;

    public AuthService(AuthenticationManager authenticationManager,
                       AdminUserRepository adminUserRepository,
                       AuditService auditService) {
        this.authenticationManager = authenticationManager;
        this.adminUserRepository = adminUserRepository;
        this.auditService = auditService;
    }

    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        Authentication authRequest = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authRequest);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

        AdminUser user = adminUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Admin user record not found"));

        auditService.log(null, user.getId(), "LOGIN", "Admin user logged in: " + user.getUsername(), user.getUsername(), httpRequest.getRemoteAddr());

        AdminUserDto dto = mapToDto(user);
        return new LoginResponse(true, "Authentication successful", dto);
    }

    public void logout(HttpServletRequest httpRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            adminUserRepository.findByUsername(auth.getName()).ifPresent(user -> {
                auditService.log(null, user.getId(), "LOGOUT", "Admin user logged out: " + user.getUsername(), user.getUsername(), httpRequest.getRemoteAddr());
            });
        }
        SecurityContextHolder.clearContext();
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @Transactional(readOnly = true)
    public AdminUserDto getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            throw new UnauthorizedException("No active session found");
        }
        AdminUser user = adminUserRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UnauthorizedException("Session user not found"));
        return mapToDto(user);
    }

    private AdminUserDto mapToDto(AdminUser u) {
        return new AdminUserDto(
                u.getId(),
                u.getUsername(),
                u.getFullName(),
                u.getEmail(),
                u.getRole(),
                u.getIsActive(),
                u.getCreatedAt()
        );
    }
}
