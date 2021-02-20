package it.univpm.hhc.test.unit;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.univpm.hhc.model.dao.AddressDao;
import it.univpm.hhc.model.dao.AddressDaoDefault;
import it.univpm.hhc.services.AddressService;
import it.univpm.hhc.services.AddressServiceDefault;
import it.univpm.hhc.model.dao.UserDetailsDao;
import it.univpm.hhc.model.dao.UserDetailsDaoDefault;
import it.univpm.hhc.model.entities.Address;
import it.univpm.hhc.model.entities.User;
import it.univpm.hhc.test.DataServiceConfigTest;


public class TestAddressDao {

	private AnnotationConfigApplicationContext ctx;
	private AddressDao addressDao;
	private UserDetailsDao userDetailsDao;
	private AddressService addressService;
	private SessionFactory sf;
	private User u;

	@BeforeAll
	static void setup() {
		System.out.println("Prepare the test suite environment");
		// TODO prepare data structure and storage, if needed

	}

	@AfterAll
	static void tearDown() {
		System.out.println("Clean-up the test suite environment");
		// TODO cleanup data structures and storage, if needed

	}
	
	@BeforeEach
	void openContext() {
		/**
		 * Ciascun test ha bisogno di un contesto applicativo e un addressDao
		 */
		System.out.println("Prepare the test case environment");

		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		
		addressDao = ctx.getBean("addressDao", AddressDao.class);
		
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
	}
	
	@AfterEach
	void closeContext() {
		System.out.println("Clean-up the test case environment");
		
		ctx.close();
	}
	
	
	///TESTS
	
	
	@Test
	void testBeginCommitTransaction() {
		/**
		 * Check that the dao stores the session for later use, allowing to share the
		 * session
		 */

		Session s = sf.openSession();

		assertTrue(s.isOpen());

		s.beginTransaction();

		addressDao.setSession(s);

		assertEquals(s, addressDao.getSession()); // s.equals(addressDao.getSession());
		assertSame(s, addressDao.getSession()); // s == addressDao.getSession();
		assertTrue(addressDao.getSession().getTransaction().isActive());

		s.getTransaction().commit();

		assertFalse(addressDao.getSession().getTransaction().isActive());

	}
	
	@Test
	void testAutoCreationOfSession() {
		/**
		 * Check that the dao opens a new session if no session is passed
		 * (NB the returned session has transaction disabled by default)
		 * 		 
		 */
		
		Session s = addressDao.getSession();
		assertNotNull(s); // s.equals(addressDao.getSession());
		assertFalse(s.getTransaction().isActive());
	}
	
	@Test
	void testCreateaddressDuplicateNames() {
		/**
		 * We test that it is possible to create two addresss with same name and surname
		 */
		
		Session s = sf.openSession();

		addressDao.setSession(s);

		Address newaddress1 = addressDao.create("60098", "Genova", "rovereto", "67",u);

		try {
			Address newaddress2 = addressDao.create(newaddress1.getCap(), newaddress1.getCity(), newaddress1.getStreet(), newaddress1.getCiv_num(),u);
			assertTrue(true);
		} catch (Exception e) {
			// pass
			fail("Unexpected exception creating address with duplicate name: " + e.getMessage());
		}

	}

	@Test
	void testNoaddresssAtBeginning() {
		/**
		 * Check that there are no address when the application loads
		 */

		Session s = sf.openSession();

		addressDao.setSession(s);

		List<Address> alladdresss = addressDao.findAll();

		assertEquals(alladdresss.size(), 0);
	}

	@Test
	void testAllCreatedAreFound() {
		/**
		 * Generate N address, find all of them
		 */
		int N = 10;

		Session s = sf.openSession();

		addressDao.setSession(s);

		for (int i = 0; i < N; i++) {
			addressDao.create("60098", "Genova", "rovereto", "67",u);

			List<Address> alladdresss = addressDao.findAll();
			assertEquals(alladdresss.size(), i + 1);
		}
	}
	
	@Test
	void testaddressCanHaveNoUser() {
		/**
		 * An address can have empty user field
		 */
		Session s = sf.openSession();

		Address newaddress = addressDao.create("60098", "Genova", "rovereto", "67",null);

		assertNotNull(newaddress);
	}
	
	
	@Test
	void testaddressIsUpdatedCorrectlyWithMerging() {
		Session s = sf.openSession();

		addressDao.setSession(s);

		Address inserted = addressDao.create("60098", "Genova", "rovereto", "67",u);
		
		Address updated = new Address();
		updated.setAddress_id(inserted.getAddress_id());
		updated.setCap("60098");
		updated.setStreet("via");
		updated.setCity("Genova");
		updated.setCiv_num("67");
		updated.setUser(u);
		
		updated = addressDao.update(updated);
		
		Address found = addressDao.findById(inserted.getAddress_id());
		
		assertSame(inserted, updated);
		assertSame(updated, found);
		assertSame(found, inserted);
	}
	
	/*@Test
	void testaddressCanHaveNoCivNum() {
		/**
		 * An address can have empty civ_num field
		 
		Session s = sf.openSession();

		Address newaddress = addressDao.create("60098", "Genova", "rovereto", null , u);

		assertNotNull(newaddress);
	}*/
	
	/*@Test
	void testUserCanHaveNoMoreThreeAddress() {
		
		Session s1 = addressDao.getSession();
		Session s2 = userDetailsDao.getSession();
		Session s1 = sf.openSession();
		Session s2 = sf.openSession();
		addressDao.setSession(s1);
		userDetailsDao.setSession(s2);
		
		//User user = this.userDetailsDao.create("ciao", "mail@mail.com", "Manuel", "Rodriguez");
		//Address a1 = addressDao.create("60098", "Pavia", "rovereto", "45" , user);
		//Address a2 = addressDao.create("60098", "Genova", "rovereto", "45" , user);
		//Address a3 = addressDao.create("60098", "Milano", "rovereto", "45" , user);
		//Address a4 = addressDao.create("60098", "Siracusa", "rovereto", "45" , user);
		
		//List<Address> allAddresses = this.addressService.findByUserId(user.getUser_id());
		//assertSame(allAddresses.size(), 3);
		
	}*/

}