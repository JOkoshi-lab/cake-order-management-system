package com.example.demo;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@SpringBootApplication
@Controller
public class CakeShop {
    

public static void main(String[] args) {
        SpringApplication.run(CakeShop.class, args);
    }

    private final CakeDao dao;

    CakeShop(CakeDao dao) {
        this.dao = dao;
    }

    @RequestMapping("/")
    String home(Model model) {

        model.addAttribute(
                "cakeList",
                dao.select7());

        model.addAttribute(
                "salesList",
                dao.select10b());

        model.addAttribute(
                "totalSales",
                dao.totalSales());

        model.addAttribute(
                "ranking",
                dao.ranking());

        model.addAttribute(
                "orderCount",
                dao.orderCount());

        return "cakeshop";
    }

    @PostMapping("/order")
    String order(
            @RequestParam Map<String, String> params) {

        String orderId = UUID.randomUUID().toString();

        dao.insertOrderTime(orderId);

        for (String key : params.keySet()) {

            if (key.startsWith("qtt_")) {

                int cakeId = Integer.parseInt(
                        key.replace("qtt_", ""));

                int quantity = Integer.parseInt(
                        params.get(key));

                if (quantity > 0) {

                    dao.insertOrder(
                            orderId,
                            cakeId,
                            quantity);
                }
            }
        }

        return "redirect:/";
    }

    @PostMapping("/newcake")
    String newcake(
            @RequestParam String name,
            @RequestParam int price) {

        dao.newcake(name, price);

        return "redirect:/";
    }
}
