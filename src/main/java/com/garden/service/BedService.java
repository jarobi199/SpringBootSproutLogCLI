package com.garden.service;

import com.garden.authentication.SessionContext;
import com.garden.enums.BedType;
import com.garden.enums.Sun;
import com.garden.model.Bed;
import com.garden.model.Plant;
import com.garden.repository.BedRepository;
import com.garden.repository.PlantRepository;
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
    private PlantRepository plantRepository;

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
        else
        {
            System.out.println("There are no beds listed");
        }
    }


    public void deleteBed(Bed bed) {
        List<Plant> plants = plantRepository.findByBedId(bed.getId());
        if (plants.isEmpty()) {
            bedRepository.delete(bed);
            System.out.println("The bed has been successfully deleted!");
        }
        else
        {
            System.out.println("This bed cannot be deleted since it is being referenced by a plant");
        }
    }

}
