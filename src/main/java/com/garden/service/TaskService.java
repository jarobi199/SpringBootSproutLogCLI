package com.garden.service;

import com.garden.alert.AlertManager;
import com.garden.authentication.SessionContext;
import com.garden.enums.PlantStatus;
import com.garden.enums.TaskType;
import com.garden.model.Bed;
import com.garden.model.Plant;
import com.garden.model.Task;
import com.garden.repository.BedRepository;
import com.garden.repository.PlantRepository;
import com.garden.repository.TaskRepository;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private BedRepository bedRepository;

    public void logTask(Plant plant, TaskType taskType, LocalDate date, String notes) {
        Optional<Bed> optionalBed = bedRepository.findById(plant.getBedId());
        Task task = new Task(SessionContext.getUser().getId(), plant.getId(), plant.getName(),
                optionalBed.map(Bed::getName).orElse(null), taskType, date, notes);
        taskRepository.save(task);

        if(TaskType.WATER.equals(taskType)) {
            plant.setLastWatered(date);
            plantRepository.save(plant);
        }
    }

    public void historyByPlant(Plant plant) {
        List<Task> tasks = taskRepository.findByPlantIdOrderByDateDesc(plant.getId());
        if(!tasks.isEmpty()) {
          displayAllTasks(tasks);
        }
    }

    public void displayAllTasks(List<Task> tasks ) {
        System.out.println("| LIST OF TASKS |");
        Table taskTable = Clique.table(TableType.BOX_DRAW)
                .headers(
                        "[blue, bold]PLANT NAME[/]",
                        "[blue, bold]BED NAME[/]",
                        "[blue, bold]TYPE[/]",
                        "[blue, bold]DATE[/]",
                        "[blue, bold]NOTES[/]"
                );
        tasks.forEach(task -> taskTable.row(task.getPlantName(), task.getBedName(), task.getType().toString(), task.getDate().toString(), task.getNotes()));
        taskTable.render();
        System.out.println();
    }

    public void displayCareSchedule() {
        List<Plant> plants = plantRepository.findByUserIdAndStatus(SessionContext.getUser().getId(), PlantStatus.GROWING)
                .stream().sorted(Comparator.comparing(Plant::getNextWateringDate)).toList();
        if(!plants.isEmpty()) {
            System.out.println("| CARE SCHEDULE |");
            Table plantTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[blue, bold]NAME[/]",
                            "[blue, bold]SPECIES[/]",
                            "[blue, bold]NEXT WATERING DATE[/]",
                            "[blue, bold]DAYS UNTIL DUE/OVERDUE[/]"
                    );
            plants.forEach(plant -> {
                LocalDate nextWateringDate = plant.getNextWateringDate();
                long daysUntilDue = LocalDate.now().until(nextWateringDate, ChronoUnit.DAYS);
                String daysUntilDueString = (daysUntilDue < 0) ? AlertManager.showWarning(String.valueOf(daysUntilDue)) : String.valueOf(daysUntilDue);
                plantTable.row(plant.getName(), plant.getSpeciesName(),
                        plant.getNextWateringDate().toString(), daysUntilDueString);
            });
        }

    }

    public List<Task> findAllTasks() {
        return taskRepository.findByUserId(SessionContext.getUser().getId());
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    public List<Task> findAllTasksByPlant(Plant plant) {
        return taskRepository.findByPlantIdOrderByDateDesc(plant.getId());
    }
}
