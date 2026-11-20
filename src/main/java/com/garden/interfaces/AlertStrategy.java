package com.garden.interfaces;

import com.garden.alert.AlertContext;
import com.garden.alert.AlertResult;

import java.util.List;

public interface AlertStrategy {
    List<AlertResult> evaluate(AlertContext ctx);
}