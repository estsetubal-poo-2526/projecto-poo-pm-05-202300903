package com.example.projectopoopm05202300903.models.card;

import com.example.projectopoopm05202300903.models.enums.PlayerType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class UnitCardGroup {
    private final Map<PlayerType, List<UnitCard>> units = new EnumMap<>(PlayerType.class);

    public UnitCardGroup() {
        for (PlayerType type : PlayerType.values()) {
            units.put(type, new ArrayList<>());
        }
    }

    public List<UnitCard> getUnits(PlayerType type) {
        return units.get(type);
    }

    public void addUnit(PlayerType type, UnitCard unit) {
        units.get(type).add(unit);
    }

    public void removeUnit(PlayerType type, UnitCard unit) {
        units.get(type).remove(unit);
    }

    public List<UnitCard> getAllUnits() {
        List<UnitCard> all = new ArrayList<>();
        for (List<UnitCard> list : units.values()) {
            all.addAll(list);
        }
        return all;
    }

    public List<UnitCard> getEnemiesOf(PlayerType type) {
        List<UnitCard> enemies = new ArrayList<>();
        for (Map.Entry<PlayerType, List<UnitCard>> entry : units.entrySet()) {
            if (entry.getKey() != type) {
                enemies.addAll(entry.getValue());
            }
        }
        return enemies;
    }
}
