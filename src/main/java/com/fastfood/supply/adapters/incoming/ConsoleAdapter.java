package com.fastfood.supply.adapters.incoming;

import com.fastfood.supply.adapters.outgoing.InMemorySupplyOrderRepository;
import com.fastfood.supply.adapters.outgoing.MockSupplierNotifier;
import com.fastfood.supply.application.SupplyOrderService;
import com.fastfood.supply.ports.incoming.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleAdapter {

    public static void main(String[] args) {

        InMemorySupplyOrderRepository repo = new InMemorySupplyOrderRepository();
        MockSupplierNotifier notifier = new MockSupplierNotifier();

        SupplyOrderService service = new SupplyOrderService(repo, notifier);

        CreateSupplyOrderPort createPort = service;
        ConfirmSupplyOrderPort confirmPort = service;
        TrackSupplyOrderPort trackPort = service;
        DeliveryPort deliveryPort = service;
        QualityCheckPort qualityPort = service;
        AcceptancePort acceptancePort = service;
        DefectiveReturnPort defectiveReturnPort = service;

        Scanner sc = new Scanner(System.in);
        System.out.println("Hexagonal Supply Order Console");

        while (true) {
            System.out.println("1) создать заказ\n" +
                            "2) отправить заказ\n" +
                            "3) подтвердить от поставщика\n" +
                            "4) отметить прибытие поставки\n" +
                            "5) проверить качество (ПРОШЛО)\n" +
                            "6) проверить качество (НЕ ПРОШЛО)\n" +
                            "7) принять поставку\n" +
                            "8) отклонить поставку\n" +
                            "9) вернуть бракованный товар\n" +
                            "10) посмотреть статус заказа\n" +
                            "0) exit"
            );

            String cmd = sc.nextLine().trim();
            if ("0".equals(cmd)) break;

            try {
                switch (cmd) {
                    case "1" -> {
                        System.out.print("id поставщика: ");
                        String sid = sc.nextLine();

                        System.out.print("dueDate (YYYY-MM-DD): ");
                        LocalDate due = LocalDate.parse(sc.nextLine());

                        Map<String, Integer> items = new HashMap<>();
                        while (true) {
                            System.out.print("id продукта (enter для конца): ");
                            String pid = sc.nextLine();
                            if (pid.isBlank()) break;

                            System.out.print("кол-во: ");
                            int q = Integer.parseInt(sc.nextLine());
                            items.put(pid, q);
                        }

                        String id = createPort.createOrder(sid, due, items);
                        System.out.println("Заказ создан: " + id);
                    }

                    case "2" -> {
                        System.out.print("id заказа: ");
                        createPort.sendOrder(sc.nextLine());
                        System.out.println("Заказ отправлен");
                    }

                    case "3" -> {
                        System.out.print("id заказа: ");
                        confirmPort.receiveSupplierConfirmation(sc.nextLine());
                        System.out.println("Подтверждение получено");
                    }

                    case "4" -> {
                        System.out.print("id заказа: ");
                        deliveryPort.markDelivered(sc.nextLine());
                        System.out.println("Поставка отмечена как полученная");
                    }

                    case "5" -> {
                        System.out.print("id заказа: ");
                        qualityPort.checkQuality(sc.nextLine(), true);
                        System.out.println("Качество подтверждено");
                    }

                    case "6" -> {
                        System.out.print("id заказа: ");
                        qualityPort.checkQuality(sc.nextLine(), false);
                        System.out.println("Качество НЕ прошло проверку");
                    }

                    case "7" -> {
                        System.out.print("id заказа: ");
                        acceptancePort.acceptDelivery(sc.nextLine());
                        System.out.println("Поставка принята");
                    }

                    case "8" -> {
                        System.out.print("id заказа: ");
                        acceptancePort.rejectDelivery(sc.nextLine());
                        System.out.println("Поставка отклонена");
                    }

                    case "9" -> {
                        System.out.print("id заказа: ");
                        defectiveReturnPort.returnDefectiveDelivery(sc.nextLine());
                        System.out.println("Брак возвращён поставщику, заказ снова отправлен");
                    }

                    case "10" -> {
                        System.out.print("id заказа: ");
                        System.out.println("Статус: " + trackPort.getStatus(sc.nextLine()));
                    }

                    default -> System.out.println("Неизвестная команда");
                }

            } catch (Exception e) {
                System.out.println("ERR: " + e.getMessage());
            }
        }

        sc.close();
    }
}