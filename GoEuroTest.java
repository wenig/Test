import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoEuroTest
{
  	public static void main(String[] args) throws IOException
  	{
  		String position = "";
  		boolean header = false;
  		switch(args.length){
  		case 0:
  			System.out.println("Please enter a position: ");
  			position = new BufferedReader(new InputStreamReader(System.in)).readLine();
  			break;
  		case 1:
  			position = args[0];
  			break;
  		case 2:
  		default:
  			position = args[0];
  			if(args[1].equals("h"))
  			{ 
  				header = true;
  			}
  			break;
  		}
  		if(position != ""){
	  		String url = "http://api.goeuro.com/api/v2/position/suggest/en/"+position;
	     	InputStream is = new URL(url).openStream();
	     	try {
	      		BufferedReader br = new BufferedReader(new InputStreamReader(is));
	      		JSONArray json;
				try {
					json = new JSONArray(readJSON(br));
					System.out.println(toCSV(json, header));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	     	} finally {
	      		is.close();
	    	}
  		}else{
  			System.out.println("You should enter a position!");
  		}
  	}

  	private static String readJSON(Reader rd) throws IOException 
  	{
    	StringBuilder sb = new StringBuilder();
    	int cp;
    	while ((cp = rd.read()) != -1) {
    		sb.append((char) cp);
    	}
    	return sb.toString();
  	}

  	public static String toCSV(JSONArray json, boolean withHeaders){
  		try
		{
  			if(json.length() > 0){
		    	FileWriter writer = new FileWriter("GoEuroTest.csv");
		    	if(withHeaders){
		    		writer.append("_id");
		    		writer.append(',');
		    		writer.append("name");
		    		writer.append(',');
		    		writer.append("type");
		    		writer.append(',');
		    		writer.append("latitude");
		    		writer.append(',');
		    		writer.append("longitude");
		    		writer.append('\n');
		    	}
	 			for(int i = 0; i < json.length(); i++){
	 				JSONObject current = json.getJSONObject(i);
	 				writer.append(checkForComma(toString(current.getInt("_id"))));
	 				writer.append(',');
	 				writer.append(checkForComma(current.getString("name")));
	 				writer.append(',');
	 				writer.append(checkForComma(current.getString("type")));
	 				writer.append(',');
	 				JSONObject geo = current.getJSONObject("geo_position");
	 				writer.append(checkForComma(toString(geo.getDouble("latitude"))));
	 				writer.append(',');
	 				writer.append(checkForComma(toString(geo.getDouble("longitude"))));
	 				writer.append('\n');
	 			}
		 
			    writer.flush();
			    writer.close();
			    return "CSV file created!";
  			}else{
  				return "Entered position does not provide any results!";
  			}
		}
		catch(Exception e)
		{
		    return "An Error occurred:"+e;
		} 
  	}
  	
  	public static String toString(Object input){
  		StringBuilder sb = new StringBuilder();
  		sb.append(input);
  		return sb.toString();
  	}
  	
  	public static String checkForComma(String input){
  		if(input.indexOf(",") >= 0){
  			return "\""+input+"\"";
  		}
  		return input;
  	}
}