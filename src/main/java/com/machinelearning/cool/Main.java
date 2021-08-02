package com.machinelearning.cool;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Static Movie Recommender
public class Main {

    /*
    * Dependencies used:
    * SLF4J, Apache Mahout, Google guava
    *
    * Dataset from:
    * MovieLens
    */

    public static void main(String[] args) {
        try {
            // Reading from .csv files
            // u.date = user id | item id | rating
            DataModel ratingsDataModel = new FileDataModel(new File("src/data/ratings.csv"));
            List<String> movies = getMovieNames("src/data/movies.csv");
            sendWelcomeMessage();

            // For getting the similarity coefficient
            TanimotoCoefficientSimilarity similarity = new TanimotoCoefficientSimilarity(ratingsDataModel);

            // Recommends items based on similarity
            GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(ratingsDataModel, similarity);

            // Looping through sets of recommendations
            long itemID;
            for(LongPrimitiveIterator items = ratingsDataModel.getItemIDs(); items.hasNext();) {
                itemID = items.nextLong();

                // Getting List of recommendations
                int recommendationsCount = 5;
                List<RecommendedItem> recommendations = recommender.mostSimilarItems(itemID, recommendationsCount);

                String movieName = movies.get((int)itemID);
                System.out.println("Recommendation for movie: " + movieName);
                System.out.println("------------------------------------------------------");

                // Printing recommendations
                for(RecommendedItem recommendation : recommendations) {
                    movieName = movies.get((int)recommendation.getItemID());
                    double itemSimilarity = Math.round(recommendation.getValue()*100.0*100.0)/100.0;
                    System.out.println(movieName + ", Similarity: " + itemSimilarity + "%");
                }

                itemID++;
                // Sets how many users should be given recommendations
                if(itemID > 5) System.exit(1);
                System.out.println("");
                System.out.println("");
            }
        } catch (IOException e) {
            System.out.println("There was an error.");
            e.printStackTrace();
        } catch (TasteException e) {
            System.out.println("There was a Taste Exception");
            e.printStackTrace();
        }
    }

    static private void sendWelcomeMessage()
    {
        System.out.println("");
        System.out.println("===========================");
        System.out.println("Movie Recommendation System");
        System.out.println("===========================");
        System.out.println("");
    }

    static private List<String> getMovieNames(String path) throws IOException {
        List<String> result = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        while((line = br.readLine()) != null) {
            result.add(line);
        }

        br.close();

        return result;
    }
}
