package model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/*
 	<bean id="productService" class="model.ProductService">
		<constructor-arg ref="productDAO" />
	</bean>
 */
@Service("productService")
public class ProductService {
	@Autowired
	private ProductDAO productDao;
	
	public ProductService() {
	}

/*	public ProductService(ProductDAO productDao) {
		this.productDao = productDao;
	}
*/
	public static void main(String[] args) {
		ApplicationContext context=new ClassPathXmlApplicationContext("beans.config.xml");
		ProductService service = (ProductService) context.getBean("productService");
		List<ProductBean> beans = service.select(null);
		System.out.println("beans="+beans);
		((ConfigurableApplicationContext) context).close();

	}
	
	public List<ProductBean> select(ProductBean bean) {
		List<ProductBean> result = null;
		if(bean!=null && bean.getId()!=0) {
			ProductBean temp = productDao.select(bean.getId());
			if(temp!=null) {
				result = new ArrayList<ProductBean>();
				result.add(temp);
			}
		} else {
			result = productDao.select(); 
		}
		return result;
	}
	public ProductBean insert(ProductBean bean) {
		ProductBean result = null;
		if(bean!=null) {
			result = productDao.insert(bean);
		}
		return result;
	}
	public ProductBean update(ProductBean bean) {
		ProductBean result = null;
		if(bean!=null) {
			result = productDao.update(bean.getName(), bean.getPrice(),
					bean.getMake(), bean.getExpire(), bean.getId());
		}
		return result;
	}
	public boolean delete(ProductBean bean) {
		boolean result = false;
		if(bean!=null) {
			result = productDao.delete(bean.getId());
		}
		return result;
	}

}
