package com.mycompany.property_management.controller;

public class CalculatorMain {
    public static void main(String[] args) {
        CalculatorController cc = new CalculatorController();
        Double res = cc.add(14.3, 4.5);
        System.out.println(res);
    }
}
