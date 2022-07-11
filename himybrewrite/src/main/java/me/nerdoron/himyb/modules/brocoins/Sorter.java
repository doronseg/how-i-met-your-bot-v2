package me.nerdoron.himyb.modules.brocoins;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Sorter {

    public Sorter() {
    }

    public Map<String, Integer> sortMapLowMax(java.util.Map<String, Integer> m, int amountToShow) {
        java.util.Map<String,Integer> mm =
                m.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .limit(amountToShow)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return mm;
    }
    public Map<String, Integer> sortMapMaxLow(java.util.Map<String, Integer> m, int amountToShow) {
        java.util.Map<String,Integer> mm =
                m.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(amountToShow)
                        .collect(Collectors.toMap(
                                java.util.Map.Entry::getKey, java.util.Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return mm;
    }

}
