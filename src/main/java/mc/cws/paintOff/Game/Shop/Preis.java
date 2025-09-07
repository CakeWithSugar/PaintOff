package mc.cws.paintOff.Game.Shop;

public class Preis {
    private static final double BASE_COST_PER_25_PERCENT = 20.0; // Basispreis pro 25% Bonus

    public static double calculateBoosterCost(double multiplier) {
        // Berechne den prozentualen Bonus über 100% hinaus
        double bonusPercent = (multiplier - 1.0) * 100; // bei 1,25 --> 125

        // Berechne die Kosten pro Runde
        double costPerRound = (bonusPercent / 25) * BASE_COST_PER_25_PERCENT; // bei 1,25 --> 100

        // Multipliziere mit der Dauer
        double totalCost = costPerRound * ShopInventory.DURATION_ROUNDS; // bei 1,25 und 10 Runden --> 1.000

        // Runde auf 5er-Schritte
        return Math.round(totalCost / 5) * 5; // bei 1,25 -> 1.000 : 5 --> 200
    }

    // Beispielwerte
    public static double basic = 500;
    public static double basic2 = 750;
    public static double basic3= 1000;
    public static double preis25 = calculateBoosterCost(1.25);  // ~200 Punkte
    public static double preis50 = calculateBoosterCost(1.5);   // ~400 Punkte
    public static double preis100 = calculateBoosterCost(2.0);  // ~800 Punkte
}
