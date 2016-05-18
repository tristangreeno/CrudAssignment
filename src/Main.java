import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

class Main {

    private static HashMap<String, User> users = new HashMap<>();
    private static ArrayList<Food> foods = new ArrayList<>();

    public static void main(String[] args) {

                Spark.init();

                Spark.get(
                        "/",
                        (request, response) -> {
                            HashMap<Object, Object> m = new HashMap<>();
                            String foodId = request.queryParams("foodId");

                            Integer foodIdNum = -1;
                            if(foodId != null){
                                foodIdNum = Integer.parseInt(foodId);
                            }

                            ArrayList<Food> foodArrayList = new ArrayList<>();
                            for(Food f : foods){
                                if(f.id.equals(foodIdNum)){
                                    foodArrayList.add(f);
                                }
                            }

                            Session s = request.session();
                            String name = s.attribute("username");

                            m.put("food", foodArrayList);
                            m.put("foodId", foodIdNum);
                            m.put("username", name);
                            return new ModelAndView(m, "home.html");
                        },
                        new MustacheTemplateEngine()
                );

                Spark.post(
                        "/login",
                        (request, response) -> {
                            String username = request.queryParams("username");
                            String password = request.queryParams("password");

                            User user = users.get(username);

                            if(user == null){
                                user = new User(username, password);
                                users.put(username, user);
                            }

                            Session s = request.session();
                            s.attribute("username", username);

                            response.redirect("/");
                            return "";
                        }
                );

                Spark.post(
                        "/create-food",
                        (request, response) -> {
                            String foodName = request.queryParams("foodname");
                            Integer calories = Integer.parseInt(request.queryParams("calories"));
                            Session s = request.session();
                            String userName = s.attribute("username");
                            Integer foodId = Integer.parseInt(request.queryParams("foodId"));

                            if(userName == null) throw new Exception("User not logged in");
                            if(foodName == null) throw new Exception("Name is null");

                            Food f = new Food(foodName, calories, foodId);
                            foods.add(f);

                            response.redirect("/");
                            return "";
                        }
                );

                Spark.post(
                        "/edit-food",
                        (request, response) -> {
                            String foodName = request.queryParams("editname");
                            Integer calories = Integer.parseInt(request.queryParams("editcalories"));
                            Session s = request.session();
                            String userName = s.attribute("username");
                            Integer foodId = Integer.parseInt(request.queryParams("foodId"));
                            String originalName = foods.get(foodId).name;

                            if(userName == null) throw new Exception("User not logged in");
                            if(foodName == null) throw new Exception("Name is null");

                            Food f = new Food(foodName, calories, foodId);
                            foods.add(getArrayIndex(foods, originalName), f);
                            response.redirect("/");
                            return "";
                        }
                );

                Spark.post(
                        "/delete-food",
                        (request, response) -> {
                            Integer foodId = Integer.parseInt(request.queryParams("foodId"));
                            String originalName = foods.get(foodId).name;
                            foods.remove(getArrayIndex(foods, originalName));

                            response.redirect("/");
                            return "";
                        }
                );
            }

            private static int getArrayIndex(ArrayList<Food> array, String name) {

                int k=0;
                for(int i = 0;i < array.size(); i++){
                    if(Objects.equals(array.get(i).name, name)){
                        k = i ;
                        break;
                    }
                }
                return k;
            }
}
