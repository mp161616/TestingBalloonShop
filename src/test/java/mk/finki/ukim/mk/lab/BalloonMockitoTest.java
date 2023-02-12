package mk.finki.ukim.mk.lab;

import mk.finki.ukim.mk.lab.model.*;
import mk.finki.ukim.mk.lab.model.enumerations.Role;
import mk.finki.ukim.mk.lab.model.enumerations.ShoppingCartStatus;
import mk.finki.ukim.mk.lab.model.exceptions.InvalidUsernameOrPasswordException;
import mk.finki.ukim.mk.lab.model.exceptions.PasswordsDoNotMatchException;
import mk.finki.ukim.mk.lab.model.exceptions.UsernameExistsException;
import mk.finki.ukim.mk.lab.repository.jpa.*;
import mk.finki.ukim.mk.lab.service.BalloonService;
import mk.finki.ukim.mk.lab.service.OrderService;
import mk.finki.ukim.mk.lab.service.ShoppingCartService;
import mk.finki.ukim.mk.lab.service.UserService;
import mk.finki.ukim.mk.lab.service.impl.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class BalloonMockitoTest {


        @Mock
        private BalloonRepository balloonRepository;

        @InjectMocks
        private BalloonServiceImpl balloonService;

         @Mock
         private ManufacturerRepository manufacturerRepository;

        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private UserServiceImpl userService;



        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private ShoppingCartRepository scRepository;

        @InjectMocks
        private ShoppingCartServiceImpl serviceSC;


        @Mock
        User user;

        @Mock
        OrderRepository orderRepository;

        @InjectMocks
        OrderServiceImpl orderService;


        @Before
        public void init() {
            MockitoAnnotations.initMocks(this);
            user = new User("username", "password", "name", "surname", LocalDate.of(2001, 1, 1), Role.ROLE_ADMIN);

            this.userService = Mockito.spy(new UserServiceImpl(this.userRepository, this.passwordEncoder));
            this.serviceSC = Mockito.spy(new ShoppingCartServiceImpl(this.scRepository, this.userRepository, this.balloonService));
            this.balloonService = Mockito.spy(new BalloonServiceImpl(this.balloonRepository, this.manufacturerRepository));
            this.orderService = Mockito.spy(new OrderServiceImpl(this.orderRepository, this.serviceSC, this.userRepository));
        }


        @Test
        public void findUser(){

            User ana = new User("ana", "ana123", "ana", "aneva", LocalDate.of(1993, 12, 12), Role.ROLE_USER);
            Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(ana));
            UserDetails userDetails = userService.loadUserByUsername("ana");
            System.out.println(userDetails);
            Assertions.assertNotNull(userDetails);


        }


        @Test
        public void createEmptyCart(){
            User ana = new User("ana", "ana123", "ana", "aneva", LocalDate.of(1993, 12, 12), Role.ROLE_USER);
            Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(ana));

            ShoppingCart sc = new ShoppingCart(ana);
            Mockito.when(this.scRepository.findByUserAndStatus(Mockito.any(), Mockito.any())).thenReturn(Optional.of(sc));
            ShoppingCart scAna =  serviceSC.getActiveShoppingCart("ana");
            System.out.println(scAna.getUser());
            Assertions.assertEquals(0, scAna.getBalloons().size());
        }

        @Test
        public void addProductToCart() {
            List<Balloon> balloons = new ArrayList<>();
            balloons.add(new Balloon("balon1", "opis", new Manufacturer("ime", "MK", "adresa")));

            User ana = new User("ana", "ana123", "ana", "aneva", LocalDate.of(1993, 12, 12), Role.ROLE_USER);
            Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(ana));

            ShoppingCart sc = new ShoppingCart(ana);
            Mockito.when(this.scRepository.findByUserAndStatus(Mockito.any(), Mockito.any())).thenReturn(Optional.of(sc));
            ShoppingCart scAna =  serviceSC.getActiveShoppingCart("ana");
            scAna.setBalloons(balloons);

            Assertions.assertEquals(1, scAna.getBalloons().size());
       }

    @Test
    public void deleteProductFromCart() {
        List<Balloon> balloons = new ArrayList<>();
        Balloon balloon = new Balloon("balon1", "opis", new Manufacturer("ime", "MK", "adresa"));
        balloons.add(balloon);

        User ana = new User("ana", "ana123", "ana", "aneva", LocalDate.of(1993, 12, 12), Role.ROLE_USER);
        Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(ana));

        ShoppingCart sc = new ShoppingCart(ana);
        Mockito.when(this.scRepository.findByUserAndStatus(Mockito.any(), Mockito.any())).thenReturn(Optional.of(sc));
        ShoppingCart scAna =  serviceSC.getActiveShoppingCart("ana");
        scAna.setBalloons(balloons);

        if(scAna.getBalloons().contains(balloon)){
            scAna.getBalloons().remove(balloon);
        }

        Assertions.assertEquals(0, scAna.getBalloons().size());
    }

    @Test
    public void createNewBalloonAsAdmin(){
        Balloon balloon;
        List<Balloon> balloons = null;
        if(user.getRole().equals(Role.ROLE_ADMIN)){
            balloon = new Balloon("balon1", "opis", new Manufacturer("ime", "MK", "adresa"));
            balloons = balloonService.searchByNameOrDescription("balon1");
        }
        Assertions.assertNotNull(balloons);
    }


    @Test
    public void createOrder(){
        User ana = new User("ana", "ana123", "ana", "aneva", LocalDate.of(1993, 12, 12), Role.ROLE_USER);
        Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(ana));

        List<Balloon> balloons = new ArrayList<>();
        Balloon balloon = new Balloon("balon1", "opis", new Manufacturer("ime", "MK", "adresa"));
        balloons.add(balloon);

        ShoppingCart sc = new ShoppingCart(ana);
        Mockito.when(this.scRepository.findByUserAndStatus(Mockito.any(), Mockito.any())).thenReturn(Optional.of(sc));
        ShoppingCart scAna =  serviceSC.getActiveShoppingCart("ana");


        List<Order> orderList = new ArrayList<>();
        Order order = new Order(ana, "adresa", LocalDateTime.now());
        orderList.add(order);
        Mockito.when(this.orderRepository.findAllByUser(Mockito.any())).thenReturn(orderList);
        List<Order> order1 = orderService.getPlacedOrdersForUser("ana");

        Assertions.assertEquals(1, order1.size());


    }



        @Test
        public void testEmptyUsername() {
            String username = "";
            Assert.assertThrows("InvalidArgumentException expected",
                    InvalidUsernameOrPasswordException.class,
                    () -> this.userService.register(username, "password", "password", "name", "surename",LocalDate.of(2001, 1, 1), Role.ROLE_USER));
            Mockito.verify(this.userService).register(username, "password", "password", "name", "surename", LocalDate.of(2001, 1, 1), Role.ROLE_USER);
        }
        @Test
        public void testNullUsername() {
            Assert.assertThrows("InvalidArgumentException expected",
                    InvalidUsernameOrPasswordException.class,
                    () -> this.userService.register(null, "password", "password", "name", "surename",LocalDate.of(2001, 1, 1), Role.ROLE_USER));
            Mockito.verify(this.userService).register(null, "password", "password", "name", "surename",LocalDate.of(2001, 1, 1), Role.ROLE_USER);
        }
        @Test
        public void testEmptyPassword() {
            String username = "username";
            String password = "";
            Assert.assertThrows("InvalidArgumentException expected",
                    InvalidUsernameOrPasswordException.class,
                    () -> this.userService.register(username, password, "password", "name", "surename",LocalDate.of(2001, 1, 1),  Role.ROLE_USER));
            Mockito.verify(this.userService).register(username, password, "password", "name", "surename",LocalDate.of(2001, 1, 1), Role.ROLE_USER);
        }

        @Test
        public void testNullPassword() {
            String username = "username";
            String password = null;
            Assert.assertThrows("InvalidArgumentException expected",
                    InvalidUsernameOrPasswordException.class,
                    () -> this.userService.register(username, password, "password", "name", "surename", LocalDate.of(2001, 1, 1),Role.ROLE_USER));
            Mockito.verify(this.userService).register(username, password, "password", "name", "surename",LocalDate.of(2001, 1, 1), Role.ROLE_USER);
        }


        @Test
        public void testPasswordMismatch() {
            String username = "username";
            String password = "password";
            String confirmPassword = "otherPassword";
            Assert.assertThrows("PasswordsDoNotMatchException expected",
                    PasswordsDoNotMatchException.class,
                    () -> this.userService.register(username, password, confirmPassword, "name", "surename", LocalDate.of(2001, 1, 1),Role.ROLE_USER));
            Mockito.verify(this.userService).register(username, password, confirmPassword, "name", "surename", LocalDate.of(2001, 1, 1),Role.ROLE_USER);
        }


        @Test
        public void testDuplicateUsername() {
            User user = new User("username", "password", "name", "surename", LocalDate.of(2001, 1, 1), Role.ROLE_USER);
            Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
            String username = "username";
            Assert.assertThrows("UsernameAlreadyExistsException expected",
                    UsernameExistsException.class,
                    () -> this.userService.register(username, "password", "password", "name", "surename",LocalDate.of(2001, 1, 1), Role.ROLE_USER));
            Mockito.verify(this.userService).register(username, "password", "password", "name", "surename",LocalDate.of(2001, 1, 1),  Role.ROLE_USER);
        }
    }




