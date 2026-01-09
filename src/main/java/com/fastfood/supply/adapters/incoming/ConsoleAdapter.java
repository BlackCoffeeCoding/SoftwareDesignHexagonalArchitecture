package com.fastfood.supply.adapters.incoming;

import com.fastfood.supply.adapters.outgoing.InMemorySupplyOrderRepository;
import com.fastfood.supply.adapters.outgoing.MockSupplierNotifier;
import com.fastfood.supply.application.SupplyOrderService;
import com.fastfood.supply.ports.incoming.CreateSupplyOrderPort;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleAdapter {
    public static void main(String[] args) {
        InMemorySupplyOrderRepository repo = new InMemorySupplyOrderRepository();
        MockSupplierNotifier notifier = new MockSupplierNotifier();
        CreateSupplyOrderPort service = new SupplyOrderService(repo, notifier);
        Scanner sc = new Scanner(System.in);
        System.out.println("Hexagonal Supply Order Console");
        while (true) {
            System.out.println("1) создание заказа 2) отправка заказа 3) получение подтверждения от поставщика 0) exit");
            String cmd = sc.nextLine().trim();
            if ("0".equals(cmd)) break;
            try {
                if ("1".equals(cmd)) {
                    System.out.print("id поставщика: "); String sid = sc.nextLine();
                    System.out.print("dueDate (YYYY-MM-DD): "); LocalDate due = LocalDate.parse(sc.nextLine());
                    Map<String,Integer> items = new HashMap<>();
                    while (true) {
                        System.out.print("id продукта: ");
                        String pid = sc.nextLine();
                        if (pid.isBlank()) break;
                        System.out.print("кол-во: "); int q = Integer.parseInt(sc.nextLine());
                        items.put(pid, q);
                    }
                    String id = service.createOrder(sid, due, items);
                    System.out.println("создано: " + id);
                } else if ("2".equals(cmd)) {
                    System.out.print("id заказа: "); String id = sc.nextLine();
                    service.sendOrder(id);
                    System.out.println("заказ отправлен");
                } else if ("3".equals(cmd)) {
                    System.out.print("id заказа: "); String id = sc.nextLine();
                    service.confirmOrder(id);
                    System.out.println("подтверждено.");
                } else {
                    System.out.println("ошибка");
                }
            } catch (Exception e) {
                System.out.println("ERR: " + e.getMessage());
            }
        }
        sc.close();
    }
}