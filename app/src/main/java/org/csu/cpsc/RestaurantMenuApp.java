package org.csu.cpsc;

// Data Structures Assignment (single-file version)
// Requirements tagged Req1–Req6

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class RestaurantMenuApp {
    public static void main(String[] args) {
        new UserInterface().start();
    }
}

/* Req1: MenuItem */
class MenuItem {
    private int itemID;
    private String name;
    private String category;
    private int calories;
    private double price;

    public MenuItem(int itemID, String name, String category, int calories, double price) {
        setItemID(itemID);
        setName(name);
        setCategory(category);
        setCalories(calories);
        setPrice(price);
    }

    // getters / setters with minimal validation
    public int getItemID() { return itemID; }
    public void setItemID(int itemID) { this.itemID = itemID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }

    @Override
    public String toString() {
        return name + "- price: $" + price + " - calories: " + calories;
    }
}

/* Req2: Menu */
class Menu {
    private MenuItem[] menuList;
    private int count;

    public Menu(int size) { menuList = new MenuItem[size]; }
    public Menu() { this(10); } // default

    public void addItem(MenuItem item) { menuList[count++] = item; }

    public void printMenu() { // Req2 – printMenu
        for (int i = 0; i < count; i++)
            System.out.println(menuList[i]);
    }

    public int size() { return count; }
    public MenuItem get(int i) { return menuList[i]; }
}

/* Req3–Req6: UserInterface */
class UserInterface {
    private Menu restaurantMenu = new Menu();

    public void start() {
        Scanner sc = new Scanner(System.in);
        String cmd;
        do {
            showOptions();
            System.out.print("\nSelect your option:\n");
            cmd = sc.nextLine().trim().toLowerCase();
            switch (cmd) {
                case "load" -> loadMenu(sc);
                case "new"  -> createMenu(sc);
                case "view" -> restaurantMenu.printMenu();
                case "quit" -> System.out.println("Goodbye!");
                default     -> System.out.println("Unknown option");
            }
        } while (!cmd.equals("quit"));
    }

    private void showOptions() {
        System.out.println("\nload: Load a Menu from File");
        System.out.println("new: Create a new menu");
        System.out.println("view: View items on the menu");
        System.out.println("quit: Quit the menu");
    }

    // Req3 – load existing menu
    private void loadMenu(Scanner sc) {
        try {
            System.out.print("Enter file path to load: ");
            Path path = Path.of(sc.nextLine());
            List<String> lines = Files.readAllLines(path);
            restaurantMenu = new Menu(lines.size());
            for (String l : lines) {
                String[] p = l.split(",");
                restaurantMenu.addItem(new MenuItem(
                        Integer.parseInt(p[0]), p[1], p[2],
                        Integer.parseInt(p[3]), Double.parseDouble(p[4])));
            }
            System.out.println("Menu loaded.");
        } catch (Exception e) {
            System.out.println("Load error: " + e.getMessage());
        }
    }

    // Req4 – create new menu and save to file
    private void createMenu(Scanner sc) {
        System.out.print("Enter the number of menu items: ");
        int n = Integer.parseInt(sc.nextLine());
        restaurantMenu = new Menu(n);
        for (int i = 0; i < n; i++) {
            System.out.print("Enter the item ID: ");
            int id = Integer.parseInt(sc.nextLine());
            System.out.print("Enter the item name: ");
            String name = sc.nextLine();
            System.out.print("Enter the item category: ");
            String cat = sc.nextLine();
            System.out.print("Enter the item price: ");
            double price = Double.parseDouble(sc.nextLine());
            System.out.print("Enter the item calories: ");
            int cal = Integer.parseInt(sc.nextLine());
            restaurantMenu.addItem(new MenuItem(id, name, cat, cal, price));
        }
        System.out.print("Enter path to save the menu file: ");
        try (PrintWriter pw = new PrintWriter(sc.nextLine())) {
            for (int i = 0; i < restaurantMenu.size(); i++) {
                MenuItem m = restaurantMenu.get(i);
                pw.println(m.getItemID() + "," + m.getName() + "," + m.getCategory() + ","
                           + m.getCalories() + "," + m.getPrice());
            }
            System.out.println("Menu saved.");
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }
}