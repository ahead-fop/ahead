
package ProgramCube;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class PLMReader {

	ArrayList dimensions = new ArrayList();
	ArrayList dimensionNames = new ArrayList();
	
	ArrayList features = new ArrayList();
	String rootpath="";	
	String modelFile;
	public String errorMessage="";
	
	public void read(String plmFile) throws Exception{
		

		try {

	
	        File f = new File(plmFile);
	        FileReader fr = new FileReader(f);
	        BufferedReader buf = new BufferedReader(fr);
	
	        String data, token;
	        int section = 0, index = 0;
	        ArrayList entry;
	        
	        if ((data = buf.readLine()) != null){
	        	modelFile =data;
	        	System.out.println("Model file="+modelFile);
	        	
	        	try {
	        		File temp = new File(modelFile);
                    
                    //Absolute path does not exist, maybe it is a relative path
                    if (!temp.exists()){
                      temp = new File(f.getParent(), modelFile);
                        
                    }
	        		if (!temp.isFile())
	        			throw new Exception("file "+modelFile+" musts exist");
                    
	        	} catch(Exception e){
	        		errorMessage =  e.getMessage();
	        		return;
	        	}
	        }
	        else{
	        	System.err.println("PLM file seems to be empty!!");
	        	errorMessage = "PLM file seems to be empty!!";
	        	return;//error
	        }
	        while ((data = buf.readLine()) != null) { // Reading the model
	            
	            data = data.trim(); // Removing redundant whitespace
	            
	            if (data.startsWith("'")) { ; } // Comment
		        else if (data.startsWith("%%")) { // New section started
		            section++;
		        }
		        else if (data.equals("")) { ; } // Empty line
				else {
				    switch (section) {
			        	case 0: // Reading the root path
			        	    rootpath = data;
                            
                            // Check root path
                            try {
                                File temp = new File(rootpath);
                                if(rootpath.equals(".")){
                                    temp = new File(f.getParent(), rootpath);
                                    rootpath=temp.getAbsolutePath();
                                }
                                else if (!temp.exists()){
                                    temp = new File(f.getParent(), rootpath);
                                    rootpath=temp.getAbsolutePath();
                                }
                                
                                if (!temp.exists())
                                    throw new Exception("Root directory "+rootpath+" does not exist");
                                if (!temp.isDirectory())
                                    throw new Exception("root file "+rootpath+" not a directory");
                            } 
                            catch(Exception e){
                                e.printStackTrace();
                                
                            }
                            
                            
			        	    break;
			            
					    case 1: // Reading all the dimensions
					        entry = new ArrayList();
					        
					        if (data.indexOf(":")>0){
					        	String[] split = data.split(":");
					        	dimensionNames.add(split[0].trim());
					        	data = split[1].trim();
					        }
					        else
					        	break;
					        
					        
					        while (!data.equals("")) { // Reading dimension
					            index = data.indexOf(" ");
					            if (index == -1) {
					                entry.add(data);
					                break;
					            }
					            else {
						            token = data.substring(0, index);
						            entry.add(token);
						            data = data.substring(index);
					            }
					            data = data.trim();
					        }
					        dimensions.add(entry);
					        
					        break;
					        
				        case 2:
				            
				            index = data.indexOf(":");
				            if (index != -1) {
				                token = data.substring(0, index).trim();
				                data = data.substring(index + 1).trim();
				                //System.out.println("coordinate="+token+", feature(s)="+data);
				                
				                String[] temp_indexes=token.split(" ");
                                ArrayList<String> list = new ArrayList<String>();
                                for(int i=0; i< temp_indexes.length; i++){
                                    if(!temp_indexes[i].trim().equals(""))
                                        list.add(temp_indexes[i].trim());
                                }
                                String[] indexes = new String[list.size()];
                                indexes = list.toArray(indexes);
                                    
				                String[] featuresName=data.split(" ");
				                
				                for(int i=0;i<featuresName.length;i++){
				                	if (featuresName[i].trim().length()==0)
				                		continue;
				                	
				                	Feature fet = new Feature();
				                	fet.name=featuresName[i];
				                	fet.indexes=indexes;
				                	
				                	features.add(fet);
				                }
				                
				            }
				            break;
				            
				        default:
				            // trouble
				    }
				}
	        }
	        buf.close();
	        fr.close();
        
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}

		/*
		System.out.println("-----printing dimensions------");
		for(int i=0;i< dimensions.size(); i++)
			System.out.println(dimensions.get(i).getClass());
		
		System.out.println("-----printing rootpath------");
		System.out.println(rootpath);
		*/
		
		//ArrayList dims = new ArrayList();
		//ArrayList features = new ArrayList();
		//String rootpath;

	
	
	}
	
	public String getModelFileString(){
		return modelFile;
	} 
	
	public ArrayList getDimensions(){
		return dimensions;
	}

	public ArrayList getDimensionNames(){
		return dimensionNames;
	}
	
	public String getRootPath(){
		return rootpath;
	}
	
	public ArrayList getFeatures(){
		return features;
	}

	 
}
