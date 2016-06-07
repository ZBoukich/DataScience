package utility;

import model.Item;
import model.TestModel;
import model.UserPreference;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Zahey Boukich on 4-6-2016.
 */
public class Utility {

    public static Map<Number, UserPreference> loadData(String file, String splitBy) {
        Map<Number, UserPreference> userPreferenceHashMap = new HashMap<>();
        try {
            File fileToParse = new File(file);
            String line = null;
            BufferedReader reader = new BufferedReader(new FileReader(fileToParse));
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(splitBy);

                UserPreference userPreference = new UserPreference();
                int userId = Integer.parseInt(tokens[0]);
                int itemId = Integer.parseInt(tokens[1]);
                double rating = Double.parseDouble(tokens[2]);
                if (userPreferenceHashMap.containsKey(userId)) {
                    userPreferenceHashMap.get(userId).addRating(itemId, rating);
                } else {
                    userPreference.addRating(itemId, rating);
                    userPreferenceHashMap.put(userId, userPreference);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userPreferenceHashMap;
    }


    public static List<TestModel> loadTestData(String file, String splitBy) {
        List<TestModel>items = new ArrayList<>();
        try {
            File fileToParse = new File(file);
            String line = null;
            BufferedReader reader = new BufferedReader(new FileReader(fileToParse));
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(splitBy);
                TestModel testModel = new TestModel();
                String name = tokens[0];
                int itemId = Integer.parseInt(tokens[1]);
                double rating = Double.parseDouble(tokens[2]);
                testModel.setName(name);
                testModel.setItemId(itemId);
                testModel.setRating(rating);
                items.add(testModel);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }
    public static List<Item> loadItemData(String file, String splitBy) {
        List<Item> itemsList = new ArrayList<>();
        try {
            File fileToParse = new File(file);
            String line = null;
            BufferedReader reader = new BufferedReader(new FileReader(fileToParse));
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(splitBy);
                String itemId = tokens[0];
                String [] id = itemId.split("|");
                int rID = Integer.parseInt(id[0]);
                String title = id[1];
                System.out.println("id: " + rID + " " + "title : " + title);
                Item item = new Item();
                item.setId(itemId);
                itemsList.add(item);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemsList;
    }





    public static void getStreamOfFile(String file, String delimiter) {

        Stream<String> itemsStream = null;
        try {
            Files.lines(Paths.get(file))
                    .map(line -> line.split(delimiter)) // Stream<String[]>
                    .flatMap(Arrays::stream) // Stream<String>
                    .distinct() // Stream<String>
                    .forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
