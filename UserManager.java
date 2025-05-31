import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static Map<String, ATMUser> users = new HashMap<>();

    static {
        // Preload users
        users.put("user1", new ATMUser("user1", "1234", 500.00));
        users.put("user2", new ATMUser("user2", "5678", 750.00));
    }

    public static ATMUser authenticate(String username, String pin) {
        ATMUser user = users.get(username);
        if (user != null && user.getPin().equals(pin)) {
            return user;
        }
        return null;
    }

    public static boolean changePin(ATMUser user, String oldPin, String newPin) {
        if (user.getPin().equals(oldPin)) {
            user.setPin(newPin);
            return true;
        }
        return false;
    }

    public static Map<String, ATMUser> getAllUsers() {
        return users;
    }
}
