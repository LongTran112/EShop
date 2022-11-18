package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {

//    @Autowired
//    private TestEntityManager testEntityManager;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin = new Role("Admin","manages everything");
        Role savedRole = roleRepository.save(roleAdmin);

        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRoles(){
        Role roleSalesperson = new Role("Salesperson", "manage product price, customers" +
                ", shipping, orders and sales report");
        Role roleEditor = new Role("Editor", "manages categories, brands," +
                "products, articles and menus");
        Role roleShipper = new Role("Shipper", "views products, view orders " +
                "and update order status");
        Role roleAssistant = new Role("Assistant", " manages questions and reviews");

        List<Role> savedRoles = (List<Role>) roleRepository.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));

        assertThat(savedRoles.isEmpty()).isFalse();

    }



}
