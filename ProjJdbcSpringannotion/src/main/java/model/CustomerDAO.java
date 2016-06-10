package model;

public interface CustomerDAO {

	CustomerBean select(String custid);

	boolean update(byte[] password, String email, java.util.Date birth, String custid);

}