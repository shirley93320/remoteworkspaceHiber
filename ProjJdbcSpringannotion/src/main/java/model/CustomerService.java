package model;

import java.util.Arrays;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
/*
 	<bean id="customerService" class="model.CustomerService">
		<constructor-arg ref="customerDAO" />
	</bean>
 */
@Service("customerService")
public class CustomerService {
	@Autowired
	private CustomerDAO customerDao;
	
	public CustomerService() {
	}

/*	public CustomerService(CustomerDAO customerDao) {
		this.customerDao = customerDao;
	}
*/
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.config.xml");
		CustomerService customerService = (CustomerService) context.getBean("customerService");
		//CustomerBean bean = customerService.login("Babe", "BBB");
		CustomerBean bean = customerService.login("Ellen", "E");
		System.out.println(bean);
		
		System.out.println(customerService.changPassword("Ellen", "ABC", "E"));
		((ConfigurableApplicationContext) context).close();

	}
	public boolean changPassword(
			String username, String oldPassword, String newPassword) {
		CustomerBean bean = this.login(username, oldPassword);
		if(bean!=null) {
			byte[] temp = newPassword.getBytes();	//�ϥΪ̿�J
			return customerDao.update(
					temp, bean.getEmail(), bean.getBirth(), username);
		}
		return false;
	}
	public CustomerBean login(String username, String password) {
		CustomerBean bean = customerDao.select(username);
		if(bean!=null) {
			if(password!=null && password.length()!=0) {
				byte[] pass = bean.getPassword();	//��Ʈw��X
				byte[] temp = password.getBytes();	//�ϥΪ̿�J
				if(Arrays.equals(pass, temp)) {
					return bean;
				}
			}
		}
		return null;
	}
}
