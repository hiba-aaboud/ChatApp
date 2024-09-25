import java.rmi.*;
import java.util.*;

public interface Chat extends Remote {

    public void enter(User user) throws RemoteException;

    List<User> getUserList() throws RemoteException;

    public void leave(User user) throws RemoteException;

    void sendMsg(User user, String msg) throws RemoteException;

    boolean nameAvail(String name) throws RemoteException;
  
    boolean historyAvail() throws RemoteException;

    void showAllMessages(User to) throws RemoteException;
}
