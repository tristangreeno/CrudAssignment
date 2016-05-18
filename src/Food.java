/**
 * Stores information about each food item, such as calories and name. Also stores each food with a unique ID for reference.
 */
class Food {
    Integer calories;
    String name;
    Integer id;

    public Food(String name, Integer calories, Integer id){
        this.name = name;
        this.calories = calories;
        this.id = id;
    }
}
