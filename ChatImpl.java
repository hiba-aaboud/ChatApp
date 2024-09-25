import java.rmi.RemoteException;
import java.io.*;
import java.util.*;

public class ChatImpl implements Chat, Serializable {
    private static final String PATH = "backup.txt";

    private final List<String> messages;
    private final List<User> userList;

    public ChatImpl() {
        userList = Collections.synchronizedList(new ArrayList<>());
        messages = Collections.synchronizedList(new ArrayList<>());
        restoreMessages();
    }

   

    @Override
    public void enter(User u) throws RemoteException {
        if (!userList.contains(u)) {
            userList.add(u);
            for(User user : userList){
                if(!user.getName().equals(u.getName()))
                System.out.println("'hi'");
                user.joined(u.getName() + " has entered the chat!");
            }
        }     
    }
    @Override
    public boolean nameAvail(String name) throws RemoteException {
        for (User user : userList) {
            if (name.equalsIgnoreCase(user.getName().trim())) {
                return false; 
            }
        }
        return true; 
    }

    @Override
    public List<User> getUserList() {
        return userList;
    }

    @Override
    public void leave(User u) throws RemoteException {
        for (User user : userList) {
            if (!user.getName().equals(u.getName()))
                user.left(u.getName() + " has left the chat!");
            else
                user.left("You have left the chat!");
        }
        userList.remove(u);
    }

    
    @Override
    public void sendMsg(User u , String msg) throws RemoteException {
        messages.add(u.getName() + " : " + msg);
        addToBackup(u.getName() + " : " + msg);
        for(User user: userList) {
            try {
                if(!user.getName().equals(u.getName()))
                    user.talk(u.getName(), msg);
            }catch (RemoteException e){
                userList.remove(user);
            }
        }
    }

    public synchronized void addToBackup(String msg){
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter(PATH,true));
            output.append(msg).append("\n");
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean historyAvail() {
        return messages.size() >= 1;
    }

    public void deleteBackup() {
        File f = new File(PATH);
        f.delete();
    }
    @Override
public void showAllMessages(User to) throws RemoteException {
    for (String msg : messages) {
        String[] parts = msg.split(":", 2);
        if (parts.length == 2) {
            String sender = parts[0].trim();
            String messageContent = parts[1].trim();
            to.talk(sender, messageContent);
        }
    }
}

public void restoreMessages() {
    File file = new File(PATH); // Creating a File object for the backup file
    if (!file.exists() || file.isDirectory()) {
        return; // If the file doesnt exist we get out
    }

    BufferedReader reader = null; // Initializing a buffer to read from the file
    Scanner scanner = new Scanner(System.in); 

    try {
        reader = new BufferedReader(new FileReader(file)); 
        System.out.println("Recover message history? yes/no : "); 
        String response = scanner.nextLine().trim(); // Reading user input and trimming leading/trailing whitespace

        if (response.equalsIgnoreCase("yes")) { 
            String line;
            while ((line = reader.readLine()) != null) { // Reading lines from the file until the end
                messages.add(line); 
            }
        }
    } catch (IOException e) { // Catching any IOException that may occur
        throw new RuntimeException("Error while restoring messages", e); 
    } finally { 
        if (reader != null) { 
            try {
                reader.close(); // Closing the reader to release resources
            } catch (IOException e) { 
                e.printStackTrace(); 
            }
        }
        deleteBackup(); // Deleting the backup file 
    }
}


 
}
