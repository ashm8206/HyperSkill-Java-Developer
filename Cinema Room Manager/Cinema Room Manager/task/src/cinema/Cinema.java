package cinema;

import java.util.Arrays;
import java.util.Scanner;

public class Cinema {
    private final int CINEMA_ROWS;
    private final int CINEMA_COLS;
    private int TICKET_PRICE_1;
    private int TICKET_PRICE_2;
    private int TOTAL_SALE;
    private int TICKETS_SOLD;
    private int CURRENT_INCOME;
    private char[][] cinemaBoard;
    public Cinema(int rows, int cols) {
        this.CINEMA_ROWS = rows;
        this.CINEMA_COLS = cols;
        this.TICKET_PRICE_1 = 10;
        this.TICKET_PRICE_2 = 8;
        this.TOTAL_SALE = 0;
        this.TICKETS_SOLD = 0;
        this.CURRENT_INCOME = 0;
        createCinema();
        getSale();
    }
    private void createCinema(){
        this.cinemaBoard = new char[this.CINEMA_ROWS][this.CINEMA_COLS];
        for (char[] row : cinemaBoard){
            Arrays.fill(row, 'S');
        }
    }

    private void printCinema() {
        System.out.println("Cinema:");
        System.out.print(" ");
        for (int i = 1 ; i < this.CINEMA_COLS + 1 ; i++)
            System.out.print(" " + i);
        System.out.println();
        for (int i = 0 ; i < this.CINEMA_ROWS; i++) {

            System.out.print(i+1);
            for (int j = 0; j < cinemaBoard[i].length; j++) {
                System.out.print(" "+cinemaBoard[i][j]);
            }
            System.out.print("\n");
        }
    }
    private int getSale() {
        int totalSeats = this.CINEMA_ROWS * this.CINEMA_COLS;

        if (totalSeats <= 60) {
            this.TOTAL_SALE = totalSeats * TICKET_PRICE_1;
        } else {
            int firstHalf = (int) Math.floor(this.CINEMA_ROWS / 2);
            int secondHalf = this.CINEMA_ROWS - firstHalf;
            this.TOTAL_SALE  = firstHalf * this.CINEMA_COLS * TICKET_PRICE_1
                                + secondHalf * this.CINEMA_COLS * TICKET_PRICE_2;



        }
        return this.TOTAL_SALE;
    }
    private int getTicketCost(int row) {
            int ticketPrice;
            int totalSeats = this.CINEMA_ROWS * this.CINEMA_COLS;
            if (totalSeats <= 60) {
                ticketPrice = TICKET_PRICE_1;
            }
            else {
                int firstHalf = (int) Math.floor(this.CINEMA_ROWS/2);
                if (row <= firstHalf) {
                    ticketPrice = TICKET_PRICE_1;
                }
                else {
                    ticketPrice = TICKET_PRICE_2;
                }


            }
            this.CURRENT_INCOME += ticketPrice;
            return ticketPrice;



    }

    private void reserveSeat(int level, int seat) {
        this.cinemaBoard[level-1][seat-1] ='B';
        this.TICKETS_SOLD++;
    }

    private void calculateStats(){


    }
    private void statisticPrint() {

        double val = (this.TICKETS_SOLD * 100) /(double)(this.CINEMA_ROWS * this.CINEMA_COLS);
        String percentageSold = String.format("%.2f", val);
        System.out.println("Number of purchased tickets: " + this.TICKETS_SOLD);
        System.out.println("Percentage: " + percentageSold + "%");
        System.out.println("Current income: " + "$" +this.CURRENT_INCOME);
        System.out.println("Total income: " + "$" + this.TOTAL_SALE);


    }

    public static void main(String[] args) {


        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of rows");
        int rows = sc.nextInt();
        System.out.println("Enter the number of seats in each row");
        int cols = sc.nextInt();
        Cinema cinema = new Cinema(rows, cols);
//        System.out.println(cinema.getSale());
        boolean stopped = true;
        while(stopped) {
            System.out.println("1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");

            int option = sc.nextInt();
            switch(option) {
                case 1:
                    cinema.printCinema();
                    System.out.println();
                    break;
                case 2:
                    Boolean ticketPurchasePending = true;
                    while(ticketPurchasePending) {

                        System.out.println("Enter a row number:");
                        int level = sc.nextInt();
                        System.out.println("Enter a seat number in that row:");
                        int seat = sc.nextInt();
                        if ((level < 0 || level > cinema.CINEMA_ROWS) || (seat < 0 || seat > cinema.CINEMA_COLS)) {
                            System.out.println("Wrong input");
                        }
                        else if (cinema.cinemaBoard[level - 1][seat - 1] == 'B') {
                            System.out.println("That ticket has already been purchased!");
                        }  else {
                            int cost = cinema.getTicketCost(level);
                            System.out.println("Ticket Price:\n" + "$" + cost);
                            cinema.reserveSeat(level, seat);
                            cinema.calculateStats();
                            ticketPurchasePending = false;
                        }
                    }
                    break;
                case 3:
                    cinema.statisticPrint();
                    break;
                case 0:
                    stopped = false;
                    break;
            }

        }

        }
    }
