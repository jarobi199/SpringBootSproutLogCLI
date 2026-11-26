package com.garden.service;

import com.garden.authentication.SessionContext;
import com.garden.model.Bed;
import com.garden.model.Harvest;
import com.garden.model.Plant;
import com.garden.repository.BedRepository;
import com.garden.repository.HarvestRepository;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class HarvestService {
    @Autowired
    private HarvestRepository harvestRepository;
    @Autowired
    private BedRepository bedRepository;

    public void logHarvest(Plant plant, LocalDate harvestDate, int quantity, String notes) {
        Bed bed = bedRepository.findById(plant.getBedId()).orElse(null);
        Harvest harvest = new Harvest(SessionContext.getUser().getId(), plant.getId(), plant.getName(), (bed != null) ? bed.getName() : "", plant.getSpeciesName(), harvestDate, quantity, plant.getYieldUnit(), notes);
        harvestRepository.save(harvest);
    }

    public List<Harvest> findAllHarvests() {
        return harvestRepository.findByUserId(SessionContext.getUser().getId());
    }

    public void deleteHarvest(Harvest harvest) {
        harvestRepository.delete(harvest);
    }

    public void historyByPlant(Plant plant) {
        List<Harvest> harvests = harvestRepository.findByPlantId(plant.getId()).stream().
                sorted(Comparator.comparing(Harvest::getDate)).toList();
        System.out.println("PLANT NAME: " + plant.getName());
        System.out.println("PLANT SPECIES: " + plant.getSpeciesName());
        System.out.println();
        System.out.println("| HARVESTS |");
        if (!harvests.isEmpty()) {
            Table harvestTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[blue, bold]BED NAME[/]",
                            "[blue, bold]DATE[/]",
                            "[blue, bold]QUANTITY[/]",
                            "[blue, bold]NOTES[/]"

                    );
            harvests.forEach(harvest -> {
                harvestTable.row(harvest.getBedName(), harvest.getDate().toString(), harvest.getQuantity() + " " + harvest.getYieldUnit(), harvest.getNotes());
            });
            harvestTable.render();
            System.out.println();

            //Seasonal totals
            Map<Integer, List<Harvest>> seasonalMap = new HashMap<Integer, List<Harvest>>();
            for (Harvest harvest : harvests) {
                if(seasonalMap.containsKey(harvest.getDate().getYear())) {
                    seasonalMap.get(harvest.getDate().getYear()).add(harvest);
                }
                else
                {
                    seasonalMap.put(harvest.getDate().getYear(), new ArrayList<>(List.of(harvest)));
                }
            }

            System.out.println("| SEASONAL TOTALS |");
            Table seasonalTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[blue, bold]YEAR[/]",
                            "[blue, bold]SEASONAL TOTAL[/]",
                            "[blue, bold]EXPECTED YIELD[/]",
                            "[blue, bold]POSITION[/]"

                    );
            for(Map.Entry<Integer, List<Harvest>> entry : seasonalMap.entrySet()) {
                int seasonTotal = entry.getValue().stream().mapToInt(Harvest::getQuantity).sum();
                seasonalTable.row(String.valueOf(entry.getKey()), String.valueOf(seasonTotal) + plant.getYieldUnit(), plant.getExpectedYield().toString(), plant.getExpectedYield().getPositionDisplay(seasonTotal));
            }
            seasonalTable.render();
        }
        else
        {
            System.out.println("There are no harvests found.");
        }
    }
}
