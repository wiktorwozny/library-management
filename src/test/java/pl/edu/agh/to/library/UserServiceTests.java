package pl.edu.agh.to.library;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pl.edu.agh.to.library.model.notification.Notification;
import pl.edu.agh.to.library.model.notification.NotificationService;
import pl.edu.agh.to.library.model.role.Role;
import pl.edu.agh.to.library.model.role.RoleRepository;
import pl.edu.agh.to.library.model.user.User;
import pl.edu.agh.to.library.model.user.UserRepository;
import pl.edu.agh.to.library.model.user.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceTests {

    private final UserRepository userRepository;

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final NotificationService notificationService;

    @Autowired
    public UserServiceTests(UserRepository userRepository, UserService userService, RoleRepository roleRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.notificationService = notificationService;
    }

    @Test
    @Transactional
    public void addNewUserTest() {

        // given
        Role adminRole = new Role("admin");
        roleRepository.save(adminRole);

        Notification notification = new Notification();
        notificationService.save(notification);
        User newUser = new User("Jan", "Kowalski", "jan@asd.pl", adminRole, new Notification());
        userService.saveUser(newUser);

        int userId = newUser.getId();

        // then
        assertEquals(userRepository.getReferenceById(userId), newUser);
    }
}
