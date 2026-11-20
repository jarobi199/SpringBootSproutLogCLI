package com.garden.interfaces;

public interface AlertStrategy {
    List<AlertResult> evaluate(AlertContext ctx);
}