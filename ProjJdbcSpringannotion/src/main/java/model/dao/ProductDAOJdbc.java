package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import model.ProductBean;
import model.ProductDAO;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;
/*
<bean id="productDAO" class="model.dao.ProductDAOJdbc">
<constructor-arg ref="dataSource" />
</bean>
*/
@Repository("productDAO")
public class ProductDAOJdbc implements ProductDAO {
	@Resource
	DataSource dataSource;
/*	
	public ProductDAOJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public ProductDAOJdbc() {
	}
*/
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.config.xml");
		ProductDAO dao = (ProductDAO) context.getBean("productDAO");
		ProductBean bean = dao.select(1);
		System.out.println(bean);
		
		List<ProductBean> beans = dao.select();
		System.out.println(beans);
		((ConfigurableApplicationContext) context).close();

	}
	private static final String SELECT_BY_ID =
			"select * from product where id=?";
	@Override
	public ProductBean select(int id) {
		ProductBean result = null;
		ResultSet rset = null;
		try(Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID);) {
			stmt.setInt(1, id);
			rset = stmt.executeQuery();
			if(rset.next()) {
				result = new ProductBean();
				result.setId(rset.getInt("id"));
				result.setName(rset.getString("name"));
				result.setPrice(rset.getDouble("price"));
				result.setMake(rset.getDate("make"));
				result.setExpire(rset.getInt("expire"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	private static final String SELECT_ALL =
			"select * from product";
	@Override
	public List<ProductBean> select() {
		List<ProductBean> result = null;
		try(Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
			ResultSet rset = stmt.executeQuery();) {
			
			result = new ArrayList<ProductBean>();
			while(rset.next()) {
				ProductBean row = new ProductBean();
				row.setId(rset.getInt("id"));
				row.setName(rset.getString("name"));
				row.setPrice(rset.getDouble("price"));
				row.setMake(rset.getDate("make"));
				row.setExpire(rset.getInt("expire"));
				
				result.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static final String INSERT =
			"insert into product (id, name, price, make, expire) values (?, ?, ?, ?, ?)";
	@Override
	public ProductBean insert(ProductBean bean) {
		ProductBean result = null;
		if(bean!=null) {
			try(Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(INSERT);) {
			
				stmt.setInt(1, bean.getId());
				stmt.setString(2, bean.getName());
				stmt.setDouble(3, bean.getPrice());
				
				java.util.Date make = bean.getMake();
				if(make!=null) {
					stmt.setDate(4, new java.sql.Date(make.getTime()));
				} else {
					stmt.setDate(4, null);
				}
				stmt.setInt(5, bean.getExpire());
				int i = stmt.executeUpdate();
				if(i==1) {
					result = bean;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	private static final String UPDATE =
			"update product set name=?, price=?, make=?, expire=? where id=?";
	@Override
	public ProductBean update(String name,
			double price, java.util.Date make, int expire, int id) {
		ProductBean result = null;
		try(Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(UPDATE);) {
			
			stmt.setString(1, name);
			stmt.setDouble(2, price);
			if(make!=null) {
				stmt.setDate(3, new java.sql.Date(make.getTime()));
			} else {
				stmt.setDate(3, null);
			}
			stmt.setInt(4, expire);
			stmt.setInt(5, id);
			int i = stmt.executeUpdate();
			if(i==1) {
				result = this.select(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return result;
	}
	private static final String DELETE =
			"delete from product where id=?";
	@Override
	public boolean delete(int id) {
		try(Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(DELETE);) {
			
			stmt.setInt(1, id);
			int i = stmt.executeUpdate();
			if(i==1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
