package com.garden.service;

import com.garden.authentication.SessionContext;
import com.garden.enums.BedType;
import com.garden.enums.Sun;
import com.garden.model.Bed;
import com.garden.model.Plant;
import com.garden.repository.BedRepository;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BedService {
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private PlantService plantService;

    public void addBed(String bedName, BedType bedType, Sun sunExposure, int squareFeet) {
        Bed bed = new Bed(SessionContext.getUser().getId(), bedName, bedType, sunExposure, squareFeet);
        bedRepository.save(bed);
    }

    public List<Bed> getAllBeds() {
        return bedRepository.findByUserId(SessionContext.getUser().getId());
    }

    public void listBeds() {
        List<Bed> beds = bedRepository.findByUserId(SessionContext.getUser().getId());
        System.out.println("| LIST OF BEDS |");
        if (!beds.isEmpty()) {
           displayBeds(beds);
        }
        else
        {
            System.out.println("There are no beds listed");
        }
    }

    public void displayBeds(List<Bed> beds) {
        Table bedTable = Clique.table(TableType.BOX_DRAW)
                .headers(
                        "[blue, bold]NAME[/]",
                        "[blue, bold]TYPE[/]",
                        "[blue, bold]SUN EXPOSURE[/]",
                        "[blue, bold]AREA SQUARE FEET[/]"
                );
        beds.forEach(bed -> bedTable.row(bed.getName(), bed.getBedType().name(), bed.getSunExposure().name(), String.valueOf(bed.getAreaSqFt())));
        bedTable.render();
    }


    public void deleteBed(Bed bed) {
        List<Plant> plants = plantService.findPlantsByBedId(bed.getId());
        if (plants.isEmpty()) {
            bedRepository.delete(bed);
            System.out.println("The bed has been successfully deleted!");
        }
        else
        {
            System.out.println("This bed cannot be deleted since it is being referenced by a plant");
        }
    }

    public void viewBedDetail(Bed bed) {
        System.out.println("| BED DETAIL |");
        displayBeds(List.of(bed));
        System.out.println();

        List<Plant> plants = plantService.findPlantsByBedId(bed.getId());
        if (!plants.isEmpty()) {
            System.out.println("| PLANTS |");
            Table plantTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[blue, bold]NAME[/]",
                            "[blue, bold]SPECIES NAME[/]",
                            "[blue, bold]STATUS[/]",
                            "[blue, bold]AREA SQUARE FEET[/]"
                    );
            plants.forEach(plant -> plantTable.row(plant.getName(), plant.getSpeciesName(), plant.getStatus().name(), String.valueOf(plant.getSquareFeet())));
            plantTable.render();
            System.out.println();
        }
        int usedSpace = plants.stream().mapToInt(Plant::getSquareFeet).sum();
        int freeSpace = bed.getAreaSqFt() - usedSpace;
        System.out.println("TOTAL USED SPACE: " +  usedSpace);
        System.out.println("TOTAL FREE SPACE: " +  freeSpace);
    }

    public void editBed(Bed bed, String bedName, Sun sunExposure, int squareFeet) {
        if(isSquareFeetAvailable(bed, squareFeet)) {
            bed.setName(bedName);
            bed.setSunExposure(sunExposure);
            bed.setAreaSqFt(squareFeet);
            bedRepository.save(bed);
        }
       else
       {
            System.out.println("You cannot edit this bed because the square feet value is invalid!");
        }
    }

    public boolean isSquareFeetAvailable(Bed bed, int squareFeet) {
        int totalUsedSquareFeet = squareFeetUsed(bed);
        return squareFeet > totalUsedSquareFeet;
    }

    public boolean isCrowded(Bed bed, int squareFeet) {
        int totalUsedSquareFeet = squareFeetUsed(bed);
        return (((squareFeet + totalUsedSquareFeet) * 100) / bed.getAreaSqFt()) < 10; //Crowded if there is less than 10% space available
    }

    public int squareFeetUsed(Bed bed) {
        return plantService.findPlantsByBedId(bed.getId()).stream().mapToInt(Plant::getSquareFeet).sum();
    }

    public void warnIfCrowded(Bed bed, int squareFeet) {
        if(isCrowded(bed, squareFeet)) {
            System.out.println("This bed is getting crowded! Less than 10% of the square feet is available.");
        }
    }

}
