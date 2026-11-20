package com.garden.alert;

import com.garden.enums.AlertSeverity;
import com.garden.enums.AlertType;

public record AlertResult(String subject, AlertType type, AlertSeverity severity, String message) {}
