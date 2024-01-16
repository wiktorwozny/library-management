package pl.edu.agh.to.library.model.role;

import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addRole(Role newRole) {
        roleRepository.save(newRole);
    }

    public Role getByName(String name) {
        return roleRepository.findByRoleName(name);
    }
}
