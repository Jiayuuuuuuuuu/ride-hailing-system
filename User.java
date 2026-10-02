import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String password;
    private List<Booking> bookingHistory;

    private static Map<String, User> users = new HashMap<>();

    static {
        // Initialize users
        addUser(new User("Jia Yu", "abcd"));
        addUser(new User("Boon", "1234"));
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.bookingHistory = new ArrayList<>();
    }

    public static void addUser(User user) {
        users.put(user.username, user);
    }

    public static User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.password.equals(password)) {
            return user;
        }
        return null;
    }

    public void addBooking(Booking booking) {
        bookingHistory.add(booking);
    }

    public List<Booking> getBookingHistory() {
        return new ArrayList<>(bookingHistory);
    }

    public String getUsername() {
        return username;
    }
}