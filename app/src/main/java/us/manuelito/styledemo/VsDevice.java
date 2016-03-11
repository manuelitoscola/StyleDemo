package us.manuelito.styledemo;

/**
 * Created by scola on 3/11/16.
 */
public interface VsDevice {

    public void login();
    public void logout();
    public boolean isLogged();

    public String getName();
    public String getManufacturer();
    public String getBrand();

    public String getAddress();
    public String getUsername();
}
