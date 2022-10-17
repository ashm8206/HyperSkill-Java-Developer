package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static cinema.Cinema.getAllSeats;

/* Not good practice to Declare two classes in one file, but you can have it*/


@RestController
public class CinemaController {
    private final Cinema cinema;

    public CinemaController() {
        this.cinema = getAllSeats(9, 9);
    }

    @GetMapping("/seats")
    public Cinema getSeats() {
        return cinema;
    }

    /* STAGE 2
    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@RequestBody Seat seat) {

        if (seat.getColumn() > cinema.getTotal_columns()
                || seat.getRow() > cinema.getTotal_rows()
                || seat.getRow() < 1
                || seat.getColumn() < 1) {
            return new ResponseEntity<>(Map.of("error", "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }
        for (int i = 0; i <= cinema.getAvailable_seats().size(); i++) {
            Seat s = cinema.getAvailable_seats().get(i);
            if (s.equals(seat)) {
                if (s.getIsAvailable()) {
                    s.setIsAvailable(false);
                    return new ResponseEntity<>(s, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(Map.of("error", "The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

     */
    /*Stage 3
    * We added another DS in cinema called PurchasedTicket and  its class to as we return an updated response body from Stage 2
    * Token needs to be class as we accept it in ResponseBody
    * */
    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@RequestBody Seat seat) {
        if (seat.getColumn() > cinema.getTotal_columns()
                || seat.getRow() > cinema.getTotal_rows()
                || seat.getRow() < 1
                || seat.getColumn() < 1) {
            return new ResponseEntity<>(Map.of("error", "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }
        for (int i = 0; i < cinema.getAvailable_seats().size(); i++) {
            Seat s = cinema.getAvailable_seats().get(i);
            if (s.equals(seat)) {
                PurchasedTicket purchasedTicket = new PurchasedTicket(UUID.randomUUID(), s);
                cinema.getPurchasedTickets().add(purchasedTicket);
                cinema.getAvailable_seats().remove(i);
                return new ResponseEntity<>(purchasedTicket, HttpStatus.OK);
            }
        }
                return new ResponseEntity<>(Map.of("error", "The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);

        }


    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(@RequestBody Token token) {
        List<PurchasedTicket> purchasedTickets = cinema.getPurchasedTickets();
        for (PurchasedTicket purchasedTicket : purchasedTickets) {
            if (purchasedTicket.getToken().equals(token.getToken())) {
                purchasedTickets.remove(purchasedTicket);
                cinema.getAvailable_seats().add(purchasedTicket.getTicket());
                return new ResponseEntity<>(Map.of("returned_ticket", purchasedTicket.getTicket()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error", "Wrong token!"), HttpStatus.BAD_REQUEST);
    }
/*Stage 4 funtion*/
    @PostMapping("/stats")
    public ResponseEntity<?> stats(@RequestParam(value = "password", required = false) String password) {
        if (password != null && password.equals("super_secret")) {
            Map<String, Integer> statistic = new HashMap<>();
            int currentIncome = 0;
            for (PurchasedTicket purchasedTicket : cinema.getPurchasedTickets()) {
                currentIncome += purchasedTicket.getTicket().getPrice();
            }
            int numberOfAvailableSeats = cinema.getAvailable_seats().size();
            int numberOfPurchasedTickets = cinema.getPurchasedTickets().size();
            statistic.put("current_income", currentIncome);
            statistic.put("number_of_available_seats", numberOfAvailableSeats);
            statistic.put("number_of_purchased_tickets", numberOfPurchasedTickets);
            return new ResponseEntity<>(statistic, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error", "The password is wrong!"), HttpStatus.valueOf(401));
        }
    }

}
