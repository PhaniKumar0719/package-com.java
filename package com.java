package com.example;
import java.util.Scanner;
import com.example.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

public class CancellationService {
    private List<Reservation> reservations;

    public CancellationService(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Reservation cancelReservation(String reservationId) {
        Reservation reservation = reservations.stream().filter(r -> r.getReservationId().equals(reservationId)).findFirst().orElse(null);
        if (reservation != null) {
            reservations = reservations.stream().filter(r -> !r.getReservationId().equals(reservationId)).collect(Collectors.toList());
        }
        return reservation;
    }
}
public class ReservationService {
    private List<Reservation> reservations;
    private List<Train> trains;

    public ReservationService() {
        reservations = new ArrayList<>();
        trains = new ArrayList<>();
        trains.add(new Train("12345", "Express Train"));
        trains.add(new Train("67890", "Fast Train"));
    }

    public Reservation createReservation(User user, String trainNumber, String classType, String dateOfJourney, String from, String to) {
        Train train = trains.stream().filter(t -> t.getTrainNumber().equals(trainNumber)).findFirst().orElse(null);
        if (train == null) {
            throw new IllegalArgumentException("Invalid train number");
        }
        Reservation reservation = new Reservation(UUID.randomUUID().toString(), user, train, classType, dateOfJourney, from, to);
        reservations.add(reservation);
        return reservation;
    }

    public List<Train> getTrains() {
        return trains;
    }
}
public class AuthService {
    private Map<String, String> userDatabase;

    public AuthService() {
        userDatabase = new HashMap<>();
        userDatabase.put("user1", "password1");
        userDatabase.put("user2", "password2");
    }

    public boolean authenticate(String loginId, String password) {
        return userDatabase.containsKey(loginId) && userDatabase.get(loginId).equals(password);
    }
}

public class Reservation {
    private String reservationId;
    private User user;
    private Train train;
    private String classType;
    private String dateOfJourney;
    private String from;
    private String to;

    public Reservation(String reservationId, User user, Train train, String classType, String dateOfJourney, String from, String to) {
        this.reservationId = reservationId;
        this.user = user;
        this.train = train;
        this.classType = classType;
        this.dateOfJourney = dateOfJourney;
        this.from = from;
        this.to = to;
    }

    // Getters and setters
    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getDateOfJourney() {
        return dateOfJourney;
    }

    public void setDateOfJourney(String dateOfJourney) {
        this.dateOfJourney = dateOfJourney;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
public class Train {
    private String trainNumber;
    private String trainName;

    public Train(String trainNumber, String trainName) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
    }

    // Getters and setters
    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }
}
public class User {
    private String loginId;
    private String password;

    public User(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    // Getters and setters
    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
public class OnlineReservationSystem {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        ReservationService reservationService = new ReservationService();
        CancellationService cancellationService = new CancellationService(reservationService.getReservations());

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Online Reservation System");

        System.out.print("Login ID: ");
        String loginId = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (authService.authenticate(loginId, password)) {
            User user = new User(loginId, password);
            System.out.println("Login successful!");

            while (true) {
                System.out.println("1. Make a reservation");
                System.out.println("2. Cancel a reservation");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (option == 1) {
                    System.out.print("Train Number: ");
                    String trainNumber = scanner.nextLine();
                    System.out.print("Class Type: ");
                    String classType = scanner.nextLine();
                    System.out.print("Date of Journey: ");
                    String dateOfJourney = scanner.nextLine();
                    System.out.print("From: ");
                    String from = scanner.nextLine();
                    System.out.print("To: ");
                    String to = scanner.nextLine();

                    Reservation reservation = reservationService.createReservation(user, trainNumber, classType, dateOfJourney, from, to);
                    System.out.println("Reservation successful! Reservation ID: " + reservation.getReservationId());
                } else if (option == 2) {
                    System.out.print("Reservation ID: ");
                    String reservationId = scanner.nextLine();
                    Reservation cancelledReservation = cancellationService.cancelReservation(reservationId);
                    if (cancelledReservation != null) {
                        System.out.println("Cancellation successful for Reservation ID: " + reservationId);
                    } else {
                        System.out.println("Invalid Reservation ID.");
                    }
                } else if (option == 3) {
                    break;
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
            }
        } else {
            System.out.println("Invalid login credentials.");
        }

        scanner.close();
    }
}
