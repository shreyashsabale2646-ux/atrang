package in.ac.iiitt.atrang.config;

import in.ac.iiitt.atrang.entity.AdminUser;
import in.ac.iiitt.atrang.repository.AdminUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${atrang.admin.default-username:admin}")
    private String defaultUsername;

    @Value("${atrang.admin.default-password:admin123}")
    private String defaultPassword;

    public DataInitializer(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!adminUserRepository.existsByUsername(defaultUsername)) {
            log.info("No default admin user found. Creating initial admin: {}", defaultUsername);
            AdminUser admin = new AdminUser(
                    defaultUsername,
                    passwordEncoder.encode(defaultPassword),
                    "Chief Technical Administrator",
                    "admin@atrang.iiitt.ac.in",
                    "ROLE_SUPERADMIN"
            );
            adminUserRepository.save(admin);
            log.info("Default administrator created successfully.");
        } else {
            // Update admin password hash to match current configured password
            adminUserRepository.findByUsername(defaultUsername).ifPresent(admin -> {
                admin.setPasswordHash(passwordEncoder.encode(defaultPassword));
                adminUserRepository.save(admin);
                log.info("Verified administrative credentials for '{}'", defaultUsername);
            });
        }
    }
}
