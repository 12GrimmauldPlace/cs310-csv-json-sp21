package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
         
        String results = "";
        
        try {
            //string reader reads character streams; used to create CSVReader
            CSVReader reader = new CSVReader(new StringReader(csvString)); 
            //invoke CSVReader's readAll() method to parse the string to a List of string arrays
            List<String[]> full = reader.readAll(); 
            //Create Iterator for the parsed iterable structure 
            Iterator<String[]> iterator = full.iterator(); 
             
           String[] headers = iterator.next();                                  //get column headers
           JSONArray colHeaders = new JSONArray();                              //container for column headers
           for (String head: headers){                                          //iterate through headers array
                colHeaders.add(head);                                           //add column headers to JSONArray
           }
           JSONArray rowHeaders = new JSONArray();                              //container for row headers
           JSONArray dataArray = new JSONArray();                               //container for data (as integers)
           JSONObject jsonObject = new JSONObject();
           String[] record;                                                     //array to get next single line (record)
          
           while(iterator.hasNext()){  //iterate through all records
                record = iterator.next();                                       //get next record
                rowHeaders.add(record[0]);                                      //add next row header to rowHeader array
                List<Integer> data = new ArrayList<>(); 
                for(int i = 1; i < record.length; ++i){                         //iterate through rest of line
                    data.add(Integer.parseInt(record[i]));
                }
                
                dataArray.add(data);                                            //add integer array to dataArray
            }
           
           
           jsonObject.put("colHeaders", colHeaders);                            //add column headers key/value pair to JSONObject
           jsonObject.put("rowHeaders", rowHeaders);                            //add row headers key/value
           jsonObject.put("data", dataArray);                                   //add data arrays key/value

           results = JSONValue.toJSONString(jsonObject);                        //encode as a JSON string
            
        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        
        try {

            StringWriter writer = new StringWriter(); 
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            // INSERT YOUR CODE HERE
            //decode JSON string into JSONObject
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            
            //get JSONObject values into JSONArrays
            JSONArray colHeaders = (JSONArray)jsonObject.get("colHeaders"); 
            JSONArray rowHeaders = (JSONArray)jsonObject.get("rowHeaders"); 
            JSONArray data = (JSONArray)jsonObject.get("data"); 

            //store column headers (first row) in string array
            String[] csvColHeaders = new String[colHeaders.size()];
            for(int i = 0; i < colHeaders.size(); ++i){
                csvColHeaders[i] = colHeaders.get(i).toString();
            }
            //Encode column headers in CSV format
            csvWriter.writeNext(csvColHeaders);

            //get the next lines of data for csv
            for (int i = 0; i < rowHeaders.size(); ++i){//iterate through each row ID
                //create new String[] array for the next line; resets once we reach the next row header
                String[] csvNextLine = new String[colHeaders.size()];
                //set first item to current ID in JSONArray rowHeaders
                csvNextLine[0] = rowHeaders.get(i).toString();
                //get data item i into a String[] array
                String[] innerDataArray = new String[colHeaders.size()-1];
                JSONArray innerArray = (JSONArray)data.get(i);
                for (int k = 0; k < innerArray.size(); ++k){
                    innerDataArray[k] = innerArray.get(k).toString();
                }
                //add the elements in innerDataArray to the csvNextLine array
                for (int j = 0; j < innerDataArray.length; ++j){
                    csvNextLine[j+1] = innerDataArray[j];
                }
                //set next line in csv
                csvWriter.writeNext(csvNextLine);
            }

            //export data to CSV string
            results = writer.toString();
   
        }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}