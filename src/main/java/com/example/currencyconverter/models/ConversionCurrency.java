package com.example.currencyconverter.models;



public class ConversionCurrency {


    private String to;

    private String from;


    private Double value;

    private double result;

    public ConversionCurrency() {

    }

    public ConversionCurrency(String to, String from, Double value) {
        this.to = to;
        this.from = from;
        this.value = value;

    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ConversionCurrency{" +
                "result=" + result +
                '}';
    }
}
