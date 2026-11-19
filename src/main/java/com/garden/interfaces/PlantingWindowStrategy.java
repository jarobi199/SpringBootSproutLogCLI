package com.garden.interfaces;

import com.garden.model.PlantingWindow;
import com.garden.model.Species;
import com.garden.model.User;

public interface PlantingWindowStrategy<T extends Species> {
    PlantingWindow window(T species, User user, int year);
}